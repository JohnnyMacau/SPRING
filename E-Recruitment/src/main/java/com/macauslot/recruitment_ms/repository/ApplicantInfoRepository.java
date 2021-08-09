package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.ApplicantInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantInfoRepository extends BaseRepository<ApplicantInfo, Integer> {

//    List<ApplicantInfo> findApplicantInfoByApplicantInfoId(Integer id);

    ApplicantInfo findApplicantInfoByIdCardNumberAndApplicationOrg(String idCardNumber, String applicationOrg);

    ApplicantInfo findApplicantInfoByIdCardNumberAndApplicationOrgAndIdTypeId(String idCardNumber, String applicationOrg, Integer idTypeId);


    /**
     * @param userName 邮箱地址为用户名
     * @return 数据
     */
//    List<ApplicantInfo> findApplicantInfoByEmailAddress(String emailAddress);

    ApplicantInfo findApplicantInfoByUserNameAndApplicationOrg(String userName, String applicationOrg);


//    $sql="select incomplete_page_no from applicant_info where applicant_info_id=".$_SESSION["id"]; 简单命名规则


//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query()
//    Integer updateEmailStatus(String emailStatus);


}
