package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.WorkingExperience;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkingExperienceRepository extends BaseRepository<WorkingExperience, Integer> {

    List<WorkingExperience> findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(Integer applicantInfoId);


}
