package com.macauslot.recruitmentadmin.vo;

import com.macauslot.recruitmentadmin.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileDetialVO {
    ApplicantInfo applicantInfo;
    RelativeInfo relativeInfo;
    List<Education> educationList;
    List<Certification> certificationList;
    List<WorkingExperience> workingExperienceList;
    List<Language> languageList;
    List<OtherSkill> otherSkillList;
}
