package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.ApplicantInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends BaseRepository<ApplicantInfo,Integer> {
//    @Transactional
//    @Modifying

    @Query(value = "SELECT department_position_detail.start_date, department_position_detail.end_date, department_position_detail.dept_pos_detail_id, recruitment_dept.code, recruitment_dept.description, department_position.job_title, department_position_detail.job_code,need_shift FROM department_position, department_position_detail, recruitment_dept, organization WHERE department_position.department_code = recruitment_dept.code and department_position.org_id = organization.org_id AND department_position.dept_pos_id = department_position_detail.dept_pos_id AND department_position. STATUS = 'A' AND department_position_detail. STATUS = 'A' AND (department_position_detail.end_date is null or :end_date <= department_position_detail.end_date) and organization.code = 'SLT' ORDER BY recruitment_dept.code", nativeQuery = true)
    List<Object[]> findDepartmentPositionDetail(
            @Param("end_date") String endDate
          );



    @Query(value = "SELECT department_code, job_desc, job_code, job_title, need_shift FROM department_position, department_position_detail WHERE department_position.dept_pos_id = department_position_detail.dept_pos_id AND department_position_detail. STATUS = 'A' AND department_position_detail.dept_pos_detail_id = :deptPosDetailId ", nativeQuery = true)
    List<Object[]> findDepartmentPositionDescription(@Param("deptPosDetailId")  Integer deptPosDetailId);


    @Query(value = "select rdm.recruitment_dept, rd.description from recruitment_dept_map rdm, recruitment_dept rd where rdm.recruitment_dept = rd.code and rdm.org_id = 1 group by rdm.recruitment_dept ", nativeQuery = true)
    List<Object[]> findDepartmentDescription();


}
