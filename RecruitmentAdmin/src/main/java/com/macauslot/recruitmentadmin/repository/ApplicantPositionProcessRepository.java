package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.macauslot.recruitmentadmin.entity.ApplicantPositionProcess;

@Repository
public interface ApplicantPositionProcessRepository extends BaseRepository<ApplicantPositionProcess, Integer> {

    @Query(value =
			"select * from ( ( select ap.applicant_pos_id, dpd.job_code, '未申請' as begin_stage, case when ap.who_generate = 'HR' then 'HR代申請' else '應征者申請' end as operation, 'HR篩選簡曆' as end_stage, ap.cr_date as operation_time, '' as remark from applicant_position ap, department_position_detail dpd where ap.dept_pos_detail_id = dpd.dept_pos_detail_id and ap.applicant_info_id = :applicantInfoId) union ( select app.applicant_position_id, dpd.job_code, m1.CHN_DESC as begin_stage, m3.CHN_DESC as operation, m2.CHN_DESC as end_stage, app.last_modify_date as operation_time, app.stage_action_remark as remark from applicant_position_process app, applicant_position ap, department_position_detail dpd, message m1, message_group mg1, message m2, message_group mg2, message m3, message_group mg3 where app.applicant_position_id = ap.applicant_pos_id and app.dept_pos_detail_id = dpd.dept_pos_detail_id and app.stage = m1.code and m1.MESSAGE_GROUP_ID = mg1.ID and mg1.DESCRIPTION = '招聘流程' and app.new_stage = m2.code and m2.MESSAGE_GROUP_ID = mg2.ID and mg2.DESCRIPTION = '招聘流程' and concat(app.stage, '-', app.stage_action) = m3.code and m3.MESSAGE_GROUP_ID = mg3.ID and mg3.DESCRIPTION = '招聘狀態-操作' and ap.applicant_info_id = :applicantInfoId) union ( select app.applicant_position_id, dpd.job_code, m1.CHN_DESC as begin_stage, case when app.stage_action = 4 then '歸檔' when app.stage_action = 5 then 'HR手動結束流程' when app.stage_action = 6 then '系統自動結束流程' else '' end as operation, m2.CHN_DESC as end_stage, app.last_modify_date as operation_time, app.stage_action_remark as remark from applicant_position_process app, applicant_position ap, department_position_detail dpd, message m1, message_group mg1, message m2, message_group mg2 where app.applicant_position_id = ap.applicant_pos_id and app.dept_pos_detail_id = dpd.dept_pos_detail_id and app.stage = m1.code and m1.MESSAGE_GROUP_ID = mg1.ID and mg1.DESCRIPTION = '招聘流程' and app.new_stage = m2.code and m2.MESSAGE_GROUP_ID = mg2.ID and mg2.DESCRIPTION = '招聘流程' and app.stage_action in (4, 5, 6) and ap.applicant_info_id = :applicantInfoId) ) t order by applicant_pos_id desc, operation_time desc "
			, nativeQuery = true)
    List<Object[]> findProcessHistoryByApplicantInfoId(
            @Param("applicantInfoId") Integer applicantInfoId
    );

    @Query(value =
			"select * from (( select app.applicant_position_id, dpd.job_code, m1.CHN_DESC as begin_stage, case when app.stage_action = 4 then '歸檔' when app.stage_action = 5 then 'HR手動結束流程' when app.stage_action = 6 then '系統自動結束流程' else '' end as operation, m2.CHN_DESC as end_stage, app.last_modify_date as operation_time, app.stage_action_remark as remark from applicant_position_process app, applicant_position ap, department_position_detail dpd, message m1, message_group mg1, message m2, message_group mg2 where app.applicant_position_id = ap.applicant_pos_id and app.dept_pos_detail_id = dpd.dept_pos_detail_id and app.stage = m1.code and m1.MESSAGE_GROUP_ID = mg1.ID and mg1.DESCRIPTION = '招聘流程' and app.new_stage = m2.code and m2.MESSAGE_GROUP_ID = mg2.ID and mg2.DESCRIPTION = '招聘流程' and app.stage_action in (4, 5, 6) and ap.applicant_pos_id = :applicant_pos_id) union ( select app.applicant_position_id, dpd.job_code, m1.CHN_DESC as begin_stage, m3.CHN_DESC as operation, m2.CHN_DESC as end_stage, app.last_modify_date as operation_time, app.stage_action_remark as remark from applicant_position_process app, applicant_position ap, department_position_detail dpd, message m1, message_group mg1, message m2, message_group mg2, message m3, message_group mg3 where app.applicant_position_id = ap.applicant_pos_id and app.dept_pos_detail_id = dpd.dept_pos_detail_id and app.stage = m1.code and m1.MESSAGE_GROUP_ID = mg1.ID and mg1.DESCRIPTION = '招聘流程' and app.new_stage = m2.code and m2.MESSAGE_GROUP_ID = mg2.ID and mg2.DESCRIPTION = '招聘流程' and concat(app.stage, '-', app.stage_action) = m3.code and m3.MESSAGE_GROUP_ID = mg3.ID and mg3.DESCRIPTION = '招聘狀態-操作' and ap.applicant_pos_id = :applicant_pos_id) union ( select ap.applicant_pos_id, dpd.job_code, '未申請' as begin_stage, case when ap.who_generate = 'HR' then 'HR代申請' else '應征者申請' end as operation, 'HR篩選簡曆' as end_stage, ap.cr_date as operation_time, '' as remark from applicant_position ap, department_position_detail dpd where ap.dept_pos_detail_id = dpd.dept_pos_detail_id and ap.applicant_pos_id = :applicant_pos_id) ) t order by operation_time desc "
			, nativeQuery = true)
    List<Object[]> findProcessHistoryByApplicantPosId(
            @Param("applicant_pos_id") Integer applicant_pos_id
    );
}
