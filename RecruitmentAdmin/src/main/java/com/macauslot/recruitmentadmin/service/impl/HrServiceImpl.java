package com.macauslot.recruitmentadmin.service.impl;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.macauslot.recruitmentadmin.exception.UserSkipValidation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.macauslot.recruitmentadmin.entity.ApplicantBlackList;
import com.macauslot.recruitmentadmin.entity.ApplicantInfo;
import com.macauslot.recruitmentadmin.entity.ApplicantPosition;
import com.macauslot.recruitmentadmin.entity.ApplicantPositionProcessResult;
import com.macauslot.recruitmentadmin.entity.ApplicantSourceDetail;
import com.macauslot.recruitmentadmin.entity.BaseEntity;
import com.macauslot.recruitmentadmin.entity.Certification;
import com.macauslot.recruitmentadmin.entity.DepartmentPosition;
import com.macauslot.recruitmentadmin.entity.DepartmentPositionDetail;
import com.macauslot.recruitmentadmin.entity.Education;
import com.macauslot.recruitmentadmin.entity.Language;
import com.macauslot.recruitmentadmin.entity.OnShift;
import com.macauslot.recruitmentadmin.entity.OtherSkill;
import com.macauslot.recruitmentadmin.entity.RelativeInfo;
import com.macauslot.recruitmentadmin.entity.WorkingExperience;
import com.macauslot.recruitmentadmin.exception.DataNotFoundException;
import com.macauslot.recruitmentadmin.exception.DuplicateKeyException;
import com.macauslot.recruitmentadmin.repository.ApplicantBlackListRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantInfoRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantPositionProcessRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantPositionProcessResultRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantPositionRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantSourceDetailRepository;
import com.macauslot.recruitmentadmin.repository.BaseRepository;
import com.macauslot.recruitmentadmin.repository.CertificationRepository;
import com.macauslot.recruitmentadmin.repository.DepartmentPositionDetailRepository;
import com.macauslot.recruitmentadmin.repository.DepartmentPositionRepository;
import com.macauslot.recruitmentadmin.repository.EducationRepository;
import com.macauslot.recruitmentadmin.repository.LanguageRepository;
import com.macauslot.recruitmentadmin.repository.MeetingRoomRepository;
import com.macauslot.recruitmentadmin.repository.MessageRepository;
import com.macauslot.recruitmentadmin.repository.OnShiftRepository;
import com.macauslot.recruitmentadmin.repository.OrganizationRepository;
import com.macauslot.recruitmentadmin.repository.OtherSkillRepository;
import com.macauslot.recruitmentadmin.repository.RelativeInfoRepository;
import com.macauslot.recruitmentadmin.repository.WorkingExperienceRepository;
import com.macauslot.recruitmentadmin.service.AbstractBaseService;
import com.macauslot.recruitmentadmin.service.IEmailService;
import com.macauslot.recruitmentadmin.service.IHrService;
import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import com.macauslot.recruitmentadmin.util.EncryptUtil;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.DeptPositionDescVO;
import com.macauslot.recruitmentadmin.vo.MessageVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentSourceVO;

@Service
public class HrServiceImpl extends AbstractBaseService implements IHrService {


    @Value("${sms.service.url}")
    private String smsServiceUrl;
    @Value("${sms.user}")
    private String smsUser;
    @Value("${sms.password}")
    private String smsPassword;
    @Value("${mail.receiver.test}")
    private String mailReceiverTest;
    @Value("${mail.from}")
    private String mailFrom;


    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    private final ApplicantInfoRepository applicantInfoRepository;

    private final RelativeInfoRepository relativeInfoRepository;

    private final ApplicantPositionRepository applicantPositionRepository;

    private final EducationRepository educationRepository;

    private final CertificationRepository certificationRepository;

    private final WorkingExperienceRepository workingExperienceRepository;

    private final LanguageRepository languageRepository;

    private final OtherSkillRepository otherSkillRepository;

    private final OrganizationRepository organizationRepository;

    private final OnShiftRepository onShiftRepository;

    private final DepartmentPositionDetailRepository departmentPositionDetailRepository;

    private final DepartmentPositionRepository departmentPositionRepository;
    private final MessageRepository messageRepository;
    private final ApplicantSourceDetailRepository applicantSourceDetailRepository;
    private final ApplicantPositionProcessResultRepository applicantPositionProcessResultRepository;
    private final ApplicantPositionProcessRepository applicantPositionProcessRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final ApplicantBlackListRepository applicantBlackListRepository;
    private final IEmailService emailService;

    @Autowired
    public HrServiceImpl(
            JavaMailSender mailSender,
            FreeMarkerConfigurer freeMarkerConfigurer,
            ApplicantInfoRepository applicantInfoRepository,
            RelativeInfoRepository relativeInfoRepository,
            ApplicantPositionRepository applicantPositionRepository,
            EducationRepository educationRepository,
            CertificationRepository certificationRepository,
            WorkingExperienceRepository workingExperienceRepository,
            LanguageRepository languageRepository,
            OtherSkillRepository otherSkillRepository,
            OrganizationRepository organizationRepository,
            OnShiftRepository onShiftRepository,
            DepartmentPositionDetailRepository departmentPositionDetailRepository,
            DepartmentPositionRepository departmentPositionRepository,
            MessageRepository messageRepository,
            ApplicantSourceDetailRepository applicantSourceDetailRepository,
            ApplicantPositionProcessResultRepository applicantPositionProcessResultRepository,
            ApplicantPositionProcessRepository applicantPositionProcessRepository,
            MeetingRoomRepository meetingRoomRepository,
            ApplicantBlackListRepository applicantBlackListRepository, IEmailService emailService) {
        this.mailSender = mailSender;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.applicantInfoRepository = applicantInfoRepository;
        this.relativeInfoRepository = relativeInfoRepository;
        this.applicantPositionRepository = applicantPositionRepository;
        this.educationRepository = educationRepository;
        this.certificationRepository = certificationRepository;
        this.workingExperienceRepository = workingExperienceRepository;
        this.languageRepository = languageRepository;
        this.otherSkillRepository = otherSkillRepository;
        this.organizationRepository = organizationRepository;
        this.onShiftRepository = onShiftRepository;
        this.departmentPositionDetailRepository = departmentPositionDetailRepository;
        this.departmentPositionRepository = departmentPositionRepository;
        this.messageRepository = messageRepository;
        this.applicantSourceDetailRepository = applicantSourceDetailRepository;
        this.applicantPositionProcessResultRepository = applicantPositionProcessResultRepository;
        this.applicantPositionProcessRepository = applicantPositionProcessRepository;
        this.meetingRoomRepository = meetingRoomRepository;
        this.applicantBlackListRepository = applicantBlackListRepository;
        this.emailService = emailService;
        Map<Class<? extends BaseEntity>, BaseRepository> tmpMap = new HashMap<>();
        tmpMap.put(ApplicantInfo.class, this.applicantInfoRepository);
        tmpMap.put(RelativeInfo.class, this.relativeInfoRepository);
        tmpMap.put(Education.class, this.educationRepository);
        tmpMap.put(Certification.class, this.certificationRepository);
        tmpMap.put(WorkingExperience.class, this.workingExperienceRepository);
        tmpMap.put(Language.class, this.languageRepository);
        tmpMap.put(OtherSkill.class, this.otherSkillRepository);
        tmpMap.put(OnShift.class, this.onShiftRepository);

        tmpMap.put(DepartmentPositionDetail.class, this.departmentPositionDetailRepository);
        tmpMap.put(DepartmentPosition.class, this.departmentPositionRepository);
        tmpMap.put(ApplicantSourceDetail.class, this.applicantSourceDetailRepository);
        tmpMap.put(ApplicantPosition.class, this.applicantPositionRepository);
        tmpMap.put(ApplicantPositionProcessResult.class, this.applicantPositionProcessResultRepository);
        tmpMap.put(ApplicantBlackList.class, this.applicantBlackListRepository);
        CRUD_DB_MAP = Collections.unmodifiableMap(tmpMap);

    }

    @Override
    public List<ApplicantPosition> getAllByApplicantInfoIdAndProcessStage(Integer applicantInfoId) {
        return applicantPositionRepository.findAllProcessing(applicantInfoId);
    }

    @Override
    public List<DeptPositionDescVO> getDepartmentPositionDesc(Integer deptPosDetailId) {
        return findDepartmentPositionDesc(deptPosDetailId);
    }

    @Override
    public List<ApplicantSourceDetail> getApplicantSourceDetailListByMessageId(Integer messageId) {
        return applicantSourceDetailRepository.findAllByMessageIdAndStatusOrderByOrderNum(messageId, "A");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplicantInfo newOrEditDetailPofile(
            boolean newForm,
            ApplicantInfo applicantInfo,
            RelativeInfo relativeInfo,
            List<Education> educationList,
            List<Certification> certificationList,
            List<WorkingExperience> workingExperienceList,
            List<Language> languageList,
            List<OtherSkill> otherSkillList
    ) {


        ApplicantInfo dbResult;
        if (newForm) {
            applicantInfo.setEmailStatus("N");//2021???4???27???16:09:48 ???????????????????????????????????????
            dbResult = regTop1(applicantInfo, relativeInfo);
        } else {
            dbResult = updateTop1(applicantInfo.getApplicantInfoId(), applicantInfo, relativeInfo);
        }


        Integer visitorId = dbResult.getApplicantInfoId();


        try {

            for (Education education : educationList) {
                education.setApplicantInfoId(visitorId);
                education.setToDate4sort(DateUtils.parseDate(education.getToDate(), "M-yyyy"));
            }
        } catch (ParseException parseException) {
            throw new UserSkipValidation("?????????????????????");
        }
        educationList = educationList.stream().sorted(Comparator.comparing(Education::getToDate4sort).reversed()).collect(Collectors.toList());


        for (Certification certification : certificationList) {
            certification.setApplicantInfoId(visitorId);
        }

        updateTop3(visitorId, educationList, certificationList);

        for (WorkingExperience workingExperience : workingExperienceList) {
            workingExperience.setApplicantInfoId(visitorId);
        }

        updateTop4(visitorId, workingExperienceList);

        for (Language language : languageList) {
            language.setApplicantInfoId(visitorId);
        }
        for (OtherSkill otherSkill : otherSkillList) {
            otherSkill.setApplicantInfoId(visitorId);
        }
        updateTop5(visitorId, languageList, otherSkillList);

        if (!newForm) {
            return dbResult;
        }

        //        emailService.sendTemplateMailByAsynchronousMode(newData, this::dBUpdate);//?????????ok???? ?????????! ????????????????????????
        /*save????????????????????????????????????????????????????????????????????????????????????id?????????????????????id???0
        ??????GenerateValue???????????????hibernate??????????????????????????????
        ?????????????????????????????????hibernate???????????????id????????????????????????
        ????????????????????????????????????????????????id??????????????????????????????id*/

       /* try {
            emailService.sendTemplateMailByAsynchronousMode(dbResult, sendResult -> {
                        System.err.println(Thread.currentThread().getName() + "???????????????: " + sendResult.getEmailStatus());
                        try {
                            ApplicantInfo dataSecond = findById(dbResult.getApplicantInfoId());
                            UserAuditor.setMannualUsername("");
                            dataSecond.setEmailStatus(sendResult.getEmailStatus());
                            dbUpdate(dataSecond);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException("???????????????");
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("---- emailService.sendTemplateMailByAsynchronousMode error ----");
        }*/

        return dbResult;

    }

    @Override
    public List<MessageVO> getMsgDescSource(String applicationOrg) {
        String description;
        if ("FLT".equalsIgnoreCase(applicationOrg)) {
            description = "????????????????????????";
        } else if ("SLT".equalsIgnoreCase(applicationOrg)) {
            description = "??????????????????";
        } else {
            throw new IllegalArgumentException("getMsgDescSource() applicationOrg is null");
        }
        return findMsgDesc(description);
    }

    @Override
    public List<RecruitmentSourceVO> getRecruitmentSourceList() {
        return findMsgDescList(Arrays.asList("????????????????????????", "??????????????????"));
    }


    private ApplicantInfo regTop1(ApplicantInfo applicantInfo, RelativeInfo relativeInfo) throws DuplicateKeyException, IllegalArgumentException {


        checkDuplicate(applicantInfo);

        String newPassword = getNewPassword();
        String md5password = EncryptUtil.getMd5password(newPassword);
        applicantInfo.setPassword(md5password);
        ApplicantInfo newData = insertApplicantInfo4RollBack(applicantInfo, relativeInfo);


        ApplicantInfo returnData = new ApplicantInfo();
        BeanCopyUtil.beanCopyExceptNull(newData, returnData);

        returnData.setPassword(newPassword);
        return returnData;
    }

    private String getNewPassword() {
        int i = 1234567890;
        String s = "abcdefghijklmnopqrstuvwxyz";
        String S = s.toLowerCase();
        String word = s + S + i;
        char[] c = word.toCharArray();
        int charArrayLen = c.length;
        Random rd = new Random();//??????????????????????????????????????????
        StringBuilder newPasswordsb = new StringBuilder();
        for (int k = 0; k < 6; k++) {//??????6???
            int index = rd.nextInt(charArrayLen);
            newPasswordsb.append(c[index]);//????????????????????????
        }
        return newPasswordsb.toString();
    }


    /*
    *???:
    *   ??????mslot??????           	?????????????????????(????????????)
        ??????flt??????             	?????????????????????(????????????)
        HR??????	                ?????????????????????(????????????)
        mslot??????????????????	        ?????????????????????6??? ????????? ????????????
        flt??????????????????	        ???????????????????????????4????????????4?????????????????? ????????????
        HR??????????????????	        ???????????????????????????(??????)
        HR??????????????????	        ???????????????????????????(??????)
    *
    *
    * ???:2021???4???27???14:46:10
    *
    *   ??????mslot??????           	???????????????6??? ?????????(????????????)???
        ??????flt??????             	???????????????6??? ?????????(????????????)???
        HR??????	                ???????????????6??? ?????????(??????)???
        mslot??????????????????	        ?????????????????????6??? ????????? ???????????????
        flt??????????????????	        ?????????????????????6??? ????????? ???????????????
        HR??????????????????	        ??????????????????
        HR??????????????????	        ??????????????????
    *
    *
    *
    *
    *
    * */
    private ApplicantInfo updateTop1(Integer visitorId, ApplicantInfo applicantInfo, RelativeInfo relativeInfo) {
        ApplicantInfo applicantInfoAfterUpdate;
        try {
            ApplicantInfo data = findById(visitorId);
            if (data == null) {
                throw new DataNotFoundException();
            }

            //?????????????????????????????????md5?????? 2021???4???27???16:33:15 ???????????????
//            String newPwd = EncryptUtil.getMd5password(applicantInfo.getIdCardNumber());
//            applicantInfo.setPassword(newPwd);

            applicantInfoAfterUpdate = dbUpdate(data, applicantInfo);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("???????????????");
        }
        RelativeInfo data2 = findByApplicantInfoId(visitorId);
        if (relativeInfo != null) {
            if (data2 != null) {
                dbUpdate(data2, relativeInfo);
            } else {
                relativeInfo.setApplicantInfoId(visitorId);
                dbInsert(relativeInfo);
            }
        } else {
            if (data2 != null) {
                dbDelete(data2);
            }
        }
        return applicantInfoAfterUpdate;
    }

    private void updateTop3(Integer visitorId, List<Education> educationList, List<Certification> certificationList) {

        List<Education> data = findEducationByApplicantInfoIdOrderByApplicantInfoId(visitorId);
        List<Certification> data2 = findCertificationByApplicantInfoIdOrderByApplicantInfoId(visitorId);

        if (data.isEmpty() && educationList.isEmpty()) {
        } else {
            dbAutohandle(data, educationList);
        }
        if (data2.isEmpty() && certificationList.isEmpty()) {
        } else {
            dbAutohandle(data2, certificationList);
        }

    /*    ApplicantInfo data0 = findById(visitorId);
        //????????????4???-----??? ???4????????????.
        if (data0.getIncompletePageNo() < 4) {
            data0.setIncompletePageNo(4);
            SessionUtil.setSessionAttribute("incompletePageNo", 4);
//            save(data0);
            dBUpdate(data0);
        }*/


    }

    private void updateTop4(Integer visitorId, List<WorkingExperience> workingExperienceList) {
        List<WorkingExperience> data = findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(visitorId);
        if (data.isEmpty() && workingExperienceList.isEmpty()) {
        } else {
            dbAutohandle(data, workingExperienceList);
        }
    }


    private void updateTop5(Integer visitorId, List<Language> languageList, List<OtherSkill> otherSkillList) {
        List<Language> data = findLanguageByApplicantInfoIdOrderByApplicantInfoId(visitorId);

        List<OtherSkill> data2 = findOtherSkillByApplicantInfoIdOrderByName(visitorId);

        dbAutohandle(data, languageList);
        dbAutohandle(data2, otherSkillList);

//        ApplicantInfo data0 = findById(visitorId);
/*        //????????????6???-----??? ???6????????????.
        if (data0.getIncompletePageNo() < 6) {
            data0.setIncompletePageNo(6);
            SessionUtil.setSessionAttribute("incompletePageNo", 6);
//            save(data0);
            dBUpdate(data0);

        }*/
/*        //????????????7???-----??? ??????????????????.
        if (data0.getIncompletePageNo() < 7) {
            data0.setIncompletePageNo(7);
            data0.setStatus("Y");
            SessionUtil.setSessionAttribute("incompletePageNo", 7);
//            save(data0);
            dBUpdate(data0);

        }*/
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplicantPosition updateTop2(Integer visitorId, ApplicantPosition newData) {
        int ingCount = getAllByApplicantInfoIdAndProcessStage(visitorId).size();
        if (ingCount >= 3) {
            throw new IllegalArgumentException("????????????,??????????????????????????????(" + ingCount + ")");
        }
        return dbInsert(newData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void updateTop6(Integer visitorId, Integer applicantPositionId, List<OnShift> onShiftList) {
        List<OnShift> data = onShiftRepository.findOnShiftByApplicantInfoIdAndApplicantPositionId(visitorId, applicantPositionId);
        dbAutohandle(data, onShiftList);
    }


    private ApplicantInfo findById(Integer id) {
        Optional<ApplicantInfo> applicantInfo = applicantInfoRepository.findById(id);
        return applicantInfo.orElseThrow(DataNotFoundException::new);
    }

    private RelativeInfo findByApplicantInfoId(Integer applicantInfoId) {
        try {
            return relativeInfoRepository.findByApplicantInfoId(applicantInfoId);
        } catch (Exception e) {
            throw new IllegalArgumentException("??????????????????");
        }
    }

    private void checkDuplicate(ApplicantInfo applicantInfo) {
        ApplicantInfo data = findApplicantInfoByUserNameAndApplicationOrg(applicantInfo);
        ApplicantInfo data2 = findApplicantInfoByIdCardNumberAndApplicationOrg(applicantInfo);

        if (data != null && data2 != null) {
            throw new DuplicateKeyException("????????????????????????????????????" + applicantInfo.getUserName() + ", ????????????" + applicantInfo.getIdCardNumber() + "????????????");
        } else if (data != null) {
            throw new DuplicateKeyException("????????????????????????????????????" + applicantInfo.getUserName() + "????????????");
        } else if (data2 != null) {
            throw new DuplicateKeyException("??????????????????????????????????????????" + applicantInfo.getIdCardNumber() + "????????????");
        }
    }

    private ApplicantInfo insertApplicantInfo4RollBack(ApplicantInfo applicantInfo, RelativeInfo relativeInfo) {
        applicantInfo.setIncompletePageNo(1); //??????????????????
        applicantInfo.setStatus("H");//?????????HR??????

        ApplicantInfo newData;
        try {
//            newData = save(applicantInfo);
            newData = dbInsert(applicantInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("???????????????");
        }
        if (relativeInfo != null) {
            relativeInfo.setApplicantInfoId(newData.getApplicantInfoId());
//            saveRelatibeInfo(relativeInfo);
            dbInsert(relativeInfo);
        }
        return newData;
    }

    private ApplicantInfo findApplicantInfoByUserNameAndApplicationOrg(ApplicantInfo applicantInfo) {
        try {
            return applicantInfoRepository.findApplicantInfoByUserNameAndApplicationOrg(applicantInfo.getUserName(), applicantInfo.getApplicationOrg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DuplicateKeyException("??????????????????");
        }
    }

    private ApplicantInfo findApplicantInfoByIdCardNumberAndApplicationOrg(ApplicantInfo applicantInfo) {
        try {
            return applicantInfoRepository.findApplicantInfoByIdCardNumberAndApplicationOrg(applicantInfo.getIdCardNumber(), applicantInfo.getApplicationOrg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DuplicateKeyException("??????????????????");
        }
    }

    private List<Education> findEducationByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return educationRepository.findEducationByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }

    private List<Certification> findCertificationByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return certificationRepository.findCertificationByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }

    private List<WorkingExperience> findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return workingExperienceRepository.findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }

    private List<Language> findLanguageByApplicantInfoIdOrderByApplicantInfoId(Integer visitorId) {
        return languageRepository.findLanguageByApplicantInfoIdOrderByApplicantInfoId(visitorId);
    }

    private List<OtherSkill> findOtherSkillByApplicantInfoIdOrderByName(Integer visitorId) {
        return otherSkillRepository.findOtherSkillByApplicantInfoIdOrderBySkillType(visitorId);
    }

    private List<DeptPositionDescVO> findDepartmentPositionDesc(Integer deptPosDetailId) {
        List<DeptPositionDescVO> data = EntityUtils.castEntity(departmentPositionRepository.findDepartmentPositionDescription(deptPosDetailId), DeptPositionDescVO.class);
        if (data.size() != 1) {
            throw new DataNotFoundException("????????????");
        }
        return data;
    }


    private List<MessageVO> findMsgDesc(String description) {
        return EntityUtils.castEntity(messageRepository.findMsgDesc(description), MessageVO.class);
    }

    private List<RecruitmentSourceVO> findMsgDescList(List<String> descriptionList) {
        return EntityUtils.mapping(messageRepository.findMsgDescList(descriptionList), RecruitmentSourceVO.class, "findMsgDescList");
    }

}
