package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.OtherSkill;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtherSkillRepository extends BaseRepository<OtherSkill, Integer> {

    List<OtherSkill> findOtherSkillByApplicantInfoIdOrderByApplicantInfoId(Integer applicantInfoId);


}
