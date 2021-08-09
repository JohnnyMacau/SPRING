package com.macauslot.recruitmentadmin.controller;


import com.macauslot.recruitmentadmin.annotation.NoRepeatToken;
import com.macauslot.recruitmentadmin.annotation.UserLoginvalidation;
import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.entity.*;
import com.macauslot.recruitmentadmin.exception.UserSkipValidation;
import com.macauslot.recruitmentadmin.service.IHrService;
import com.macauslot.recruitmentadmin.service.RecruitmentService;
import com.macauslot.recruitmentadmin.util.ServerRoleTagEnum;
import com.macauslot.recruitmentadmin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dispatch")
public class WebPageController {


    private final RecruitmentService recruitmentService;
    private final IHrService hrService;

    @Autowired
    public WebPageController(RecruitmentService recruitmentService, IHrService hrService) {
        this.recruitmentService = recruitmentService;
        this.hrService = hrService;
    }


    @RequestMapping("/login")
    public String login(
            UserDTO userDTO,
            Model model) {
        model.addAttribute("user", userDTO);
        return "/backend/login";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/main")
    public String mainPage(
            UserDTO userDTO,
            Model model) {
        model.addAttribute("user", userDTO);
        return "/backend/main";
    }

    @UserLoginvalidation(needSetUserDTO = true)
    @RequestMapping("/main_sub")
    public String main_sub_Page(UserDTO userDTO, Model model) {
        model.addAttribute("user", userDTO);

        JobVO condition = new JobVO();
        condition.setPost_status("A");
        condition.setOrg("FLT");
        Page<JobVO> pageFlt = recruitmentService.getJobPage4Main(condition);

        model.addAttribute("countFlt", pageFlt.getTotalElements());
        model.addAttribute("fltList", pageFlt.getContent());


        condition.setPost_status("A");
        condition.setOrg("SLT");
        Page<JobVO> pageMslot = recruitmentService.getJobPage4Main(condition);

        model.addAttribute("countMslot", pageMslot.getTotalElements());
        model.addAttribute("mslotList", pageMslot.getContent());

        List<Object[]> applicantAndRegisterData4Main = recruitmentService.getApplicantAndRegisterData4Main();

        model.addAttribute("mslotRegister", applicantAndRegisterData4Main.get(0));
        model.addAttribute("mslotApplicant", applicantAndRegisterData4Main.get(1));

        model.addAttribute("fltRegister", applicantAndRegisterData4Main.get(2));
        model.addAttribute("fltApplicant", applicantAndRegisterData4Main.get(3));

        return "/backend/main_sub";
    }


    @UserLoginvalidation
    @RequestMapping("/setting/sourceType")
    public String sourceType(HttpServletRequest request, Model model) {
        return "/backend/setting/source_type_list";
    }

    @UserLoginvalidation
    @RequestMapping("/setting/editSourceType/{messageId}")
    public String editSourceType(
            @PathVariable("messageId") Integer messageId,
            HttpServletRequest request,
            Model model) {

     /*   Message m = new Message();
        m.setAstId(messageId);
        PageInfo<Message> page4ApplicantSourceType = recruitmentService.getPage4ApplicantSourceType(0, 0, 1, m, null, null);
        List<Message> messageList = page4ApplicantSourceType.getData();

        if (messageList.isEmpty()) {
            throw new DataNotFoundException("editSourceType--messageId " + messageId +
                    " is not exist");
        } else if (messageList.size() > 1) {
            throw new DuplicateKeyException("editSourceType--messageId " + messageId +
                    " DuplicateKey error");
        }
        model.addAttribute("applicantSourceType", messageList.get(0));*/

        List<ApplicantSourceDetail> applicantSourceDetailList = recruitmentService.getApplicantSourceDetailListByMessageId(messageId);

        model.addAttribute("applicantSourceType", recruitmentService.getMessageById(messageId));
        model.addAttribute("applicantSourceDetailList", applicantSourceDetailList);
        model.addAttribute("messageId", messageId);

        return "/backend/setting/edit_source_type";
    }

    @UserLoginvalidation
    @RequestMapping("/setting/blackList")
    public String blackList(HttpServletRequest request, Model model) {
        return "/backend/setting/black_list";
    }

    @UserLoginvalidation
    @RequestMapping("/setting/loginPermission")
    public String loginPermisson(HttpServletRequest request, Model model) {
        model.addAttribute("departmentList", recruitmentService.getDepartment());
        return "/backend/setting/login_permission";
    }

    @UserLoginvalidation
    @RequestMapping("/setting/r_group")
    public String recruitmentGroup(HttpServletRequest request, Model model) {
        model.addAttribute("departmentList", recruitmentService.getDepartment());
        model.addAttribute("recruitmentGroupNameList", recruitmentService.getRecruitmentGroupName());

        return "/backend/setting/r_group";
    }


    @UserLoginvalidation
    @RequestMapping("/setting/addBlackList")
    public String addBlackList(HttpServletRequest request, Model model) {
//        model.addAttribute("blackListReasonList", recruitmentService.getBlackListReasonList());
/*        ApplicantBlackList applicantBlackList = new ApplicantBlackList();
        model.addAttribute("applicantBlackList", applicantBlackList);*/
        return "/backend/setting/edit_black_list";
    }

    @UserLoginvalidation
    @RequestMapping("/setting/editBlackList/{applicantBlackListId}")
    public String editBlackList(HttpServletRequest request, Model model, @PathVariable("applicantBlackListId") Integer applicantBlackListId) {
        ApplicantBlackList applicantBlackList = recruitmentService.findByApplicantBlackListId(applicantBlackListId);
        model.addAttribute("applicantBlackList", applicantBlackList);
        return "/backend/setting/edit_black_list";
    }

    @UserLoginvalidation
    @RequestMapping("/setting/addRecruitmentGroup")
    public String addRecruitmentGroup(HttpServletRequest request, Model model) {
        model.addAttribute("type", "type_create");
        model.addAttribute("departmentList", recruitmentService.getDepartment());


        return "/backend/setting/add_r_group";
    }

    @UserLoginvalidation
    @RequestMapping("/setting/editRecruitmentGroup/{groupId}")
    public String editRecruitmentGroup(HttpServletRequest request, Model model, @PathVariable("groupId") Integer groupId) {
        model.addAttribute("type", "type_update");
//        model.addAttribute("departmentList", recruitmentService.getDepartment());
        RecruitmentGroupMemberVO group = recruitmentService.getRecruitmentGroupById(groupId);
        model.addAttribute("group", group);
        List<User> userList = recruitmentService.getUserListByDeptCode(group.getDeptCode());


        List<String> arrDepartmentHead = getArr(group.getDepartmentHead());
        List<String> arrInterviewer = getArr(group.getInterviewer());
        List<String> arrAdministrativePersonnel = getArr(group.getAdministrativePersonnel());

        List<String> userIdList = userList.stream().map(User::getUserName).collect(Collectors.toList());
        userList.addAll(getReduceUserList(arrDepartmentHead, userIdList));
        userList.addAll(getReduceUserList(arrInterviewer, userIdList));
        userList.addAll(getReduceUserList(arrAdministrativePersonnel, userIdList));






        /*
        for (User user : userList) {
            if (arrDepartmentHead.contains(user.getUserName())) {
                user.setDepartmentHead(true);
            }
            if (arrInterviewer.contains(user.getUserName())) {
                user.setInterviewer(true);
            }
            if (arrAdministrativePersonnel.contains(user.getUserName())) {
                user.setAdministrativePersonnel(true);
            }
        }*/


        model.addAttribute("userList", userList);
//        System.err.println(userList);


        return "/backend/setting/add_r_group";
    }

    private List<User> getReduceUserList(List<String> arr, List<String> userIdList) {
        List<String> reduceIdList = reduce(arr, userIdList);
        userIdList.addAll(reduceIdList);
        return reduceIdList.stream().map(recruitmentService::getUserByUserName).collect(Collectors.toList());
    }

    private List<String> getArr(String strArr) {
        if (strArr == null) {
            return Collections.emptyList();
        }
        String[] strings = strArr.split(",");
        return Arrays.asList(strings);
    }

    //差集 (list1 - list2)
    private List<String> reduce(List<String> list1, List<String> list2) {
        return list1.stream().filter(item -> !list2.contains(item)).collect(Collectors.toList());
    }


    /**
     * 管理職位--列表
     *
     * @param request
     * @param model
     * @return
     */
    @UserLoginvalidation
    @RequestMapping("/job/search")
    public String searchJob(HttpServletRequest request, Model model) {
        model.addAttribute("departmentList", recruitmentService.getDepartment());
        model.addAttribute("organizationList", recruitmentService.getOrganization());

        return "/backend/job/search";
    }

    /**
     * 開啟新空缺-add
     *
     * @param model
     * @return
     */
    @UserLoginvalidation
    @RequestMapping("/job/add")
    public String addJob(String activePlace, Model model) {
        model.addAttribute("addJob", true);


        model.addAttribute("departmentList", recruitmentService.getDepartment());
        model.addAttribute("organizationList", recruitmentService.getOrganization());
        model.addAttribute("templateList", recruitmentService.getJobTemplateList());
        model.addAttribute("activePlace", activePlace);


        return "/backend/job/edit";
    }

    /**
     * 管理職位-edit
     *
     * @param request
     * @param model
     * @return
     */
    @UserLoginvalidation
    @RequestMapping("/job/edit/{deptPosDetailId}")
    public String editJob(@PathVariable("deptPosDetailId") Integer deptPosDetailId, HttpServletRequest request, Model model) {
        model.addAttribute("editJob", true);

        DepartmentPositionDetailVO departmentPositionDetail = recruitmentService.getDepartmentPositionDetail(deptPosDetailId);
        model.addAttribute("departmentPositionDetail", departmentPositionDetail);
        List<RecruitmentGroup> groupList = recruitmentService.getRecruitmentGroupListByDepartmentCode(departmentPositionDetail.getDept_code());
        model.addAttribute("groupList", groupList);

        return "/backend/job/edit";
    }


    /**
     * 職位查看
     *
     * @param deptPosDetailId
     * @param request
     * @param model
     * @return
     */
    @UserLoginvalidation(serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/job/show/{deptPosDetailId}")
    public String showJob(@PathVariable("deptPosDetailId") Integer deptPosDetailId, HttpServletRequest request, Model model) {
        model.addAttribute("showJob", true);


        DepartmentPositionDetailVO departmentPositionDetail = recruitmentService.getDepartmentPositionDetail(deptPosDetailId);
        model.addAttribute("departmentPositionDetail", departmentPositionDetail);
        List<RecruitmentGroup> groupList = recruitmentService.getRecruitmentGroupListByDepartmentCode(departmentPositionDetail.getDept_code());
        model.addAttribute("groupList", groupList);
        System.err.println(groupList);
        return "/backend/job/edit";
    }

    /**
     * 職位草稿
     *
     * @param deptPosDetailId
     * @param request
     * @param model
     * @return
     */
    @UserLoginvalidation
    @RequestMapping("/job/editDraft/{deptPosDetailId}")
    public String editDraft(@PathVariable("deptPosDetailId") Integer deptPosDetailId, HttpServletRequest request, Model model) {
        model.addAttribute("editDraft", true);


        DepartmentPositionDetailVO departmentPositionDetail = recruitmentService.getDepartmentPositionDetail(deptPosDetailId);
        model.addAttribute("departmentPositionDetail", departmentPositionDetail);
        List<RecruitmentGroup> groupList = recruitmentService.getRecruitmentGroupListByDepartmentCode(departmentPositionDetail.getDept_code());
        model.addAttribute("groupList", groupList);

        return "/backend/job/edit";
    }


    /**
     * 職位範本
     *
     * @param deptPosDetailId
     * @param request
     * @param model
     * @return
     */
    @UserLoginvalidation
    @RequestMapping("/job/editTemplate/{deptPosDetailId}")
    public String editTemplate(@PathVariable("deptPosDetailId") Integer deptPosDetailId, HttpServletRequest request, Model model) {
        model.addAttribute("editTemplate", true);


        DepartmentPositionDetailVO departmentPositionDetail = recruitmentService.getDepartmentPositionDetail(deptPosDetailId);
        model.addAttribute("departmentPositionDetail", departmentPositionDetail);
        List<RecruitmentGroup> groupList = recruitmentService.getRecruitmentGroupListByDepartmentCode(departmentPositionDetail.getDept_code());
        model.addAttribute("groupList", groupList);
        return "/backend/job/edit";
    }


    @UserLoginvalidation
    @RequestMapping("/job/loadTemplate/{deptPosDetailId}")
    public String loadTemplate(
            @PathVariable("deptPosDetailId") Integer deptPosDetailId,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("loadTemplate", true);


        DepartmentPositionDetailVO jobTemplate = recruitmentService.getDepartmentPositionDetail(deptPosDetailId);
        List<DeptVO> departmentList = recruitmentService.getOrganizationDepartmentList(jobTemplate.getOrg_id());
        List<DepartmentPosition> positionList = recruitmentService.getDepartmentPositionList(jobTemplate.getDept_code(), jobTemplate.getOrg_id(), "A");
        List<RecruitmentGroup> groupList = recruitmentService.getRecruitmentGroupListByDepartmentCode(jobTemplate.getDept_code());

        model.addAttribute("groupList", groupList);
        model.addAttribute("organizationList", recruitmentService.getOrganization());
        model.addAttribute("departmentPositionDetail", jobTemplate);
        model.addAttribute("templateList", recruitmentService.getJobTemplateList());
        model.addAttribute("deptPosDetailId", deptPosDetailId);
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("positionList", positionList);

        return "/backend/job/edit";
    }


    @UserLoginvalidation
    @RequestMapping("/job/draft")
    public String jobDraft(Model model) {

        return "/backend/job/draft";
    }

    @UserLoginvalidation
    @RequestMapping("/job/closed")
    public String jobClosed(Model model) {
        List<ApplicantPositionProcessResult> resultList = recruitmentService.getDeniedProcessResultList();
        model.addAttribute("departmentList", recruitmentService.getDepartment());
        model.addAttribute("resultList", resultList);
        return "/backend/job/closed";
    }

    @UserLoginvalidation
    @RequestMapping("/job/opening")
    public String jobOpening(Model model) {
        List<ApplicantPositionProcessResult> resultList = recruitmentService.getDeniedProcessResultList();
        model.addAttribute("resultList", resultList);
        return "/backend/job/opening";
    }

    @UserLoginvalidation
    @RequestMapping("/job/template")
    public String jobTemplate(Model model) {

        return "/backend/job/template";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/profile/show/{applicant_info_id}")
    public String showProfile(UserDTO userDTO,
                              @PathVariable("applicant_info_id") Integer applicant_info_id,
                              HttpServletRequest request,
                              Model model) {
        ResumeVO resume = recruitmentService.getProfileWithMaskInspection(userDTO, applicant_info_id);

//        model.addAttribute("applyDataList", resume.getApplyDataVOList());
        model.addAttribute("personalInfo", resume.getApplicantInfoVO());
        model.addAttribute("idTypeList", resume.getMessageList());
        model.addAttribute("educationList", resume.getEducationList());
        model.addAttribute("certificationList", resume.getCertificationList());
        model.addAttribute("experienceList", resume.getExperienceList());
        model.addAttribute("languageList", resume.getLanguageList());
        model.addAttribute("otherSkillList", resume.getOtherSkillList());
        return "/backend/profile/show";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/profile/showOnlyOneApplyData/{applicant_pos_id}")
    public String showProfileAndOnlyOneApplyData(UserDTO userDTO,
                              @PathVariable("applicant_pos_id") Integer applicant_pos_id,
                              HttpServletRequest request,
                              Model model) {
        ResumeVO resume = recruitmentService.getTheResumeWithMaskInspection(userDTO, applicant_pos_id);

        model.addAttribute("applyDataList", resume.getApplyDataVOList());
        model.addAttribute("personalInfo", resume.getApplicantInfoVO());
        model.addAttribute("idTypeList", resume.getMessageList());
        model.addAttribute("educationList", resume.getEducationList());
        model.addAttribute("certificationList", resume.getCertificationList());
        model.addAttribute("experienceList", resume.getExperienceList());
        model.addAttribute("languageList", resume.getLanguageList());
        model.addAttribute("otherSkillList", resume.getOtherSkillList());
        return "/backend/profile/show";
    }


    @UserLoginvalidation
    @RequestMapping("/profile/showApplyData/{applicant_info_id}")
    public String showProfileAndApplyData(
            @PathVariable("applicant_info_id") Integer applicant_info_id,
            HttpServletRequest request,
            Model model) {
        ResumeVO resume = recruitmentService.getTheWholeResume(applicant_info_id);

        model.addAttribute("applyDataList", resume.getApplyDataVOList());
        model.addAttribute("personalInfo", resume.getApplicantInfoVO());
        model.addAttribute("idTypeList", resume.getMessageList());
        model.addAttribute("educationList", resume.getEducationList());
        model.addAttribute("certificationList", resume.getCertificationList());
        model.addAttribute("experienceList", resume.getExperienceList());
        model.addAttribute("languageList", resume.getLanguageList());
        model.addAttribute("otherSkillList", resume.getOtherSkillList());
        List<ApplicantPosition> positionList = hrService.getAllByApplicantInfoIdAndProcessStage(applicant_info_id);
        model.addAttribute("suspendAllApplicant", positionList.size() >= 3);
        return "/backend/profile/showApplyData";
    }

    @UserLoginvalidation
    @RequestMapping("/profile/editBasic/{applicant_info_id}")
    public String editBasicProfile(
            @PathVariable("applicant_info_id") Integer applicant_info_id,
            HttpServletRequest request,
            Model model) {
        ResumeVO resume = recruitmentService.getProfile(applicant_info_id);

//        model.addAttribute("applyDataList", resume.getApplyDataVOList());
        model.addAttribute("personalInfo", resume.getApplicantInfoVO());
        model.addAttribute("idTypeList", resume.getMessageList());
        model.addAttribute("educationList", resume.getEducationList());
        model.addAttribute("certificationList", resume.getCertificationList());
        model.addAttribute("experienceList", resume.getExperienceList());
        model.addAttribute("languageList", resume.getLanguageList());
        model.addAttribute("otherSkillList", resume.getOtherSkillList());

        List<ApplicantPosition> positionList = hrService.getAllByApplicantInfoIdAndProcessStage(applicant_info_id);
        model.addAttribute("suspendAllApplicant", positionList.size() >= 3);
        return "/backend/profile/editBasic";
    }

    @UserLoginvalidation
    @NoRepeatToken(producer = true)
    @RequestMapping("/profile/editDetail/{applicant_info_id}")
    public String getEditDetailPage(
            @PathVariable("applicant_info_id") Integer applicant_info_id,
            HttpServletRequest request,
            @RequestParam String applicationOrg,
            Model model) {
        System.err.println("||applicationOrg||---" + applicationOrg);


        ResumeVO resume = recruitmentService.getProfile(applicant_info_id);

//        model.addAttribute("applyDataList", resume.getApplyDataVOList());
        ApplicantInfoVO applicantInfoVO = resume.getApplicantInfoVO();
//        System.err.println("getCriminalRecord:"+applicantInfoVO.getCriminalRecord());

        if (!"H".equalsIgnoreCase(applicantInfoVO.getStatus())) {
            throw new IllegalArgumentException("沒有權限");
        }

        model.addAttribute("personalInfo", applicantInfoVO);
        model.addAttribute("idTypeList", resume.getMessageList());
        model.addAttribute("educationList", resume.getEducationList());
        model.addAttribute("certificationList", resume.getCertificationList());
        model.addAttribute("experienceList", resume.getExperienceList());
        model.addAttribute("languageList", resume.getLanguageList());
        model.addAttribute("otherSkillList", resume.getOtherSkillList());

        List<Message> messageList = recruitmentService.getMsgIdType(applicationOrg);
        model.addAttribute("messageList", messageList);
        List<ApplicantPosition> positionList = hrService.getAllByApplicantInfoIdAndProcessStage(applicant_info_id);
        model.addAttribute("suspendAllApplicant", positionList.size() >= 3);
        return "/backend/profile/editDetail";
    }

    @UserLoginvalidation
    @NoRepeatToken(producer = true, produceMethodName = "getEditDetailPage")
    @RequestMapping("/profile/newDetailByHR")
    public String newDetailByHR(
            /*  @PathVariable("applicant_info_id") Integer applicant_info_id,*/
            HttpServletRequest request,
            @RequestParam String applicationOrg,
            Model model
    ) {
        System.err.println("||applicationOrg||---" + applicationOrg);

      /*  ResumeVO resume = recruitmentService.getProfile(applicant_info_id);

//        model.addAttribute("applyDataList", resume.getApplyDataVOList());
        model.addAttribute("personalInfo", resume.getApplicantInfoVO());
        model.addAttribute("idTypeList", resume.getMessageList());
        model.addAttribute("educationList", resume.getEducationList());
        model.addAttribute("certificationList", resume.getCertificationList());
        model.addAttribute("experienceList", resume.getExperienceList());
        model.addAttribute("languageList", resume.getLanguageList());
        model.addAttribute("otherSkillList", resume.getOtherSkillList());*/
        List<Language> languageList = new ArrayList<>();
        if ("FLT".equalsIgnoreCase(applicationOrg)) {
            languageList.add(new Language("广东话"));
            languageList.add(new Language("英语"));
            languageList.add(new Language("普通话"));
        } else if ("SLT".equalsIgnoreCase(applicationOrg)) {
            languageList.add(new Language("廣東話"));
            languageList.add(new Language("英語"));
            languageList.add(new Language("國語"));
        }
        model.addAttribute("languageList", languageList);
        List<OtherSkill> otherSkillList = new ArrayList<>();
        otherSkillList.add(new OtherSkill("電腦技能", "c"));
        otherSkillList.add(new OtherSkill("其他技能", "o"));

        model.addAttribute("otherSkillList", otherSkillList);

        List<Message> messageList = recruitmentService.getMsgIdType(applicationOrg);
        model.addAttribute("messageList", messageList);

        return "/backend/profile/editDetail";
    }

    /**
     * /application/search_d.html 用，deprecated
     */
  /*  @UserLoginvalidation
    @RequestMapping("/application/history/{applicant_info_id}")
    public String jobApplyHistory(
            @PathVariable("applicant_info_id") Integer applicant_info_id,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("personalInfo", recruitmentService.getApplicantInfoById(applicant_info_id));
        List<JobApplyHistoryVO> jobApplyHistory = recruitmentService.getJobApplyHistory(applicant_info_id);
        model.addAttribute("jobApplyHistoryList", jobApplyHistory);
        return "/backend/application/history";
    }*/


    @UserLoginvalidation
    @RequestMapping("/application/inbox")
    public String applicationInbox(HttpServletRequest request, Model model) {
        model.addAttribute("departmentList", recruitmentService.getDepartment());
        model.addAttribute("openJobList", recruitmentService.getOpenJob());
        model.addAttribute("resultList", recruitmentService.getDeniedProcessResultList());
        model.addAttribute("processStatusList", recruitmentService.getProcessStatusList());
        return "/backend/application/inbox";
    }


    @UserLoginvalidation
    @RequestMapping("/application/favorite")
    public String applicationFavorite(HttpServletRequest request, Model model) {
        model.addAttribute("openJobList", recruitmentService.getOpenJob());
        model.addAttribute("resultList", recruitmentService.getDeniedProcessResultList());
        return "/backend/application/favorite";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/application/specified/{src}/{type}/{jobId}")
    public String applicationSpecified(HttpServletRequest request, Model model,
                                       @PathVariable("src") String src,
                                       @PathVariable("type") String type,
                                       @PathVariable("jobId") String jobId) {
        model.addAttribute("src", src);
        model.addAttribute("type", type);
        model.addAttribute("jobId", jobId);

        return "/backend/application/specified";
    }

    @UserLoginvalidation
    @RequestMapping("/profile/registered")
    public String profileRegistered(HttpServletRequest request, Model model) {
        return "/backend/profile/registered";
    }

    @UserLoginvalidation
    @RequestMapping("/profile/nonregistered")
    public String profileNonregistered(HttpServletRequest request, Model model) {
        return "nonregistered_deprecated";
    }

    @UserLoginvalidation
    @RequestMapping("/process/transferToDept")
    public String transferToDept(HttpServletRequest request, Model model) {
        List<JobVO> list = recruitmentService.getOpenJob();
        model.addAttribute("openJobList", list);
        return "/backend/process/transfer_to_dept";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/process/arrangeInterview")
    public String arrangeInterview(UserDTO userDTO, HttpServletRequest request, Model model) {
        Boolean isFLTAdmin = userDTO.getIsFLTAdmin();
        boolean isNotFltAdmin = isFLTAdmin == null || !isFLTAdmin;
        if (ServerRoleTagEnum.DEPT == userDTO.getRoleTag() && isNotFltAdmin) {
            throw new UserSkipValidation("非法操作:/process/arrangeInterview");
        }

        model.addAttribute("processStatusList", recruitmentService.getProcessStatusList());
        model.addAttribute("meetingRoomList", recruitmentService.getMeetingRoomList());
        List<JobVO> Joblist = recruitmentService.getOpenJob();
        model.addAttribute("openJobList", Joblist);
        return "/backend/process/arrange_interview";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/process/deptReply")
    public String deptReply(UserDTO userDTO, HttpServletRequest request, Model model) {
        List<JobVO> list = recruitmentService.getDeptOnlineJob(userDTO.getDeptCode());
        model.addAttribute("openJobList", list);
        model.addAttribute("user_dept", userDTO.getDeptCode());
        return "/backend/process/dept_reply";
    }

    @UserLoginvalidation(needSetUserDTO = true)
    @RequestMapping("/process/control")
    public String processControl(UserDTO userDTO, HttpServletRequest request, Model model) {
        List<JobVO> list = recruitmentService.getOpenJob();
        List<ApplicantPositionProcessResult> resultList = recruitmentService.getDeniedProcessResultList();
        model.addAttribute("resultList", resultList);
        model.addAttribute("openJobList", list);
        model.addAttribute("processStatusList", recruitmentService.getProcessStatusList());
        return "/backend/process/process_control";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/report/applicationStatus")
    public String applicationStatus(UserDTO userDTO, HttpServletRequest request, Model model) {
        List<JobVO> list = recruitmentService.getDeptOnlineJob(userDTO.getDeptCode());
        model.addAttribute("openJobList", list);
        model.addAttribute("user_dept", userDTO.getDeptCode());
        return "/backend/report/application_status";
    }

    @UserLoginvalidation(needSetUserDTO = true)
    @RequestMapping("/report/employed")
    public String employed(UserDTO userDTO, HttpServletRequest request, Model model) {
        List<JobVO> list = recruitmentService.getOpenJob();
        model.addAttribute("openJobList", list);

        return "/backend/report/employed";
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/report/recruitmentProgress")
    public String recruitmentProgress(UserDTO userDTO, HttpServletRequest request, Model model) {

        return "/backend/report/recruitment_progress";
    }

    //1|申請記錄總表
    @UserLoginvalidation
    @RequestMapping("/report/application_report")
    public String applicationReport(HttpServletRequest request, Model model) {
        model.addAttribute("departmentList", recruitmentService.getDepartment());
//        model.addAttribute("openJobList", recruitmentService.getOpenJob());
//        model.addAttribute("resultList", recruitmentService.getDeniedProcessResultList());


//        model.addAttribute("recruitmentSourceList", hrService.getRecruitmentSourceList());
        model.addAttribute("departmentPositionJobTitleList", recruitmentService.getDepartmentPositionJobTitleList());
        model.addAttribute("msgIdTypeList", recruitmentService.getMsgIdType());
        model.addAttribute("processResultList", recruitmentService.getAllProcessResultList());

        model.addAttribute("organizationList", recruitmentService.getOrganization());

        return "/backend/report/application_report";
    }

    //2|應徵者資料總表
    @UserLoginvalidation
    @RequestMapping("/report/candidate_information_report")
    public String candidateInformationReport(HttpServletRequest request, Model model) {
//        model.addAttribute("departmentList", recruitmentService.getDepartment());
//        model.addAttribute("recruitmentSourceList", hrService.getRecruitmentSourceList());
//        model.addAttribute("departmentPositionJobTitleList", recruitmentService.getDepartmentPositionJobTitleList());
        model.addAttribute("msgIdTypeList", recruitmentService.getMsgIdType());
//        model.addAttribute("processResultList", recruitmentService.getAllProcessResultList());
        return "/backend/report/candidate_information_report";
    }

    //3|應徵者申請總表
    @UserLoginvalidation
    @RequestMapping("/report/candidate_application_report")
    public String candidateApplicationReport(HttpServletRequest request, Model model) {
        model.addAttribute("organizationList", recruitmentService.getOrganization());
        return "/backend/report/candidate_application_report";
    }

    //4|申請及面試情況總表
    @UserLoginvalidation
    @RequestMapping("/report/application_interview_status_summary")
    public String applicationInterviewStatusSummary(HttpServletRequest request, Model model) {
        model.addAttribute("organizationList", recruitmentService.getOrganization());
        return "/backend/report/application_interview_status_summary";
    }

    //5|應徵者收集統計
    @UserLoginvalidation
    @RequestMapping("/report/candidate_collecting_statistics")
    public String candidateCollectingStatistics(HttpServletRequest request, Model model) {
        return "/backend/report/candidate_collecting_statistics";
    }


    @UserLoginvalidation
    @RequestMapping("/profile/getOpeningJobListPage")
    public String getOpeningJobListPage(HttpServletRequest request,
                                        @RequestParam String applicationOrg,
                                        @RequestParam("applicant_info_id") Integer applicant_info_id,

                                        Model model) {
        model.addAttribute("applicationOrg", applicationOrg);
        ApplicantInfo applicantInfo = recruitmentService.getApplicantInfoById(applicant_info_id);
        /*if (!"H".equalsIgnoreCase(applicantInfo.getStatus())) {
            throw new IllegalArgumentException("沒有權限");//  2021-4-20 17:37:13: 需求改为HR可以帮助自注册用户申请
        }*/
        if (!applicantInfo.getApplicationOrg().equalsIgnoreCase(applicationOrg)) {
            throw new IllegalArgumentException("applicationOrg error");
        }


        if ("SLT".equalsIgnoreCase(applicationOrg)) {
            applicationOrg = "SLT";
        }
        JobVO condition = new JobVO();
        condition.setPost_status("A");
        condition.setOrg(applicationOrg);
        Page<JobVO> voPage = recruitmentService.getJobPage4Main(condition);
        model.addAttribute("personalInfo", recruitmentService.getApplicantInfoById(applicant_info_id));

        model.addAttribute("OpeningJobListSize", voPage.getTotalElements());
        List<JobVO> OpeningJobList = voPage.getContent();
        List<Integer> list = hrService.getAllByApplicantInfoIdAndProcessStage(applicant_info_id).stream().map(ApplicantPosition::getDeptPosDetailId).collect(Collectors.toList());
        for (JobVO jobVO : OpeningJobList) {
            if (list.contains(jobVO.getDetail_id())) {
                jobVO.setThisOneLock(true);
            }
        }

        model.addAttribute("OpeningJobList", OpeningJobList);


        return "/backend/profile/openingJobList";
    }

    @UserLoginvalidation
    @NoRepeatToken(producer = true)
    @RequestMapping("/profile/apply_for_a_job_on_behalf_of/{dept_pos_detail_id}")
    public String getApplyJobPage(HttpServletRequest request,
                                  @PathVariable("dept_pos_detail_id") Integer deptPosDetailId,
                                  @RequestParam("applicant_info_id") Integer applicant_info_id,
                                  @RequestParam String applicationOrg,

                                  Model model) {

        List<DeptPositionDescVO> deptPositionDescVOList = hrService.getDepartmentPositionDesc(deptPosDetailId);
        model.addAttribute("DeptPositionDescVO", deptPositionDescVOList.get(0));
        ApplicantInfo applicantInfo = recruitmentService.getApplicantInfoById(applicant_info_id);
          /*if (!"H".equalsIgnoreCase(applicantInfo.getStatus())) {
            throw new IllegalArgumentException("沒有權限");//  2021-4-20 17:37:13: 需求改为HR可以帮助自注册用户申请
        }*/
        if (!applicantInfo.getApplicationOrg().equalsIgnoreCase(applicationOrg)) {
            throw new IllegalArgumentException("applicationOrg error");
        }
        model.addAttribute("personalInfo", applicantInfo);

        return "/backend/profile/applyJob";
    }


}
