package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import com.macauslot.recruitmentadmin.vo.DeptVO;

public interface RecruitmentDeptMapRepositoryCustom {

    List<DeptVO> findAllRecruitmentDepts();
    List<DeptVO> findDepartmentByOrgId(Integer orgId);
}
