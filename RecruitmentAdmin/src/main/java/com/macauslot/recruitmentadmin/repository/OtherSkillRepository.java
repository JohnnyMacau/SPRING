package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.OtherSkill;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtherSkillRepository extends BaseRepository<OtherSkill, Integer> {

    List<OtherSkill> findOtherSkillByApplicantInfoIdOrderBySkillType(Integer applicantInfoId);


}
