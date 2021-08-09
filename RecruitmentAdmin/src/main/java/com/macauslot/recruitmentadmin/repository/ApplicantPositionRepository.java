package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.ApplicantPosition;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ApplicantPositionRepository extends BaseRepository<ApplicantPosition, Integer>, ApplicantPositionRepositoryCustom {
//    @Query(value = "SELECT applicant_position.dept_pos_detail_id, department_position.department_id, department.description, department_position.job_title FROM applicant_position, department_position_detail, department_position, department WHERE applicant_position.job_priority = :jobPriority AND applicant_position.applicant_info_id = :applicantInfoId AND department_position. STATUS = 'a' AND department_position_detail. STATUS = 'f' AND department_position_detail.dept_pos_id = department_position.dept_pos_id AND applicant_position.dept_pos_detail_id = department_position_detail.dept_pos_detail_id AND department_position.department_id = department.department_Id", nativeQuery = true)
//    List<Object[]> findHistoryByJobPriorityAndApplicantInfoId(
//            @Param("jobPriority") Integer jobPriority,
//            @Param("applicantInfoId") Integer applicantInfoId
//    );

    /**
     * 前端要的关联查询的结果
     *
     * @param applicantInfoId
     * @return
     */
    //original 空格多位置:  and department_position.status='A' AND department_position_detail. STATUS = 'f'
    @Query(value = "SELECT applicant_position.dept_pos_detail_id, department_position.department_code, department_position.department_code, department_position.job_title ,job_priority,job_code,need_shift FROM applicant_position, department_position_detail, department_position WHERE applicant_position.applicant_info_id = :applicantInfoId  and department_position.status='A' AND department_position_detail. STATUS = 'f'  AND department_position_detail.dept_pos_id = department_position.dept_pos_id AND applicant_position.dept_pos_detail_id = department_position_detail.dept_pos_detail_id order by job_priority", nativeQuery = true)
    List<Object[]> findHistoryByApplicantInfoId(
//            @Param("jobPriority") Integer jobPriority,
            @Param("applicantInfoId") Integer applicantInfoId
    );


    /**
     * 由于该表没有主键,代替单表crud之用
     *
     * @param applicantInfoId
     * @return
     */
    @Query(value = "select applicant_info_id,dept_pos_detail_id,job_priority,export_date,cr_date from applicant_position  where applicant_info_id=:applicantInfoId  order by job_priority", nativeQuery = true)
    List<Object[]> findByApplicantInfoIdAndJobPriority(
            @Param("applicantInfoId") Integer applicantInfoId
//            ,
//            @Param("jobPriority")  Integer jobPriority
    );

    @Modifying(clearAutomatically = true)
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "insert into applicant_position (applicant_info_id,dept_pos_detail_id,job_priority  ) values (:applicantInfoId,:deptPosDetailId,:jobPriority)", nativeQuery = true)
    void insert(
            @Param("applicantInfoId") Integer applicantInfoId,
            @Param("deptPosDetailId") Integer deptPosDetailId,
            @Param("jobPriority") Integer jobPriority
    );


    @Modifying(clearAutomatically = true)
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update applicant_position set dept_pos_detail_id=:deptPosDetailId,cr_date=now()  where applicant_info_id=:applicantInfoId and job_priority=:jobPriority", nativeQuery = true)
    void update(

            @Param("applicantInfoId") Integer applicantInfoId,
            @Param("deptPosDetailId") Integer deptPosDetailId,
            @Param("jobPriority") Integer jobPriority

    );

    @Modifying(clearAutomatically = true)
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "delete from applicant_position  where applicant_info_id=:applicantInfoId and job_priority=:jobPriority", nativeQuery = true)
    void delete(
            @Param("applicantInfoId") Integer applicantInfoId,
            @Param("jobPriority") Integer jobPriority
    );


    /**
     * GetCVById
     *
     * @param applicantInfoId applicantInfoId
     * @return ApplyDataVO
     */
    @Query(value =
"select apppos.process_stage, rd.code, deptpos.job_title, apppos.expected_salary, apppos.expected_salary_type, apppos.available_work_date, apppos.notice_day, apppos.notice_day_type, msg.chn_desc source, apppos.source_ref, apppos.cr_date , rd.description, asd.chn_desc, detail.need_shift , apppos.applicant_pos_id as applicant_position_id, ifnull(case when apppos.process_stage = 12 then '1.1' when apppos.process_stage = 11 then (select b.index from applicant_position_process a, applicant_position_process_result b where a.stage_action_remark = b.desc and a.applicant_position_id = apppos.applicant_pos_id and a.new_stage = apppos.process_stage order by a.applicant_position_process_id desc limit 1) else '' end, 'N/A') as result from applicant_position apppos inner join department_position_detail detail on apppos.dept_pos_detail_id = detail.dept_pos_detail_id inner join department_position deptpos on detail.dept_pos_id = deptpos.dept_pos_id inner join message msg on apppos.source_id = msg.id left join applicant_source_detail asd on apppos.applicant_source_detail_id = asd.id inner join recruitment_dept rd on deptpos.department_code = rd.code where apppos.process_stage is not null and apppos.applicant_info_id = :applicantInfoId order by apppos.cr_date desc "
            , nativeQuery = true)
    List<Object[]> findApplyDataListByApplicantInfoId(
            @Param("applicantInfoId") Integer applicantInfoId
    );


    @Query(value =
"select apppos.applicant_info_id , apppos.process_stage, rd.code, deptpos.job_title, apppos.expected_salary, apppos.expected_salary_type, apppos.available_work_date, apppos.notice_day, apppos.notice_day_type, msg.chn_desc source, apppos.source_ref, apppos.cr_date , rd.description, asd.chn_desc, detail.need_shift , apppos.applicant_pos_id as applicant_position_id, ifnull(case when apppos.process_stage = 12 then '1.1' when apppos.process_stage = 11 then (select b.index from applicant_position_process a, applicant_position_process_result b where a.stage_action_remark = b.desc and a.applicant_position_id = apppos.applicant_pos_id and a.new_stage = apppos.process_stage order by a.applicant_position_process_id desc limit 1) else '' end, 'N/A') as result from applicant_position apppos inner join department_position_detail detail on apppos.dept_pos_detail_id = detail.dept_pos_detail_id inner join department_position deptpos on detail.dept_pos_id = deptpos.dept_pos_id inner join message msg on apppos.source_id = msg.id left join applicant_source_detail asd on apppos.applicant_source_detail_id = asd.id inner join recruitment_dept rd on deptpos.department_code = rd.code where apppos.process_stage is not null and apppos.applicant_pos_id = :applicantPosId limit 1 "
    , nativeQuery = true)
    List<Object[]> findApplyDataListByApplicantPosId(
            @Param("applicantPosId") Integer applicantPosId
    );




    @Modifying(clearAutomatically = true)
    @Transactional(rollbackFor = Exception.class)
    @Query(value =
//            "update applicant_position set export_date=now()  where applicant_pos_id in (:applicantPosIds) and job_priority=:jobPriority"

            "update applicant_position set export_date=now()  where applicant_pos_id in (:applicantPosIds)"
            , nativeQuery = true)
    void batchUpdateExportDate(@Param("applicantPosIds") List<Integer> applicantPosIds);


    /**
     * GetJobApplyHistory
     */
    @Query(value =
//"select app.cr_date, app.job_priority, deptpos.job_title, detail.job_code from department_position deptpos, department_position_detail detail, applicant_position app where deptpos.dept_pos_id = detail.dept_pos_id and detail.dept_pos_detail_id = app.dept_pos_detail_id and app.applicant_info_id = :applicantInfoId"
            "select app.cr_date, app.process_stage, deptpos.job_title, detail.job_code from department_position deptpos, department_position_detail detail, applicant_position app where deptpos.dept_pos_id = detail.dept_pos_id and detail.dept_pos_detail_id = app.dept_pos_detail_id and app.process_stage is not null and app.applicant_info_id = :applicantInfoId"
            , nativeQuery = true)
    List<Object[]> findJobApplyHistory(@Param("applicantInfoId") Integer applicantInfoId);


    List<ApplicantPosition> findByDeptPosDetailId(Integer deptPosDetailId);

    @Query(value = "select ap from ApplicantPosition ap where ap.bookmarkedJobId = :deptPosDetailId or ap.bookmarkedJobId is null and ap.deptPosDetailId = :deptPosDetailId" , nativeQuery = false)
    List<ApplicantPosition> findApplicationByJobId(Integer deptPosDetailId);


    @Query(value = "from ApplicantPosition ap where ap.applicantInfoId =:applicantInfoId and ap.processStage not in (11,12) and ap.processStage is not null", nativeQuery = false)
    List<ApplicantPosition> findAllProcessing(@Param("applicantInfoId") Integer applicantInfoId);


    @Query(value =
"select ai.cn_l_name 'cn_last_name', ai.cn_f_name 'cn_first_name', ai.en_l_name 'en_last_name', ai.en_f_name 'en_first_name', m3.CHN_DESC as 'id_card_type_name', ai.id_card_number as 'id_card_number', ai.blacklisted as 'is_blacklisted', table_count1.count_apply_times as 'count_apply_times' from applicant_info ai inner join message m3 on (ai.id_type_id = m3.ID) left join applicant_position ap on (ap.applicant_info_id = ai.applicant_info_id) left join ( select ap.applicant_info_id , count(ap.applicant_info_id) as count_apply_times from applicant_position ap where ap.process_stage is not null group by ap.applicant_info_id) table_count1 on (ap.applicant_info_id = table_count1.applicant_info_id) where ai.id_card_number = :id_card_number and ai.application_org = :application_org group by ai.applicant_info_id "
            , nativeQuery = true)
    List<Object[]> find4CandidateApplicationReport1(@Param("id_card_number") String id_card_number,
                                                    @Param("application_org") String application_org);

    @Query(value =
    		"select ap.cr_date as 'applicant_cr_date', dp.department_code as 'applicant_dept_code', dp.job_title as 'job_title', ( select m.chn_desc from message m, message_group mg where m.message_group_id = mg.id and mg.description = '招聘流程' and m.code = ap.process_stage) as 'process_stage', table_archive.stage_action_remark as 'archived_result', app9.flow_detail from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on (app.applicant_position_process_id = table_archive.applicant_position_process_id) left join ( select app8.applicant_position_id, group_concat(m1.CHN_DESC, concat(' ', app8.last_modify_date), concat(' ', m3.CHN_DESC), concat(' ', m2.CHN_DESC) order by app8.last_modify_date asc separator '||') as 'flow_detail' from applicant_position_process app8, applicant_position ap, applicant_info ai, message m1, message_group mg1, message m2, message_group mg2, message m3, message_group mg3 where app8.applicant_position_id = ap.applicant_pos_id and ap.applicant_info_id = ai.applicant_info_id and app8.stage = m1.code and m1.MESSAGE_GROUP_ID = mg1.ID and mg1.DESCRIPTION = '招聘流程' and app8.new_stage = m2.code and m2.MESSAGE_GROUP_ID = mg2.ID and mg2.DESCRIPTION = '招聘流程' and concat(app8.stage, '-', app8.stage_action) = m3.code and m3.MESSAGE_GROUP_ID = mg3.ID and mg3.DESCRIPTION = '招聘狀態-操作' and ai.id_card_number = :id_card_number group by app8.applicant_position_id ) app9 on (ap.applicant_pos_id = app9.applicant_position_id) where ap.process_stage is not null and ai.id_card_number = :id_card_number and ai.application_org = :application_org order by ap.cr_date desc "
            , nativeQuery = true)
    List<Object[]> find4CandidateApplicationReport2(@Param("id_card_number") String id_card_number,
                                                    @Param("application_org") String application_org);

    @Query(value =
            "select recruitment_source_1 as 'title', n, round( n / s * 100, 3 ) percent from ( select * from ( select m2.CHN_DESC as 'recruitment_source_1', count(ap.applicant_pos_id) n from applicant_position ap inner join message m2 on (ap.source_id = m2.ID) where ap.process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) group by ap.source_id ) t1 inner join ( select count(1) s from applicant_position ap where process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t union select 'TOTAL' as recruitment_source_1, sum( t9.n ) , sum(round( n / s * 100, 3 )) from ( select * from ( select m2.CHN_DESC as 'recruitment_source_1', count(ap.applicant_pos_id) n from applicant_position ap inner join message m2 on (ap.source_id = m2.ID) where ap.process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) group by ap.source_id ) t1 inner join ( select count(1) s from applicant_position ap where process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t9 order by n "
            , nativeQuery = true)
    List<Object[]> find4CandidateApplicationSources(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query(value =
//            "select degree_int as 'title', n, round( n / s * 100, 3 ) percent from ( select * from (select ( case e2.degree_int when 1 then '小學畢業或以下' when 2 then '初中' when 3 then '初中畢業' when 4 then '高中' when 5 then '高中畢業' when 6 then '大專/高級文憑' when 7 then '大學/大專(中)' when 8 then '學士' when 9 then '學士以上' when 0 then '其他舊學歷' ELSE e2.degree_int END ) as 'degree_int', count(ap.applicant_pos_id) n from applicant_position ap left join (select e.applicant_info_id ,MAX(case e.`degree` when '小學畢業或以下' then 1 when '初中' then 2 when '初中畢業' then 3 when '高中' then 4 when '高中畢業' then 5 when '大專/高級文憑' then 6 when '大學/大專(中)' then 7 when '學士' then 8 when '學士以上' then 9 when '小学毕业或以下' then 1 when '初中' then 2 when '初中毕业' then 3 when '高中' then 4 when '高中毕业' then 5 when '大专/高级文凭' then 6 when '大学/大专(中)' then 7 when '学士' then 8 when '学士以上' then 9 else 0 end ) as 'degree_int' from education e group by e.applicant_info_id) e2 on ap.applicant_info_id = e2.applicant_info_id where ap.process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) group by e2.degree_int) t1 inner join ( select count(1) s from applicant_position ap where process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t union select 'TOTAL' as recruitment_source_1, sum( t9.n ) , sum(round( n / s * 100, 3 )) from ( select * from (select ( case e2.degree_int when 1 then '小學畢業或以下' when 2 then '初中' when 3 then '初中畢業' when 4 then '高中' when 5 then '高中畢業' when 6 then '大專/高級文憑' when 7 then '大學/大專(中)' when 8 then '學士' when 9 then '學士以上' when 0 then '其他舊學歷' ELSE e2.degree_int END ) as 'degree_int', count(ap.applicant_pos_id) n from applicant_position ap left join (select e.applicant_info_id ,MAX(case e.`degree` when '小學畢業或以下' then 1 when '初中' then 2 when '初中畢業' then 3 when '高中' then 4 when '高中畢業' then 5 when '大專/高級文憑' then 6 when '大學/大專(中)' then 7 when '學士' then 8 when '學士以上' then 9 when '小学毕业或以下' then 1 when '初中' then 2 when '初中毕业' then 3 when '高中' then 4 when '高中毕业' then 5 when '大专/高级文凭' then 6 when '大学/大专(中)' then 7 when '学士' then 8 when '学士以上' then 9 else 0 end ) as 'degree_int' from education e group by e.applicant_info_id) e2 on ap.applicant_info_id = e2.applicant_info_id where ap.process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) group by e2.degree_int) t1 inner join ( select count(1) s from applicant_position ap where process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t9 "
     "select `degree` as 'title', n, round( n / s * 100, 3 ) percent from ( select * from ( select e2.`degree`, count(ap.applicant_pos_id) n from applicant_position ap left join education e2 on (ap.applicant_info_id = e2.applicant_info_id and e2.education_id in ( select min(e3.education_id) from education e3 where ap.applicant_info_id = e3.applicant_info_id)) where ap.process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) group by e2.`degree`) t1 inner join ( select count(1) s from applicant_position ap where process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t union select 'TOTAL' as recruitment_source_1, sum(t9.n) , sum(round( n / s * 100, 3 )) from ( select * from ( select e2.`degree`, count(ap.applicant_pos_id) n from applicant_position ap left join education e2 on (ap.applicant_info_id = e2.applicant_info_id and e2.education_id in ( select min(e3.education_id) from education e3 where ap.applicant_info_id = e3.applicant_info_id)) where ap.process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) group by e2.`degree`) t1 inner join ( select count(1) s from applicant_position ap where process_stage is not null and date(ap.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t9 "
            , nativeQuery = true)
    List<Object[]> find4CandidateEducationalBackground(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query(value =
            "select age_range as 'title', n, round( n / s * 100, 3 ) percent from ( select * from ( select age_range, count(age_range) n from ( select age, case when (age <18 ) then '18歲以下' when (age >= 18 and age <21) then '18-20歲' when (age >= 21 and age <31) then '21-30歲' when (age >= 31 and age <41) then '31-40歲' when (age >= 41) then '41歲或以上' else '無提供年齡' end as 'age_range' from ( select TIMESTAMPDIFF(YEAR,ai.dob,CURDATE()) as 'age' from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) where ap.process_stage is not null and dpd.recruitment_form = :recruitment_form and date(ap.cr_date) between date(:startDate) and date(:endDate) )t_age)t_age_range group by age_range ) t1 inner join ( select count(1) s from applicant_position ap0 inner join department_position_detail dpd on (ap0.dept_pos_detail_id = dpd.dept_pos_detail_id) where process_stage is not null and dpd.recruitment_form = :recruitment_form and date(ap0.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t union select 'TOTAL' as recruitment_source_1, sum( t9.n ) , sum(round( n / s * 100, 3 )) from ( select * from ( select age_range, count(age_range) n from ( select age, case when (age <18 ) then '18歲以下' when (age >= 18 and age <21) then '18-20歲' when (age >= 21 and age <31) then '21-30歲' when (age >= 31 and age <41) then '31-40歲' when (age >= 41) then '41歲或以上' else '無提供年齡' end as 'age_range' from ( select TIMESTAMPDIFF(YEAR,ai.dob,CURDATE()) as 'age' from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) where ap.process_stage is not null and dpd.recruitment_form = :recruitment_form and date(ap.cr_date) between date(:startDate) and date(:endDate) )t_age)t_age_range group by age_range ) t1 inner join ( select count(1) s from applicant_position ap0 inner join department_position_detail dpd on (ap0.dept_pos_detail_id = dpd.dept_pos_detail_id) where process_stage is not null and dpd.recruitment_form = :recruitment_form and date(ap0.cr_date) between date(:startDate) and date(:endDate) ) t2 on 1 = 1 ) t9 "
            , nativeQuery = true)
    List<Object[]> find4CandidateAgeRatio(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("recruitment_form") String recruitmentForm
    );
}
