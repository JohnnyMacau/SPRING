package com.macauslot.recruitmentadmin.service;

import com.macauslot.recruitmentadmin.entity.*;
import com.macauslot.recruitmentadmin.vo.DeptPositionDescVO;
import com.macauslot.recruitmentadmin.vo.MessageVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentSourceVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: jim.deng
 * @Date: 2021/1/18 17:30
 */
public interface IHrService {


    List<ApplicantPosition> getAllByApplicantInfoIdAndProcessStage(Integer applicantInfoId);

    List<DeptPositionDescVO> getDepartmentPositionDesc(Integer deptPosDetailId);

    List<ApplicantSourceDetail> getApplicantSourceDetailListByMessageId(Integer messageId);

    @Transactional(rollbackFor = Exception.class)
    ApplicantInfo newOrEditDetailPofile(
            boolean newForm,
            ApplicantInfo applicantInfo,
            RelativeInfo relativeInfo,
            List<Education> educationList,
            List<Certification> certificationList,
            List<WorkingExperience> workingExperienceList,
            List<Language> languageList,
            List<OtherSkill> otherSkillList
    );

    List<MessageVO> getMsgDescSource(String applicationOrg);


    List<RecruitmentSourceVO> getRecruitmentSourceList();

    @Transactional(rollbackFor = Exception.class)
    ApplicantPosition updateTop2(Integer visitorId, ApplicantPosition newData);

    @Transactional(rollbackFor = Exception.class)
    void updateTop6(Integer visitorId, Integer applicantPositionId, List<OnShift> onShiftList);
}
