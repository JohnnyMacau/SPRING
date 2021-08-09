package com.macauslot.recruitmentadmin.repository;


import com.macauslot.recruitmentadmin.entity.RecruitmentGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentGroupRepository extends BaseRepository<RecruitmentGroup, Integer>, RecruitmentGroupRepositoryCustom {

    @Query(value = "select distinct (rg.name) from recruitment_group rg", nativeQuery = true)
    List<String> findGroupNameList();


    @Query(value = "select f.id, department_code, name, max(case when role = 'department_head' then member end) as department_head, max(case when role = 'interviewer' then member end) as interviewer, max(case when role = 'administrative_personnel' then member end) as administrative_personnel from ( select rg.department_code , rg.name , group_concat(rgm.employee_id) as member, role, rg.id from recruitment_group_member rgm , recruitment_group rg , user u1 where rgm.recruitment_group_id = rg.id and rgm.employee_id = u1.USER_NAME and rg.id = :groupId group by rg.id, role)f group by f.id", nativeQuery = true)
    List<Object[]> findGroupById(Integer groupId);


    List<RecruitmentGroup> findAllByDepartmentCode(String departmentCode);
}
