package com.macauslot.recruitment_ms.vo;


import com.macauslot.recruitment_ms.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class VisitorSessionDataVO {
    ApplicantInfo applicantInfo;
    RelativeInfo relativeInfo;
    List<Education> educationList;
    List<Certification> certificationList;
    List<WorkingExperience> workingExperienceList;
    List<Language> languageList;
    List<OtherSkill> otherSkillList;
}
