package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.macauslot.recruitmentadmin.entity.DepartmentPositionDetail;

@Repository
public interface DepartmentPositionDetailRepository extends BaseRepository<DepartmentPositionDetail, Integer>, DepartmentPositionDetailRepositoryCustom {
	@Query(value =
            "select p.jobTitle, d from DepartmentPosition as p, DepartmentPositionDetail as d where d.deptPosId = p.deptPosId and d.status='T' and d.recruitmentForm is not null"
            , nativeQuery = false)
	List<Object> getJobTemplateList();

	void deleteByDeptPosDetailId(int deptPosDetailId);

    /**
     *GetDepartmentPositionDetail
     */
    @Query(value = "select detail.bugget_type, concat(o.code, '|', o.desc) as org_name, o.org_id, rd.code, rd.description, dp.job_title, dp.dept_pos_id, detail.dept_pos_detail_id, detail.job_code, detail.status, detail.job_desc, detail.headcount, date_format(detail.start_date, '%m/%d/%Y') start_date, date_format(detail.end_date, '%m/%d/%Y') end_date, detail.cr_date, detail.created_by, detail.need_shift, detail.recruitment_group_id, detail.recruitment_form from department_position_detail detail, department_position dp, recruitment_dept rd, organization o where detail.dept_pos_id = dp.dept_pos_id and dp.department_code = rd.code and detail.dept_pos_detail_id = :deptPosDetailId and dp.org_id = o.org_id limit 1" , nativeQuery = true)
    List<Object[]>  findDepartmentPositionDetail(@Param("deptPosDetailId") Integer deptPosDetailId);


}
