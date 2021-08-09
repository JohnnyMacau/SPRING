package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.macauslot.recruitmentadmin.entity.ApplicantInfo;

@Repository
public interface ApplicantInfoRepository extends BaseRepository<ApplicantInfo, Integer>, ApplicantInfoRepositoryCustom {


    ApplicantInfo findApplicantInfoByIdCardNumberAndApplicationOrg(String idCardNumber, String applicationOrg);

    /**
     * @param userName 邮箱地址为用户名
     * @return 数据
     */


    ApplicantInfo findApplicantInfoByUserNameAndApplicationOrg(String userName, String applicationOrg);


    /**
     * GetApplicantPersonalInfo
     *
     * @param applicantId applicantInfoId
     * @return ApplicantInfoVO
     */
    @Query(value =
//            "select info.applicant_info_id, info.en_l_name, info.en_f_name, info.cn_l_name, info.cn_f_name, msg.chn_desc id_type, msg.id type_id, info.id_card_number, info.gender, info.dob, info.martial_status, info.mobile, info.area_code, info.email_address, info.source_ref, info.social, info.application_org, info.country, info.district, info.account, info.contact_name, info.contact_phone, info.contact_phone_area_code, info.contact_relation, rel.relationship, rel.in_service, rel.name relative_name, d.name department, info.notice_day, info.notice_day_type, info.available_work_date, info.expected_salary, info.expected_salary_type, info.nationality, info.address_1, info.address_2, info.address_3, " +
//                    "eb_dept,eb_posi,eb_user_name,cr_cause,tb_cause, " +
//                    "case employed_before when 'Y' then '是' else '否' end employed_before, " +
//                    "case terminated_before when 'Y' then '是' else '否' end terminated_before, " +
//                    "case criminal_record when 'Y' then '是' else '否' end criminal_record " +
//                    "from applicant_info info inner join message msg on info.id_type_id = msg.id left join relative_info rel on info.applicant_info_id = rel.applicant_info_id left join department d on d.department_id = rel.department_id where info.applicant_info_id = :applicantId"

            //親屬任職之部門選擇 不用清單選項，改文字填寫 
            "select info.applicant_info_id, info.status, info.en_l_name, info.en_f_name, info.cn_l_name, info.cn_f_name, msg.chn_desc id_type, msg.id type_id, info.id_card_number, info.area, info.gender, info.dob, info.martial_status, info.mobile, info.area_code, info.email_address, info.source_ref, info.social, info.application_org, info.country, info.district, info.account, info.contact_name, info.contact_phone, info.contact_phone_area_code, info.contact_relation, rel.relationship, rel.in_service, rel.name relative_name, rel.department_name department, info.notice_day, info.notice_day_type, info.available_work_date, info.expected_salary, info.expected_salary_type, info.nationality, info.address_1, info.address_2, info.address_3, eb_dept, eb_posi, eb_user_name, cr_cause, tb_cause, case employed_before when 'Y' then '是' else '否' end employed_before, case applied_baccount_before when 'Y' then '是' else '否' end applied_baccount_before, case terminated_before when 'Y' then '是' else '否' end terminated_before, case criminal_record when 'Y' then '是' else '否' end criminal_record from applicant_info info inner join message msg on info.id_type_id = msg.id left join relative_info rel on info.applicant_info_id = rel.applicant_info_id where info.applicant_info_id = :applicantId "

            , nativeQuery = true)
    List<Object[]> findApplicantPersonalInfoByApplicantId(
            @Param("applicantId") Integer applicantId
    );


    int countByEmailAddressAndApplicationOrgAndApplicantInfoIdNot(String emailAddress, String applicationOrg, Integer applicantId);

    int countByIdCardNumberAndApplicationOrgAndApplicantInfoIdNot(String idCardNumber, String applicationOrg, Integer applicantId);


   /* @Query(value =
            "select sum(case month(ai.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ai.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ai.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ai.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ai.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ai.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ai.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ai.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ai.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ai.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ai.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ai.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai where ai.application_org = 'MSLOT' and ai.cr_date between date_sub(now(), interval 7 month) and now();"
            , nativeQuery = true)
    List<Object[]> findRegisterData4Main_mslot();

    @Query(value =
            "select sum(case month(ai.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ai.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ai.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ai.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ai.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ai.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ai.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ai.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ai.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ai.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ai.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ai.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai, applicant_position ap where ai.applicant_info_id = ap.applicant_info_id and ai.application_org = 'MSLOT' and ai.cr_date between date_sub(now(), interval 7 month) and now();"
            , nativeQuery = true)
    List<Object[]> findApplicantData4Main_mslot();

    @Query(value =
            "select sum(case month(ai.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ai.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ai.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ai.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ai.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ai.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ai.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ai.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ai.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ai.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ai.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ai.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai where ai.application_org = 'FLT' and ai.cr_date between date_sub(now(), interval 7 month) and now();"
            , nativeQuery = true)
    List<Object[]> findRegisterData4Main_flt();

    @Query(value =
            "select sum(case month(ai.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ai.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ai.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ai.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ai.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ai.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ai.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ai.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ai.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ai.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ai.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ai.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai, applicant_position ap where ai.applicant_info_id = ap.applicant_info_id and ai.application_org = 'FLT' and ai.cr_date between date_sub(now(), interval 7 month) and now();"
            , nativeQuery = true)
    List<Object[]> findApplicantData4Main_flt();*/


    @Query(value =
            "select sum(case month(ai.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ai.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ai.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ai.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ai.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ai.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ai.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ai.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ai.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ai.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ai.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ai.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai where ai.application_org = 'SLT' and ai.cr_date between date_sub(now(), interval 7 month) and now() union all select sum(case month(ap.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ap.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ap.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ap.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ap.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ap.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ap.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ap.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ap.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ap.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ap.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ap.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai, applicant_position ap where ai.applicant_info_id = ap.applicant_info_id and ai.application_org = 'SLT' and ap.cr_date between date_sub(now(), interval 7 month) and now() union all select sum(case month(ai.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ai.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ai.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ai.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ai.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ai.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ai.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ai.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ai.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ai.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ai.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ai.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai where ai.application_org = 'FLT' and ai.cr_date between date_sub(now(), interval 7 month) and now() union all select sum(case month(ap.cr_date) when '1' then 1 else 0 end) as Jan, sum(case month(ap.cr_date) when '2' then 1 else 0 end) as Feb, sum(case month(ap.cr_date) when '3' then 1 else 0 end) as Mar, sum(case month(ap.cr_date) when '4' then 1 else 0 end) as Apr, sum(case month(ap.cr_date) when '5' then 1 else 0 end) as May, sum(case month(ap.cr_date) when '6' then 1 else 0 end) as June, sum(case month(ap.cr_date) when '7' then 1 else 0 end) as July, sum(case month(ap.cr_date) when '8' then 1 else 0 end) as Aug, sum(case month(ap.cr_date) when '9' then 1 else 0 end) as Sept, sum(case month(ap.cr_date) when '10' then 1 else 0 end) as oct, sum(case month(ap.cr_date) when '11' then 1 else 0 end) as Nov, sum(case month(ap.cr_date) when '12' then 1 else 0 end) as Dece from applicant_info ai, applicant_position ap where ai.applicant_info_id = ap.applicant_info_id and ai.application_org = 'FLT' and ap.cr_date between date_sub(now(), interval 7 month) and now();"
            , nativeQuery = true)
    List<Object[]> findApplicantAndRegisterData4Main();
}
