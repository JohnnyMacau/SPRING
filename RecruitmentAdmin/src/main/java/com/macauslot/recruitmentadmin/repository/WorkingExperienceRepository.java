package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.WorkingExperience;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkingExperienceRepository extends BaseRepository<WorkingExperience, Integer> {

    List<WorkingExperience> findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(Integer applicantInfoId);


}
