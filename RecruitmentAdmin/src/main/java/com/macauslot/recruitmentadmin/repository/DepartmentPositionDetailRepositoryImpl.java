package com.macauslot.recruitmentadmin.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.JobVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentProgressVO;

public class DepartmentPositionDetailRepositoryImpl implements DepartmentPositionDetailRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    /**
     * CommonGetJobDetail
     */


    @Override
    public Page<JobVO> page4JobDetail(int start, int length, JobVO condition, String sidx, String sord) {

    	String sql_1 =
                "select tmp.detail_id, department_code, job_title, job_code, start_date, end_date, case when post_status = 'D' then '草稿' when post_status = 'A' then '生效' when post_status = 'O' then '離線' when post_status = 'S' then '關閉' when post_status = 'T' then '模 版' end as post_status, last_modify_date, tmp.cr_date, tmp.close_date, tmp.code, tmp.headcount, tmp.is_sealed, tmp.recruitment_count from ( select distinct detail.dept_pos_detail_id detail_id, dp.department_code, dp.job_title, detail.job_code, detail.start_date, detail.end_date, detail.STATUS post_status , detail.last_modify_date, detail.cr_date, detail.close_date, o.code, detail.headcount, detail.is_sealed, (select count(*) from applicant_position ap where ap.dept_pos_detail_id = detail.dept_pos_detail_id and process_stage = 12) as recruitment_count from organization o, department_position dp, department_position_detail detail where o.org_id = dp.org_id and dp.dept_pos_id = detail.dept_pos_id";
        String countsql_1 =
//self-bak:                 "select count(*) from ( select detail.dept_pos_detail_id detail_id, d.NAME, dp.job_title, detail.job_code, detail.start_date, detail.end_date, case when detail.STATUS = 'D' then '草稿' when detail.STATUS = 'A' then '生效' when detail.STATUS = 'O' then '離線' end post_status from department d, department_position dp, department_position_detail detail where d.department_id = dp.department_id and dp.dept_pos_id = detail.dept_pos_id";

        "select count(*) from ( select distinct detail.dept_pos_detail_id detail_id, dp.department_code, dp.job_title, detail.job_code, detail.start_date, detail.end_date, detail.STATUS post_status , detail.last_modify_date, detail.cr_date, o.code, detail.headcount, detail.is_sealed from organization o, department_position dp, department_position_detail detail where o.org_id = dp.org_id and dp.dept_pos_id = detail.dept_pos_id";
        String where_1 = "";

        where_1 += " and detail.recruitment_form is not null ";//聘請形式為新系統必填字段，因此該字段為null的即是舊職位。

        String sql_2 =
                " ) tmp where 1=1";

        String where_2 = "";

        String sort = "";

        String dept_code = condition.getDept_code();
        if (StringUtils.isNotBlank(dept_code)) {
            where_1 += " and dp.department_code = :dept_code";
        }

        String post_status = condition.getPost_status();
        if (StringUtils.isNotBlank(post_status)) {
            where_1 += " and detail.status = :post_status";
        }
        else {
            where_1 += " and detail.status != 'T' ";
        }

        String job_code = condition.getJob_code();
        if (StringUtils.isNotBlank(job_code)) {
            where_1 += " and job_code like :job_code";
        }



        String conditionOrg = condition.getOrg();
        if (StringUtils.isNotBlank(conditionOrg)) {
            where_1 += " and o.code = :conditionOrg";
        }

        if (StringUtils.isNotBlank(sidx)) {
            switch (sidx) {
            	case "org":
            		sort += " order by tmp.code";
            		break;
                case "detail_id":
                    sort += " order by tmp.detail_id";
                    break;
                case "dept_name":
                    sort += " order by tmp.department_code";
                    break;
                case "job_title":
                    sort += " order by tmp.job_title";
                    break;
                case "job_code":
                    sort += " order by tmp.job_code";
                    break;
                case "start_date":
                    sort += " order by tmp.start_date";
                    break;
                case "post_status":
                    sort += " order by tmp.post_status";
                    break;
                case "cr_date":
                    sort += " order by tmp.cr_date";
                    break;
                case "headcount":
                    sort += " order by tmp.headcount";
                    break;

                default:
                    sort += " order by tmp.job_code";
                    break;
            }
            if (StringUtils.isNotBlank(sort)) {
                sort += "asc".equals(sord) ? " asc" : " desc";
            }
        }

        Query query = em.createNativeQuery(sql_1 + where_1 + sql_2 + where_2 + sort);

        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql_1 + where_1 + sql_2 + where_2);



        if (StringUtils.isNotBlank(dept_code)) {
            query.setParameter("dept_code", dept_code);
            countQuery.setParameter("dept_code", dept_code);
        }

        if (StringUtils.isNotBlank(post_status)) {
            query.setParameter("post_status", post_status);
            countQuery.setParameter("post_status", post_status);
        }

        if (StringUtils.isNotBlank(job_code)) {
            query.setParameter("job_code", "%"+job_code+"%");
            countQuery.setParameter("job_code", "%"+job_code+"%");
        }
        if (StringUtils.isNotBlank(conditionOrg)) {
            query.setParameter("conditionOrg", conditionOrg);
            countQuery.setParameter("conditionOrg", conditionOrg);
        }

        List<JobVO> list = EntityUtils.castEntity(query.getResultList(), JobVO.class);
        BigInteger count = (BigInteger) countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }


    @Override
    public Page<JobVO> page4OpeningJob(int start, int length, String sidx, String sord, char[] status) {

    	String sql_1 =
                "select detail.dept_pos_detail_id, dp.job_title, detail.job_code, detail.headcount, detail.status, \r\n" +
                "(select count(*) from applicant_position ap where ap.dept_pos_detail_id = detail.dept_pos_detail_id and process_stage = 12) as recruitment_count, \r\n" +
                "(select count(*) from applicant_position ap where ap.bookmarked_job_id = detail.dept_pos_detail_id) as bookmark_count\r\n" +
                "from department_position_detail detail, department_position dp \r\n" +
                "where dp.dept_pos_id = detail.dept_pos_id and detail.recruitment_form is not null";
        String countsql_1 =
                "select count(*)" +
                "from department_position_detail detail, department_position dp \r\n" +
                "where dp.dept_pos_id = detail.dept_pos_id and detail.recruitment_form is not null";

        if(status.length>0) {
        	String statusStr = " and detail.`status` in (";
        	for(int i=0;i<status.length;i++) {
        		char s = status[i];
        		statusStr += "'"+s+"'";
        		if(i!=status.length-1) {
        			statusStr += ",";
        		}
        	}
        	statusStr+=")";
            sql_1+=statusStr;
            countsql_1+=statusStr;
        }

        String sort = "";

        if (StringUtils.isNotBlank(sidx)) {
            switch (sidx) {
                case "job_title":
                    sort += " order by dp.job_title";
                    break;
                case "job_code":
                    sort += " order by detail.job_code";
                    break;
                case "start_date":
                    sort += " order by detail.start_date";
                    break;
                default:
                    break;
            }
            if (StringUtils.isNotBlank(sort)) {
                sort += "asc".equals(sord) ? " asc" : " desc";
            }
        }

        Query query = em.createNativeQuery(sql_1 + sort);
        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql_1);

        List<JobVO> list = EntityUtils.castEntity(query.getResultList(), JobVO.class);
        BigInteger count = (BigInteger) countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }


    @Override
    public List<JobVO> getOpenJob() {
    	String sql =
                "select detail.dept_pos_detail_id, detail.job_code, dp.job_title\r\n" +
              "from department_position_detail detail, department_position dp \r\n" +
                "where dp.dept_pos_id = detail.dept_pos_id and detail.status in ('A','O') order by detail.job_code asc";

        Query query = em.createNativeQuery(sql);

        List<Object[]> data = query.getResultList();
        List<JobVO> list = EntityUtils.castEntity(data, JobVO.class);
        return list;

    }

    @Override
    public List<JobVO> getDeptOpenJob(String deptCode) {
    	String sql =
                "select detail.dept_pos_detail_id, detail.job_code, dp.job_title\r\n" +
                "from department_position_detail detail, department_position dp \r\n" +
                "where dp.dept_pos_id = detail.dept_pos_id \r\n" +
                " and detail.status in ('A','O') \r\n" +
                "and dp.department_code = :deptCode " +
                "order by detail.job_code asc";

        Query query = em.createNativeQuery(sql);
        query.setParameter("deptCode", deptCode);

        List<Object[]> data = query.getResultList();
        List<JobVO> list = EntityUtils.castEntity(data, JobVO.class);
        return list;

    }


    @Override
    public Page<RecruitmentProgressVO> page4RecruitmentProgress(int start, int length, String sidx, String sord, UserDTO user) {

    	String sql =
                "select detail.dept_pos_detail_id,  detail.job_code, dp.job_title, detail.headcount, " +
                "(select count(*) from applicant_position apppos where apppos .process_stage is not null and((apppos.bookmarked_job_id is null and apppos.dept_pos_detail_id = detail.dept_pos_detail_id) or (apppos.bookmarked_job_id is not null and apppos.bookmarked_job_id = detail.dept_pos_detail_id)) and apppos.process_stage = 12) as recruited, " +
                "CASE detail.status WHEN 'A' THEN 'Open' WHEN 'O' THEN 'On-hold' WHEN 'S' THEN 'Closed' ELSE '' END AS job_status, dp.department_code as dept " +
                "from  department_position_detail detail, department_position dp " +
                "where detail.dept_pos_id = dp.dept_pos_id " +
                "and detail.status in ('A','O','S') ";
        String countsql =
        		"select count(1) " +
                        "from  department_position_detail detail, department_position dp " +
                        "where detail.dept_pos_id = dp.dept_pos_id " +
                        "and detail.status in ('A','O','S') ";
        String dept = user.getDeptCode();
        String employeeId = user.getEmployeeId();
        
        if(dept!=null&&dept.compareTo("HR")!=0) {
        	sql += " and dp.department_code = :dept ";
        	countsql += " and dp.department_code = :dept ";
        	
        	if(employeeId!=null&&employeeId.compareTo("")!=0) {
        		sql += " and detail.recruitment_group_id in (select distinct(rgm.recruitment_group_id) from recruitment_group_member rgm where rgm.employee_id = :employee_id) ";
        		countsql += " and detail.recruitment_group_id in (select distinct(rgm.recruitment_group_id) from recruitment_group_member rgm where rgm.employee_id = :employee_id) ";
        	}
        }

        String sort = "";

        if (StringUtils.isNotBlank(sidx)) {
            switch (sidx) {
                case "job_title":
                    sort += " order by dp.job_title";
                    break;
                case "job_code":
                    sort += " order by detail.job_code";
                    break;
                case "job_status":
                    sort += " order by detail.status";
                    break;
                case "recruited":
                    sort += " order by recruited";
//                    sort += " order by (select count(*) from applicant_position apppos where ((apppos.bookmarked_job_id is null and apppos.dept_pos_detail_id = detail.dept_pos_detail_id) or (apppos.bookmarked_job_id is not null and apppos.bookmarked_job_id = detail.dept_pos_detail_id)) and apppos.process_stage = 12)";

                    break;
                default:
                    break;
            }
            if (StringUtils.isNotBlank(sort)) {
                sort += "asc".equals(sord) ? " asc" : " desc";
            }
        }

        System.out.println(sql + sort);

        Query query = em.createNativeQuery(sql + sort);

        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql);

        if(dept!=null&&dept.compareTo("HR")!=0) {
            query.setParameter("dept", dept);
        	countQuery.setParameter("dept", dept);
        	
        	if(employeeId!=null&&employeeId.compareTo("")!=0) {
        		query.setParameter("employee_id", employeeId);
        		countQuery.setParameter("employee_id", employeeId);
        	}
        }

        List<Object[]> data = query.getResultList();
        List<RecruitmentProgressVO> list = EntityUtils.castEntity(data, RecruitmentProgressVO.class);
        BigInteger count = (BigInteger) countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }

}
