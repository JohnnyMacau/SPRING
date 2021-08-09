package com.macauslot.recruitmentadmin.repository;


import com.macauslot.recruitmentadmin.entity.RecruitmentDeptMap;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.RecruitmentGroupMemberVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

public class RecruitmentGroupRepositoryImpl implements RecruitmentGroupRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<RecruitmentGroupMemberVO> page4RecruitmentGroup(int start, int length, RecruitmentGroupMemberVO condition, String sidx, String sord) {


        String sql =
                "select f.id, department_code, name, max(case when role = 'department_head' then member end) as department_head, max(case when role = 'interviewer' then member end) as interviewer, max(case when role = 'administrative_personnel' then member end) as administrative_personnel from ( select rg.department_code , rg.name , group_concat(concat(rgm.employee_id, u1.CH_NAME)) as member, role, rg.id from recruitment_group_member rgm , recruitment_group rg , user u1 where rgm.recruitment_group_id = rg.id and rgm.employee_id = u1.USER_NAME ";
        String where =
                "";
        String sql2 = " group by rg.id, role)f group by f.id ";

        String countsql =
                "select count(*) from ( select f.* from ( select rg.department_code , rg.name , group_concat(concat(rgm.employee_id, u1.CH_NAME)) as member, role, rg.id from recruitment_group_member rgm , recruitment_group rg , user u1 where rgm.recruitment_group_id = rg.id and rgm.employee_id = u1.USER_NAME ";
        String countsql2 = " group by rg.id, role)f group by f.id) f2 ";


        String sort =
                "";

        String deptCode = condition.getDeptCode();
        if (StringUtils.isNotBlank(deptCode)) {
            where += " and rg.department_code = :deptCode ";
        }


        String groupName = condition.getGroupName();
        if (StringUtils.isNotBlank(groupName)) {
            where += " and rg.name = :groupName ";
        }

        String employeeId = condition.getEmployeeId();
        if (StringUtils.isNotBlank(employeeId)) {
//            where += " and rgm.employee_id = :employeeId ";
            where += " and rg.id in (select rgm2.recruitment_group_id from recruitment_group_member rgm2 where rgm2.employee_id = :employeeId) ";

        }
        if (StringUtils.isNotBlank(sidx)) {
            switch (sidx) {
                case "deptCode":
                    sort += " order by f.department_code ";
                    break;
                case "groupName":
                    sort += " order by f.name ";
                    break;
                default:
                    throw new IllegalArgumentException("sidx 錯誤");
            }
            if (StringUtils.isNotBlank(sort)) {
                sort += "asc".equals(sord) ? " asc" : " desc";
            }
        }


        Query query = em.createNativeQuery(sql + where + sql2 + sort);
        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql + where + countsql2);


        if (StringUtils.isNotBlank(deptCode)) {
            query.setParameter("deptCode", deptCode);
            countQuery.setParameter("deptCode", deptCode);
        }


        if (StringUtils.isNotBlank(groupName)) {
            query.setParameter("groupName", groupName);
            countQuery.setParameter("groupName", groupName);
        }

        if (StringUtils.isNotBlank(employeeId)) {
            query.setParameter("employeeId", employeeId);
            countQuery.setParameter("employeeId", employeeId);
        }


        List<RecruitmentGroupMemberVO> list = EntityUtils.mapping(query.getResultList(), RecruitmentGroupMemberVO.class, "page4RecruitmentGroup_findGroupById");
        BigInteger count = (BigInteger) countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }
    
}
