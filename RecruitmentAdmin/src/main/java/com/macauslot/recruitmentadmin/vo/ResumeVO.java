package com.macauslot.recruitmentadmin.vo;

import com.macauslot.recruitmentadmin.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResumeVO {
    List<ApplyDataVO> applyDataVOList;//申请相关职位的资料
//    List<OnShift> onShiftList;
    ApplicantInfoVO applicantInfoVO;
    List<Message> messageList;
    List<Education> educationList;
    List<Certification> certificationList;
    List<WorkingExperience> experienceList;
    List<Language> languageList;
    List<OtherSkill> otherSkillList;
}
