package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.Education;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends BaseRepository<Education, Integer> {

//    List<ApplicantInfo> findApplicantInfoByApplicantInfoId(Integer id);

    List<Education>  findEducationByApplicantInfoIdOrderByApplicantInfoId(Integer applicantInfoId);


//    $sql="select incomplete_page_no from applicant_info where applicant_info_id=".$_SESSION["id"]; 简单命名规则



//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query()
//    Integer updateEmailStatus(String emailStatus);


}
