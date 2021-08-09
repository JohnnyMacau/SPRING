package com.macauslot.recruitment_ms.repository;


import com.macauslot.recruitment_ms.entity.ApplicantPosition;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApplicantPositionRepository extends BaseRepository<ApplicantPosition, Integer> {

    /**
     * 前端要的关联查询的结果
     * @param applicantInfoId
     * @return
     */
    //original 空格多位置:  and department_position.status='A'  AND department_position_detail. STATUS = 'A'
    @Query(value = "SELECT applicant_position.dept_pos_detail_id, department_position.department_code, recruitment_dept.description, department_position.job_title ,job_priority,job_code,need_shift FROM applicant_position, department_position_detail, department_position, recruitment_dept, recruitment_dept_map, organization WHERE applicant_position.applicant_info_id = :applicantInfoId  and department_position.status='A' AND department_position_detail. STATUS = 'A'  AND department_position_detail.dept_pos_id = department_position.dept_pos_id AND applicant_position.dept_pos_detail_id = department_position_detail.dept_pos_detail_id AND department_position.department_code = recruitment_dept.code and recruitment_dept.code = recruitment_dept_map.recruitment_dept and recruitment_dept_map.org_id = organization.org_id and organization.code = 'SLT' order by job_priority", nativeQuery = true)
    List<Object[]> findHistoryByApplicantInfoId(
//            @Param("jobPriority") Integer jobPriority,
            @Param("applicantInfoId") Integer applicantInfoId
    );


    /**
     * 由于该表没有主键,代替单表crud之用
     * @param applicantInfoId
     * @return
     */
    @Query(value = "select applicant_info_id,dept_pos_detail_id,job_priority,export_date,cr_date from applicant_position  where applicant_info_id=:applicantInfoId  order by job_priority", nativeQuery = true)

    List<Object[]> findByApplicantInfoIdAndJobPriority(
            @Param("applicantInfoId")Integer applicantInfoId
//            ,
//            @Param("jobPriority")  Integer jobPriority
    );
//    int countAllByApplicantInfoIdAndProcessStage (Integer applicantInfoId,String processStage);

    @Query(value = "from ApplicantPosition ap where ap.applicantInfoId =:applicantInfoId and ap.processStage not in (11,12) and ap.processStage is not null", nativeQuery = false)
    List<ApplicantPosition> findAllProcessing (@Param("applicantInfoId")Integer applicantInfoId);

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
     * GetJobApplyHistory
     *
     */
    @Query(value =
//"select app.cr_date, app.job_priority, deptpos.job_title, detail.job_code from department_position deptpos, department_position_detail detail, applicant_position app where deptpos.dept_pos_id = detail.dept_pos_id and detail.dept_pos_detail_id = app.dept_pos_detail_id and app.applicant_info_id = :applicantId"
"select app.cr_date, app.process_stage, deptpos.job_title, detail.job_code from department_position deptpos, department_position_detail detail, applicant_position app where deptpos.dept_pos_id = detail.dept_pos_id and detail.dept_pos_detail_id = app.dept_pos_detail_id and app.process_stage is not null and app.applicant_info_id = :applicantId and ( (app.process_stage in (11, 12) and date(app.cr_date)>= DATE_SUB(CURDATE(), interval 1 month)) or app.process_stage not in (11, 12) ) "
            , nativeQuery = true)
    List<Object[]> findJobApplyHistory(
            @Param("applicantId") Integer applicantId
    );

}
