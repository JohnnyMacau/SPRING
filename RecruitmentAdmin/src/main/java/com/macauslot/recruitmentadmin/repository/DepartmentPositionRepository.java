package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.DepartmentPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentPositionRepository extends BaseRepository<DepartmentPosition, Integer> {
     List<DepartmentPosition> findAllByDepartmentCodeAndOrgIdAndStatus(String departmentCode, int orgId, String status);

     @Query(value = "select distinct(dp.job_title) from department_position dp order by dp.job_title ", nativeQuery = true)
     List<String>  findAllDepartmentPositionJobTitle();

     @Query(value = "SELECT department_code, job_desc, job_code, job_title, need_shift FROM department_position, department_position_detail WHERE department_position.dept_pos_id = department_position_detail.dept_pos_id AND department_position_detail. STATUS = 'A' AND department_position_detail.dept_pos_detail_id = :deptPosDetailId ", nativeQuery = true)
     List<Object[]> findDepartmentPositionDescription(@Param("deptPosDetailId")  Integer deptPosDetailId);
}
