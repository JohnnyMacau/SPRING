package com.macauslot.recruitmentadmin.controller;


import com.macauslot.recruitmentadmin.annotation.NoRepeatToken;
import com.macauslot.recruitmentadmin.annotation.UserLoginvalidation;
import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.entity.*;
import com.macauslot.recruitmentadmin.entity.group.Create;
import com.macauslot.recruitmentadmin.entity.group.Delete;
import com.macauslot.recruitmentadmin.entity.group.Update;
import com.macauslot.recruitmentadmin.exception.DataNotFoundException;
import com.macauslot.recruitmentadmin.exception.DuplicateKeyException;
import com.macauslot.recruitmentadmin.exception.UserSkipValidation;
import com.macauslot.recruitmentadmin.service.IHrService;
import com.macauslot.recruitmentadmin.service.RecruitmentService;
import com.macauslot.recruitmentadmin.util.*;
import com.macauslot.recruitmentadmin.vo.ProfileDetialVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operate")
public class OperateController {
    private static final Integer SUCCESS = 200;
    private static final Integer FAILURE = 500;

    @Value("${spring.mail.sender}")
    private String from;
//    @Value("${emailTemplatePath}")
//    private String templatePath;
//    @Value("${imagePath}")
//    private String imagePath;


    private final SendEmailUtils sendEmailUtils;

    private final RecruitmentService recruitmentService;

    private final IHrService hrService;

    @Autowired
    public OperateController(RecruitmentService recruitmentService, SendEmailUtils sendEmailUtils, IHrService hrService) {
        this.recruitmentService = recruitmentService;
        this.sendEmailUtils = sendEmailUtils;
        this.hrService = hrService;
    }

    @UserLoginvalidation
    @PostMapping("/setting/addApplicantSourceType/{messageId}")
    public ResponseResult<Void> addApplicantSourceType(
            @PathVariable("messageId") Integer messageId,
            @Validated({Create.class}) @RequestBody ValidList<ApplicantSourceDetail> sourceDetailList) {
        recruitmentService.autoHandleApplicantSourceDetailList(messageId,
                sourceDetailList.stream()
                        .sorted(Comparator.comparing(ApplicantSourceDetail::getId, Comparator.nullsLast(Integer::compareTo)))
                        .collect(Collectors.toList())
        );
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation(needSetUserDTO = true)
    @PostMapping("/setting/saveBlackList")
    public ResponseResult<Void> saveBlackList(UserDTO userDTO,
                                              @Validated({Create.class}) @RequestBody ApplicantBlackList applicantBlackList,
                                              HttpServletRequest request,
                                              @RequestParam(defaultValue = "false") Boolean checkDuplicate) {//用BindingResult則不拋異常

        if (checkDuplicate == null) {
            throw new IllegalArgumentException();
        } else if (checkDuplicate && recruitmentService.checkDuplicateBeforeSaveBlackList(applicantBlackList)) {
            throw new DuplicateKeyException("證件號碼: " + applicantBlackList.getIdNumber() + "  重複, 是否繼續保存?");
        } else {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            applicantBlackList.setLastModifyBy(userDTO.getEmployeeId());
            applicantBlackList.setLastModifyDate(now);
            recruitmentService.saveBlackList(applicantBlackList);
        }
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/setting/addRecruitmentGroup")
    public ResponseResult<Void> addRecruitmentGroup(String departmentCode,
                                                    String groupName,
                                                    @RequestParam("department_head[]") String[] department_head,
                                                    @RequestParam("interviewer[]") String[] interviewer,
                                                    @RequestParam("administrative_personnel[]") String[] administrative_personnel,

                                                    HttpServletRequest request) {

        RecruitmentGroup recruitmentGroup = new RecruitmentGroup();
        recruitmentGroup.setDepartmentCode(departmentCode);
        recruitmentGroup.setName(groupName);

        List<RecruitmentGroupMember> groupMemberList = new ArrayList<>();

        for (String s : department_head) {
            RecruitmentGroupMember groupMember = new RecruitmentGroupMember();
            groupMember.setRole("department_head");
            groupMember.setEmployeeId(s);
            groupMemberList.add(groupMember);
        }
        for (String s : interviewer) {
            RecruitmentGroupMember groupMember = new RecruitmentGroupMember();
            groupMember.setRole("interviewer");
            groupMember.setEmployeeId(s);
            groupMemberList.add(groupMember);
        }
        for (String s : administrative_personnel) {
            RecruitmentGroupMember groupMember = new RecruitmentGroupMember();
            groupMember.setRole("administrative_personnel");
            groupMember.setEmployeeId(s);
            groupMemberList.add(groupMember);
        }
        recruitmentService.addRecruitmentGroup(recruitmentGroup, groupMemberList);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/setting/editRecruitmentGroup")
    public ResponseResult<Void> editRecruitmentGroup(
            Integer groupId,
            String groupName,
            @RequestParam("department_head[]") String[] department_head,
            @RequestParam("interviewer[]") String[] interviewer,
            @RequestParam("administrative_personnel[]") String[] administrative_personnel,
            HttpServletRequest request) {


        RecruitmentGroup recruitmentGroup = new RecruitmentGroup();
        recruitmentGroup.setId(groupId);
        recruitmentGroup.setName(groupName);

        List<RecruitmentGroupMember> groupMemberList = new ArrayList<>();

        for (String s : department_head) {
            RecruitmentGroupMember groupMember = new RecruitmentGroupMember();
            groupMember.setRole("department_head");
            groupMember.setEmployeeId(s);
            groupMemberList.add(groupMember);
        }
        for (String s : interviewer) {
            RecruitmentGroupMember groupMember = new RecruitmentGroupMember();
            groupMember.setRole("interviewer");
            groupMember.setEmployeeId(s);
            groupMemberList.add(groupMember);
        }
        for (String s : administrative_personnel) {
            RecruitmentGroupMember groupMember = new RecruitmentGroupMember();
            groupMember.setRole("administrative_personnel");
            groupMember.setEmployeeId(s);
            groupMemberList.add(groupMember);
        }
        recruitmentService.editRecruitmentGroup(recruitmentGroup, groupMemberList);
        return new ResponseResult<>(SUCCESS);
    }


    @UserLoginvalidation(needSetUserDTO = true)
    @PostMapping(value = "/setting/uploadBlackList")
    public ResponseResult<Void> uploadExcel(UserDTO userDTO, @RequestParam("file") MultipartFile file, RedirectAttributes attributes, HttpServletRequest request) throws Exception {
        InputStream inputStream = file.getInputStream();
        List<List<String>> data = PoiUtil.readExcel(file);
        inputStream.close();
        recruitmentService.uploadBlackList(data, userDTO.getEmployeeId());
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation(needSetUserDTO = true)
    @PostMapping(value = "/setting/replaceBlackList")
    public ResponseResult<Void> replaceBlackList(UserDTO userDTO, @RequestParam("file") MultipartFile file, RedirectAttributes attributes, HttpServletRequest request) throws Exception {
        Boolean isHrSupervisor = userDTO.getIsHrSupervisor();
        if (isHrSupervisor == null || !isHrSupervisor) {
            throw new UserSkipValidation("warning: 你沒有該權限[hrSupervisors]");
        }

        InputStream inputStream = file.getInputStream();
        List<List<String>> data = PoiUtil.readExcel(file);
        inputStream.close();
        recruitmentService.replaceBlackList(data, userDTO.getEmployeeId());
        return new ResponseResult<>(SUCCESS);
    }


    @UserLoginvalidation(needSetUserDTO = true)
    @PostMapping("/job/edit")
    public ResponseResult<Void> editJob(UserDTO userDTO, @Validated({Update.class}) @RequestBody DepartmentPositionDetail departmentPositionDetail) {//用BindingResult則不拋異常
//        DateTool.checkDate(departmentPositionDetail.getStartDate(), departmentPositionDetail.getEndDate());
     /*   if (departmentPositionDetail.getRecruitmentGroupId()==null) {
            throw new UserSkipValidation("backend validation: 請選擇群組");
        }*/
        recruitmentService.editDepartmentPositionDetail(departmentPositionDetail, userDTO.getEmployeeId());
        return new ResponseResult<>(SUCCESS);
    }


    @UserLoginvalidation
    @PostMapping("/job/add")
    public ResponseResult<Void> addJob(@Validated({Create.class}) @RequestBody DepartmentPositionDetail departmentPositionDetail) {//用BindingResult則不拋異常
//    	DateTool.checkDate(departmentPositionDetail.getStartDate(), departmentPositionDetail.getEndDate());
      /*  if (departmentPositionDetail.getRecruitmentGroupId()==null) {
            throw new UserSkipValidation("backend validation: 請選擇群組");
        }*/
        recruitmentService.addDepartmentPositionDetail(departmentPositionDetail);
        return new ResponseResult<>(SUCCESS);
    }

    @PostMapping("/job/saveAsTemplate")
    public ResponseResult<Void> recruitmentSaveTemplate(@Validated({Update.class}) @RequestBody DepartmentPositionDetail departmentPositionDetail) {//用BindingResult則不拋異常
//    	DateTool.checkDate(departmentPositionDetail.getStartDate(), departmentPositionDetail.getEndDate());
        recruitmentService.saveAsNewTemplate(departmentPositionDetail);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/job/addDepartmentPosition")
    public ResponseResult<Void> addDepartmentPosition(@Validated({Create.class}) @RequestBody DepartmentPosition departmentPosition) {//用BindingResult則不拋異常

        departmentPosition.setStatus("A");
        System.err.println(departmentPosition);
        recruitmentService.insertDepartmentPosition(departmentPosition);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/job/deleteDepartmentPosition")
    public ResponseResult<Void> deleteDepartmentPosition(@Validated({Delete.class}) @RequestBody DepartmentPosition departmentPosition) {
        recruitmentService.deleteDepartmentPosition(departmentPosition);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/job/batchDelete")
    public ResponseResult<Void> batchDeleteJob(
            @RequestParam("batchDelete_checkboxs[]") Integer[] batchDeleteIds
    ) {
//        System.out.println(batchDeleteIds);
        recruitmentService.batchDeleteJob(batchDeleteIds);
        return new ResponseResult<>(SUCCESS);
    }


    @UserLoginvalidation
    @PostMapping("/job/batchSeal")
    public ResponseResult<Void> batchSealJobApplication(HttpServletRequest request, Model model) {

        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        int deptPosDetailId = Integer.parseInt(request.getParameter("deptPosDetailId"));
        String stageActionRemark = request.getParameter("stageActionRemark");
        recruitmentService.batchSeal(deptPosDetailId, user, stageActionRemark);

        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/job/closeAndSeal")
    public ResponseResult<Void> closeAndSealApplication(HttpServletRequest request, Model model) {

        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        int deptPosDetailId = Integer.parseInt(request.getParameter("deptPosDetailId"));
        String stageActionRemark = request.getParameter("stageActionRemark");
        recruitmentService.closeAndSeal(deptPosDetailId, user, stageActionRemark);

        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/job/updateStatus")
    public ResponseResult<Void> updateJobStatus(HttpServletRequest request, Model model) {

        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        int deptPosDetailId = Integer.parseInt(request.getParameter("deptPosDetailId"));
        String status = request.getParameter("status");
        recruitmentService.updateJobStatus(deptPosDetailId, status, user);

        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/profile/update/{type}")
    public ResponseResult<Void> updateProfile(
            @PathVariable String type,
            @Validated({Update.class}) @RequestBody ApplicantInfo applicantInfo) {
        Integer applicantInfoId = applicantInfo.getApplicantInfoId();

        ApplicantInfo dbData = recruitmentService.getApplicantInfoById(applicantInfoId);
        if (dbData == null) {
            throw new DataNotFoundException("backend validation: applicantInfoId:" + applicantInfoId +
                    " is not exist");
        }


        ApplicantInfo newApplicantInfo = new ApplicantInfo();


        switch (type) {
            case "updateName":
                checkUpdateNameStyle(applicantInfo, newApplicantInfo);
                break;
            case "updateIdCard":
                checkUpdateIdCardStyle(applicantInfo, dbData, newApplicantInfo);
                break;
            case "updateEmail":
                checkUpdateEmailStyle(applicantInfo, newApplicantInfo);
                break;

            default:
                throw new UserSkipValidation("backend validation: path type is not exist");

        }

        newApplicantInfo.setApplicantInfoId(dbData.getApplicantInfoId());
        newApplicantInfo.setApplicationOrg(dbData.getApplicationOrg());


//        System.err.println("INPUT: " + newApplicantInfo);

        recruitmentService.updateApplicantInfo(type, dbData, newApplicantInfo);

/*
        if ("updateIdCard".equals(type)||"updateEmail".equals(type)) {
            ApplicantInfo dbData = recruitmentService.getApplicantInfoById(applicantInfoId);
            try {

                EmailParamVO emailParam = new EmailParamVO();
                emailParam.setUserName(dbData.getUserName());
                emailParam.setPassword(dbData.getIdCardNumber());
//                emailParam.setUpdateContent("付款到账");
//                emailParam.setUpdatePerson("盖茨");
//                emailParam.setRemarks("成功到账");
                //此处to数组输入多个值，即可实现批量发送
                String [] to={"**********@163.cn"};

                String   mailSubject = "Thank you for your application - Goalslot";
                sendEmailUtils.thymeleafEmail(from, to, mailSubject, emailParam, "email/flt", null);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
*/
        return new ResponseResult<>(SUCCESS);
    }

    private void checkUpdateNameStyle(ApplicantInfo applicantInfo, ApplicantInfo newApplicantInfo) {
        String enFName = applicantInfo.getEnFName();

        if (StringUtils.isBlank(enFName)) {
            throw new UserSkipValidation("backend validation: 英文名字 不能為空");
        }

        newApplicantInfo.setEnFName(enFName.toUpperCase());
        String enLName = applicantInfo.getEnLName();
        if (StringUtils.isBlank(enLName)) {
            throw new UserSkipValidation("backend validation: 英文姓氏 不能為空");
        }
        if (!TextValidator.checkEnglish(enFName) || !TextValidator.checkEnglish(enFName)) {
            throw new UserSkipValidation("backend validation: 英文姓名格式不正確");

        }
        newApplicantInfo.setEnLName(enLName.toUpperCase());
        String cnFName = applicantInfo.getCnFName();
        String cnLName = applicantInfo.getCnLName();

        if (StringUtils.isBlank(cnFName) && StringUtils.isBlank(cnLName)) {

        } else if (StringUtils.isNotBlank(cnFName) && StringUtils.isNotBlank(cnLName)) {
            if (!TextValidator.checkChinese(cnFName) || !TextValidator.checkChinese(cnLName)) {
                throw new UserSkipValidation("backend validation: 中文姓名格式不正確");
            }
        } else {
            throw new UserSkipValidation("backend validation: 中文姓名不完整");
        }

        newApplicantInfo.setCnFName(cnFName);
        newApplicantInfo.setCnLName(cnLName);
    }

    private void checkUpdateIdCardStyle(ApplicantInfo applicantInfo, ApplicantInfo dbData, ApplicantInfo newApplicantInfo) {
        Integer idTypeId = applicantInfo.getIdTypeId();
        if (idTypeId == null || idTypeId == -1) {
            throw new UserSkipValidation("backend validation: 身份證類別 不能為空");
        }
        newApplicantInfo.setIdTypeId(idTypeId);
        String idCardNumber = applicantInfo.getIdCardNumber();
        if (StringUtils.isBlank(idCardNumber)) {
            throw new UserSkipValidation("backend validation: 身份證編號 不能為空");
        }
//            System.err.println("idTypeId"+idTypeId+"  len:"+idCardNumber.length());

        if (idTypeId == 17 && idCardNumber.length() != 8) {
            throw new UserSkipValidation("backend validation: 身份證號碼格式有誤");
        }
        if ("FLT".equalsIgnoreCase(dbData.getApplicationOrg())) {
            if (idTypeId == 39) {
                if (!TextValidator.checkForeignID(idCardNumber)) {
                    throw new UserSkipValidation("backend validation: 国外身份证格式错误");
                }
            } else {
                if (!TextValidator.checkID(idCardNumber)) {
                    throw new UserSkipValidation("backend validation: 国内身份证格式错误");
                }
            }
        }


        newApplicantInfo.setIdCardNumber(idCardNumber);
//        newApplicantInfo.setPassword(EncryptUtil.getMd5password(idCardNumber));
    }

    private void checkUpdateEmailStyle(ApplicantInfo applicantInfo, ApplicantInfo newApplicantInfo) {
        String emailAddress = applicantInfo.getEmailAddress();
        if (StringUtils.isBlank(emailAddress)) {
            throw new UserSkipValidation("backend validation: 電郵地址 不能為空");
        }
        if (!TextValidator.checkEmail(emailAddress)) {
            throw new UserSkipValidation("backend validation: 電郵地址格式有誤");
        }
        newApplicantInfo.setEmailAddress(emailAddress);
        newApplicantInfo.setUserName(emailAddress);
    }


    @UserLoginvalidation
//    @NoRepeatToken(consumer = true, consumeMethodName = "getEditDetailPage")//由于后端防重和后端验证冲突了，因此不做后端防重的了，改做前端防重；
    @PostMapping("/profileDetail/newORupdate")
    public ResponseResult<Void> newORupdateProfileDetail(HttpServletRequest request, ProfileDetialVO profileDetialVO) {

        System.err.println("---------------[profileDetialVO] ----------- " + profileDetialVO);

        ApplicantInfo applicantInfo = profileDetialVO.getApplicantInfo();
        if (applicantInfo == null) {
            throw new UserSkipValidation("backend applicantInfo==null");
        }
        Integer applicantInfoId = applicantInfo.getApplicantInfoId();
        boolean newForm = applicantInfoId == null;

        checkUpdateNameStyle(applicantInfo, applicantInfo);
        checkUpdateIdCardStyle(applicantInfo, applicantInfo, applicantInfo);
        checkUpdateEmailStyle(applicantInfo, applicantInfo);


        if (!newForm) {
            recruitmentService.checkDuplicatedEmail(applicantInfo);
            recruitmentService.checkDuplicatedIdCardNumber(applicantInfo);
        }


        List<Education> educationList = profileDetialVO.getEducationList();
        if (educationList == null) {
            educationList = Collections.emptyList();
        }
        List<Certification> certificationList = profileDetialVO.getCertificationList();
        if (certificationList == null) {
            certificationList = Collections.emptyList();
        }
        List<WorkingExperience> workingExperienceList = profileDetialVO.getWorkingExperienceList();
        if (workingExperienceList == null) {
            workingExperienceList = Collections.emptyList();
        }
        for (WorkingExperience workingExperience : workingExperienceList) {
            workingExperience.setAllowance("Deprecated");
        }
        RelativeInfo relativeInfo = profileDetialVO.getRelativeInfo();
        if (StringUtils.isBlank(relativeInfo.getName())) {
            relativeInfo = null;
        }
        List<Language> languageList = profileDetialVO.getLanguageList();


        if ("FLT".equalsIgnoreCase(applicantInfo.getApplicationOrg())) {
            applicantInfo.setMartialStatus("Deprecated");
            for (Language language : languageList) {
                String languageName = language.getName();
                if ("廣東話".equals(languageName)) {
                    language.setName("广东话");
                } else if ("英語".equals(languageName)) {
                    language.setName("英语");
                } else if ("國語".equals(languageName)) {
                    language.setName("普通话");
                }
            }

        } else if ("SLT".equalsIgnoreCase(applicantInfo.getApplicationOrg())) {
            for (Language language : languageList) {
                String languageName = language.getName();
                if ("广东话".equals(languageName)) {
                    language.setName("廣東話");
                } else if ("英语".equals(languageName)) {
                    language.setName("英語");
                } else if ("普通话".equals(languageName)) {
                    language.setName("國語");
                }
            }
        }
        List<OtherSkill> otherSkillList = profileDetialVO.getOtherSkillList();
    /*    for (OtherSkill otherSkill : otherSkillList) {
            if ("電腦技能".equals(otherSkill.getName())) {
                otherSkill.setName("computer skill");
            } else if ("其他技能".equals(otherSkill.getName())) {
                otherSkill.setName("other skill");
            }
        }*/
        for (int i = 0; i < otherSkillList.size(); i++) {
            OtherSkill otherSkill = otherSkillList.get(i);
            if (i == 0 || "電腦技能".equals(otherSkill.getName())) {
                otherSkill.setName("computer skill");
            } else if (i == 1 || "其他技能".equals(otherSkill.getName())) {
                otherSkill.setName("other skill");
            }
        }

        hrService.newOrEditDetailPofile(
                newForm,
                applicantInfo,
                relativeInfo,
                educationList,
                certificationList,
                workingExperienceList,
                languageList,
                otherSkillList
        );

        return new ResponseResult<>(SUCCESS);

    }


    @UserLoginvalidation
    @PostMapping("/profile/batchUpdate")
    public ResponseResult<Void> batchUpdateProfile(
//            List<ApplicantInfo> applicantInfoList
            @RequestParam("applicant_info_ids[]") Integer[] applicant_info_ids,
            @RequestParam("batchChangeRemarks[]") String[] batchChangeRemarks,
            @RequestParam("batchProcessStage[]") String[] batchProcessStage,
            @RequestParam("batch_applicant_position_id[]") Integer[] batch_applicant_position_id


    ) {

        int length = applicant_info_ids.length;
        if (length == 0 ||
                length != batchChangeRemarks.length ||
                length != batchProcessStage.length ||
                length != batch_applicant_position_id.length) {
            throw new UserSkipValidation("batchChangeApplicantInfo length error");
        }

        List<ApplicantInfo> applicantInfoList = new ArrayList<>();
        List<ApplicantPosition> applicantPositionList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (i == 0 || !applicant_info_ids[i].equals(applicant_info_ids[i - 1])) {
                ApplicantInfo applicantInfo = new ApplicantInfo();
                applicantInfo.setApplicantInfoId(applicant_info_ids[i]);
                applicantInfo.setApplicationOrg(batchChangeRemarks[i]);
                applicantInfoList.add(applicantInfo);
            }

            ApplicantPosition applicantPosition = new ApplicantPosition();
            applicantPosition.setApplicantPosId(batch_applicant_position_id[i]);
            applicantPosition.setProcessStage(Float.parseFloat(batchProcessStage[i]));
            applicantPositionList.add(applicantPosition);
        }
        recruitmentService.batchChangeApplicantInfo(applicantInfoList, applicantPositionList);
        return new ResponseResult<>(SUCCESS);

    }




    @UserLoginvalidation
    @PostMapping("/application/batchBookmark")
    public ResponseResult<Void> batchBookmark(HttpServletRequest request,
                                              @RequestParam("batchProcess_checkboxs[]") Integer[] applicationIds,
                                              @RequestParam("bookmarkJobId") Integer bookmarkJobId
    ) {
        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        recruitmentService.batchBookmark(applicationIds, bookmarkJobId, user);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/application/batchTransferToDept")
    public ResponseResult<Void> batchTransferToDept(HttpServletRequest request,
                                                    @RequestParam("batchProcess_checkboxs[]") Integer[] applicationIds
    ) {
        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        recruitmentService.batchTransfer(applicationIds, user);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation(serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @PostMapping("/application/batchDeptReply")
    public ResponseResult<Void> batchDeptReply(HttpServletRequest request,
                                               @RequestParam("batchProcess_checkboxs[]") Integer[] applicationIds,
                                               @RequestParam("batchReply[]") String[] batchReply,
                                               @RequestParam("interview_time[]") String[] interview_time
    ) {
        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        recruitmentService.batchDeptReply(applicationIds, batchReply, interview_time, user);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @PostMapping("/application/batchArrangeInterview")
    public ResponseResult<Void> batchArrangeInterview(UserDTO userDTO, HttpServletRequest request,
                                                      @RequestParam("batchProcess_checkboxs[]") Integer[] applicationIds,
                                                      @RequestParam("batch_interview_time[]") String[] batch_interview_time,
                                                      @RequestParam("batch_meeting_room[]") String[] batch_meeting_room
    ) {
        Boolean isFLTAdmin = userDTO.getIsFLTAdmin();
        boolean isNotFltAdmin = isFLTAdmin == null || !isFLTAdmin;
        if (ServerRoleTagEnum.DEPT == userDTO.getRoleTag() && isNotFltAdmin) {
            throw new UserSkipValidation("非法操作:/application/batchArrangeInterview");
        }
        String user = userDTO.getEmployeeId();
        recruitmentService.batchArrangeInterview(applicationIds, batch_interview_time, batch_meeting_room, user);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/application/batchSaveResult")
    public ResponseResult<Void> batchSaveResult(HttpServletRequest request,
                                                @RequestParam("batchProcess_checkboxs[]") Integer[] applicationIds,
                                                @RequestParam("batchProcess_result[]") String[] batchProcess_result
    ) {
        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        recruitmentService.batchSaveResult(applicationIds, batchProcess_result, user);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/application/process")
    public ResponseResult<Void> applicationProcess(HttpServletRequest request,
                                                   @RequestParam("applicationId") Integer applicationId,
                                                   @RequestParam("action") Integer action,
                                                   @RequestParam("actionRemark") String actionRemark,
                                                   @RequestParam("backStage") Integer backStage
    ) {
        String user = ((UserDTO) request.getSession().getAttribute("user")).getEmployeeId();
        recruitmentService.applicationProcess(applicationId, action, actionRemark, backStage, user);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @PostMapping("/application/batchInformApplicantOfInterview")
    public ResponseResult<Void> batchInformApplicantOfInterview(UserDTO userDTO, HttpServletRequest request,
                                                     @RequestParam("batchProcess_checkboxs[]") Integer[] applicationIds
    ) {
        Boolean isFLTAdmin = userDTO.getIsFLTAdmin();
        boolean isNotFltAdmin = isFLTAdmin == null || !isFLTAdmin;
        if (ServerRoleTagEnum.DEPT == userDTO.getRoleTag() && isNotFltAdmin) {
            throw new UserSkipValidation("非法操作:/application/batchInformApplicant");
        }
        String user = userDTO.getEmployeeId();
        recruitmentService.batchInformApplicantOfInterview(applicationIds, user);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @PostMapping("/application/batchInformDeptOfInterview")
    public ResponseResult<Void> batchInformDeptOfInterview(UserDTO userDTO, HttpServletRequest request,
                                                @RequestParam("batchProcess_checkboxs[]") Integer[] applicationIds
    ) {
        Boolean isFLTAdmin = userDTO.getIsFLTAdmin();
        boolean isNotFltAdmin = isFLTAdmin == null || !isFLTAdmin;
        if (ServerRoleTagEnum.DEPT == userDTO.getRoleTag() && isNotFltAdmin) {
            throw new UserSkipValidation("非法操作:/application/batchInformDept");
        }
        String user = userDTO.getEmployeeId();
        recruitmentService.batchInformDeptOfInterview(applicationIds, userDTO);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/setting/applyBlackListData")
    public ResponseResult<Void> applyBlackListData(HttpServletRequest request) {
        recruitmentService.applyBlackListData();
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/setting/applyUserPermissionData")
    public ResponseResult<Void> applyUserPermissionData(HttpServletRequest request, String userList, String lostStr) {
        if (userList.length() > 0) {
            recruitmentService.applyUserPermissionData(userList);
        }
        if (lostStr.length() > 0) {
            recruitmentService.lostUserPermissionData(lostStr);
        }
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/setting/batchDeleteBlackList")
    public ResponseResult<Void> batchDeleteBlackList(HttpServletRequest request,
                                                     @RequestParam("batchProcess_checkboxs[]") Integer[] blackList) {
        recruitmentService.batchDeleteBlackList(blackList);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @PostMapping("/setting/batchDeleteRecruitmentGroup")
    public ResponseResult<Void> batchDeleteRecruitmentGroup(HttpServletRequest request,
                                                            @RequestParam("batchProcess_checkboxs[]") Integer[] groupList) {
        recruitmentService.batchDeleteRecruitmentGroup(groupList);
        return new ResponseResult<>(SUCCESS);
    }

    @UserLoginvalidation
    @NoRepeatToken(consumer = true, consumeMethodName = "getApplyJobPage")
    @PostMapping(value = "/profile/apply_for_a_job_on_behalf_of", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> apply_for_a_job_on_behalf_of(
//            @RequestParam Integer department_select1,
//            @RequestParam Integer title1,
//            @RequestParam Integer department_select2,
//            @RequestParam Integer title2,
//            @RequestParam Integer department_select3,
//            @RequestParam Integer title3,
            @RequestParam Integer topTitle,

            @RequestParam(value = "title", required = false) Integer[] titles,
            @RequestParam String expectedSalary,
            @RequestParam String expectedSalaryType,

          /*  @RequestParam String day,
            @RequestParam String month,
            @RequestParam String year,*/
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date availableWorkDate,

            @RequestParam Integer noticeDay,
            @RequestParam String noticeDayType,

            @RequestParam Integer sourceId,

            @RequestParam(required = false, defaultValue = "") String sourceRef,
            String sourceRef1,
            String sourceRef2,
            String sourceRef3,

            @RequestParam String type,


            @RequestParam Integer needShift,
            @RequestParam Integer onShift_total,

            @RequestParam(value = "dayofweek", required = false) String[] dayofweek,
            @RequestParam(value = "from_hour[]", required = false) Integer[] from_hour,
            @RequestParam(value = "from_min[]", required = false) Integer[] from_min,
            @RequestParam(value = "to_hour[]", required = false) Integer[] to_hour,
            @RequestParam(value = "to_min[]", required = false) Integer[] to_min,
            Integer applicantSourceDetailId,
            @RequestParam("applicant_info_id") Integer applicant_info_id,
            HttpServletRequest request
    ) throws ParseException {


        if (
//                department_select1 == null ||
//                        title1 == null ||
//                        department_select1 == -1 ||
//                        title1 == -1 ||
//                        department_select2 == null ||
//                        title2 == null ||
//                        department_select3 == null ||
//                        title3 == null ||
                topTitle == null ||
                        topTitle == -1 ||
                        StringUtils.isBlank(expectedSalary) ||
                        StringUtils.isBlank(expectedSalaryType) ||
               /*         StringUtils.isBlank(noticeDayType) ||
                        StringUtils.isBlank(year) ||
                        StringUtils.isBlank(month) ||
                        StringUtils.isBlank(day) ||
                        noticeDay == null ||*/
                        sourceId == null ||

                        needShift == null ||
                        onShift_total == null
        ) {
            throw new UserSkipValidation("绕过前端验证");
        }
        /*ApplicantInfo applicantInfo = recruitmentService.getApplicantInfoById(applicant_info_id);
         if (!"H".equalsIgnoreCase(applicantInfo.getStatus())) {
            throw new IllegalArgumentException("沒有權限");//  2021-4-20 17:37:13: 需求改为HR可以帮助自注册用户申请
        }*/

//        System.err.println(department_select1);
//        System.err.println(title1);


        Integer visitorId = applicant_info_id;

//        List<ApplicantPosition> applicantPositionList = new ArrayList<>();

        ApplicantPosition position = new ApplicantPosition();
        position.setApplicantInfoId(visitorId);
        position.setDeptPosDetailId(topTitle);
        position.setWhoGenerate("HR");
        position.setJobPriority(1);


        position.setExpectedSalary(expectedSalary);
        position.setExpectedSalaryType(expectedSalaryType);
        if (availableWorkDate != null) {
            position.setAvailableWorkDate(availableWorkDate);
        }
        if (noticeDay != null) {
            position.setNoticeDay(noticeDay);
            position.setNoticeDayType(noticeDayType);
        }
        position.setSourceId(sourceId);

        position.setSourceRef(sourceRef);

        if (StringUtils.isNotBlank(sourceRef1)) {
            position.setSourceRef(replaceWithComma(sourceRef1) + "||" + replaceWithComma(sourceRef2) + "||" + replaceWithComma(sourceRef3));
        }

        position.setApplicantSourceDetailId(applicantSourceDetailId);
        position.setProcessStage(1);

//        applicantInfoService.updateTop2(visitorId, a1, a2, a3, a);
        ApplicantPosition dbData = hrService.updateTop2(visitorId, position);










        /*
        开始top6的工作...
         */


        if (needShift == 0) {
            hrService.updateTop6(visitorId, dbData.getApplicantPosId(), Collections.emptyList());
            return new ResponseResult<>(SUCCESS);
        }
        List<OnShift> onShiftList = new ArrayList<>();


        for (int i = 0; i < onShift_total; i++) {
            OnShift o = new OnShift();
//            o.setDayofweek(String.valueOf(i + 1));
//            o.setDayofweek("every day");
            o.setDayofweek(dayofweek[i]);
            o.setFromDate(from_hour[i] + ":" + from_min[i]);
            o.setToDate(to_hour[i] + ":" + to_min[i]);
            o.setApplicantInfoId(visitorId);
            o.setApplicantPositionId(dbData.getApplicantPosId());
            onShiftList.add(o);
        }

        hrService.updateTop6(visitorId, dbData.getApplicantPosId(), onShiftList);


        return new ResponseResult<>(SUCCESS);


    }

    private static String replaceWithComma(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.replaceAll("\\|\\|", ",");
    }
}
