package com.macauslot.recruitment_ms.service;

import com.macauslot.recruitment_ms.entity.*;
import com.macauslot.recruitment_ms.exception.*;
import com.macauslot.recruitment_ms.vo.ApplicantPositionVO;
import com.macauslot.recruitment_ms.vo.JobApplyHistoryVO;
import com.macauslot.recruitment_ms.vo.VisitorSessionDataVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IApplicantInfoService {


    @Transactional(rollbackFor = Exception.class)
    void updateTop6(Integer visitorId, Integer applicantPositionId, List<OnShift> onShiftList);

    List<WorkingExperience> getWorkingExperienceByApplicantInfoId(Integer visitorId);

    List<Education> getEducationByApplicantInfoId(Integer visitorId);

    List<Language> getLanguageByApplicantInfoId(Integer visitorId);

    List<Certification> getCertificationByApplicantInfoId(Integer visitorId);

    List<OtherSkill> getOtherSkillByApplicantInfoId(Integer visitorId);

    List<OnShift> getOnShiftByApplicantInfoId(Integer visitorId);

//    List<ApplicantPositionVO> getHistoryApplicantPositionByApplicantInfoId(Integer jobPriority, Integer visitorId);
    List<ApplicantPositionVO> getHistoryApplicantPositionByApplicantInfoId(Integer visitorId);


    ApplicantInfo login(String username, String password) throws UserNotFoundException, PasswordNotMatchException;

    void checkDuplicate(ApplicantInfo applicantInfo);

    void changePassword(Integer visitorId, String oldPassword, String newPassword);

//    void forgetPassword(String emailAddress);
    void forgetPassword(String idCardNumber, Integer idTypeId);


    ApplicantInfo getById(Integer id);



    RelativeInfo getRelativeInfoByApplicantInfoId(Integer visitorId);

    ApplicantInfo regTop1(ApplicantInfo applicantInfo, RelativeInfo relativeInfo) throws DuplicateKeyException, InsertException, EmailException;


    void updateTop1(Integer visitorId, ApplicantInfo applicantInfo, RelativeInfo relativeInfo);

//    void updateTop2(Integer visitorId, ApplicantPosition applicantPosition1, ApplicantPosition applicantPosition2, ApplicantPosition applicantPosition3, ApplicantInfo applicantInfo);

//    @Transactional(rollbackFor = Exception.class)

    @Transactional(rollbackFor = Exception.class)
    ApplicantPosition updateTop2(Integer visitorId, ApplicantPosition newData);

    void updateTop3(Integer visitorId, List<Education> educationList, List<Certification> certificationList);

    void updateTop4(Integer visitorId, List<WorkingExperience> workingExperienceList, ApplicantInfo applicantInfo);

    void updateTop5(Integer visitorId, List<Language> languageList, List<OtherSkill> otherSkillList);



    @Transactional(rollbackFor = Exception.class)
    ApplicantInfo insertVistorSessionData(VisitorSessionDataVO visitorSessionDataVO);

    boolean checkOldData(Integer visitorId);

    List<ApplicantSourceDetail> getApplicantSourceDetailListByMessageId(Integer messageId);

    List<ApplicantPosition> getAllByApplicantInfoIdAndProcessStage(Integer applicantInfoId);

    List<JobApplyHistoryVO> getJobApplyHistory(Integer applicantId);
}
