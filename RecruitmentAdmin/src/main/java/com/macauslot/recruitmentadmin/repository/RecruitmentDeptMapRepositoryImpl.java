package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.DeptVO;

public class RecruitmentDeptMapRepositoryImpl implements RecruitmentDeptMapRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    

    @Override
    public List<DeptVO> findAllRecruitmentDepts(){
        String sql = "select rd.code, rd.description from recruitment_dept rd order by rd.code ";
        Query query = em.createNativeQuery(sql);
        List<DeptVO> list = EntityUtils.castEntity(query.getResultList(), DeptVO.class);
        return list;
    }
    
    @Override
    public List<DeptVO> findDepartmentByOrgId(Integer orgId){
        String sql = "select rd.code, rd.description from recruitment_dept_map rdm, recruitment_dept rd where rdm.RECRUITMENT_DEPT = rd.code and org_Id = :orgId group by rdm.RECRUITMENT_DEPT order by rd.code ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("orgId", orgId);
        List<DeptVO> list = EntityUtils.castEntity(query.getResultList(), DeptVO.class);
        return list;
    }

}
