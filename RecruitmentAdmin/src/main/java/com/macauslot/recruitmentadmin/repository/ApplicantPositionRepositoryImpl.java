package com.macauslot.recruitmentadmin.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONArray;
import com.macauslot.recruitmentadmin.util.DateBetweenUtil;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.ApplicantJobDetailVO;
import com.macauslot.recruitmentadmin.vo.ApplicantionRecordReportVO;
import com.macauslot.recruitmentadmin.vo.ApplicationHistoryVO;
import com.macauslot.recruitmentadmin.vo.ApplicationInterviewStatusSummaryVO;
import com.macauslot.recruitmentadmin.vo.ApplicationVO;
import com.macauslot.recruitmentadmin.vo.CandidateInformationReportVO;
import com.macauslot.recruitmentadmin.vo.CommonVO;
import com.macauslot.recruitmentadmin.vo.MailVO;
import com.macauslot.recruitmentadmin.vo.NoticeVO;


public class ApplicantPositionRepositoryImpl implements ApplicantPositionRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    /**
     * GetApplicantJobDetail
     */
    @Override
    public Page<ApplicantJobDetailVO> page4ApplicantJobDetail(int start, int length, ApplicantJobDetailVO condition, String sidx, String sord) {

        String sql =
                " select app.applicant_info_id, apppos.export_date, CONCAT(app.en_f_name, ' ',  app.en_l_name) as 'Applicant_Name', " +
                        "  app.gender, app.dob, " +
//                        "  (year(curdate()) - year(app.dob)) + (IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(app.dob), '-', DAY(app.dob)) ,'%Y-%c-%e') > CURDATE(), 0, 1)) as age, " +
                        "  TIMESTAMPDIFF(YEAR,app.dob,CURDATE()) as age, " +

                        "  app.id_card_number, app.mobile, " +
                        "  CONCAT(e.organization_name, ' - ', e.major) as 'education', " +
                        "  CONCAT(w.company_name, ' - ', w.position) as 'work_experience', " +
                        "  ifnull(app.application_org,'') as application_org, app.email_checking, apppos.dept_pos_detail_id, " +
                        "  case app.email_status when 'Y' then 'Sent' end as 'Mail_Status', " +


                        " deptpos .job_title," +
                        " posdetail.job_code," +
                        " (select m.chn_desc from message m, message_group mg where m.message_group_id = mg.id and mg.description = '招聘流程' and m.code = apppos.process_stage) as process_stage," +
                        " apppos.applicant_pos_id" +

//                        "  get_job_title(app.applicant_info_id, 1) as 'Apply_Position_1', " +
//                        "  get_job_code(app.applicant_info_id, 1) as 'Job_Code_1'" +


//                        ", " +
//                        "  get_job_title(app.applicant_info_id, 2) as 'Apply_Position_2', " +
//                        "  get_job_code(app.applicant_info_id, 2) as 'Job_Code_2', " +
//                        "  get_job_title(app.applicant_info_id, 3) as 'Apply_Position_3', " +
//                        "  get_job_code(app.applicant_info_id, 3) as 'Job_Code_3'  " +


                        " from applicant_info app " +
                        " inner join applicant_position apppos on app.applicant_info_id = apppos.applicant_info_id " +
                        " inner join department_position_detail posdetail on posdetail.dept_pos_detail_id = apppos.dept_pos_detail_id " +
                        " inner join department_position deptpos on deptpos.dept_pos_id = posdetail.dept_pos_id " +
                        " left join education e on (app.applicant_info_id = e.applicant_info_id and e.education_id in (select min(e2.education_id) from education e2 where app.applicant_info_id = e2.applicant_info_id)) " +
                        " left join working_experience w on (app.applicant_info_id = w.applicant_info_id and w.experience_id in (select min(w2.experience_id) from working_experience w2 where app.applicant_info_id = w2.applicant_info_id)) " +
                        " where app.status = 'Y' and app.incomplete_page_no = '7'" +
                        " and apppos.process_stage is not null ";


//                        +
//                        " and apppos.job_priority = 1";
        String countsql = " select count(app.applicant_info_id)" +
                " from applicant_info app " +
                " inner join applicant_position apppos on app.applicant_info_id = apppos.applicant_info_id " +
                " inner join department_position_detail posdetail on posdetail.dept_pos_detail_id = apppos.dept_pos_detail_id " +
                " inner join department_position deptpos on deptpos.dept_pos_id = posdetail.dept_pos_id " +
                " left join education e on (app.applicant_info_id = e.applicant_info_id and e.education_id in (select min(e2.education_id) from education e2 where app.applicant_info_id = e2.applicant_info_id)) " +
                " left join working_experience w on (app.applicant_info_id = w.applicant_info_id and w.experience_id in (select min(w2.experience_id) from working_experience w2 where app.applicant_info_id = w2.applicant_info_id))" +
                " where app.status = 'Y' and app.incomplete_page_no = '7'" +
                " and apppos.process_stage is not null";


//                +
//                " and apppos.job_priority = 1";
        String where = "";
       /*
        String group =
                " group by app.applicant_info_id, app.en_f_name, app.en_l_name, app.gender, app.dob, app.id_card_number, app.mobile, " +
                        " e.organization_name, e.major, w.company_name, w.position, app.application_org,app.email_status ";
      */


        String sort = "";


        Date startDate = condition.getApplicant_postition_start_date();
        Date endDate = condition.getApplicant_postition_end_date();
        if (startDate != null &&
                endDate != null) {
            where += " and date(apppos.cr_date) between date(:startDate) and date(:endDate)";
        }

        String departmentCode = condition.getDepartmentCode();
        if (departmentCode != null) {
            where += " and deptpos.department_code = :departmentCode";
        }

        String status = condition.getExport_date_status();
        if (StringUtils.isNotBlank(status)) {
            if ("is null".equals(status)) {
                where += " and apppos.export_date is null";
            } else if ("is not null".equals(status)) {
                where += " and apppos.export_date is not null";
            }
        }

        Integer applicantId = condition.getApplicant_info_id();
        if (applicantId != null) {
            where += " and app.applicant_info_id = :applicantId";
        }

        String id_card_number = condition.getId_card_number();
        if (StringUtils.isNotBlank(id_card_number)) {
            where += " and app.id_card_number = :id_card_number";
        }


        String mobile = condition.getMobile();
        if (StringUtils.isNotBlank(mobile)) {
            where += " and app.mobile = :mobile";
        }

        if (StringUtils.isNotBlank(sidx)) {
            String tmpSord;
            if (StringUtils.isNotBlank(sord)) {
                if (!"age".equals(sidx)) {
                    tmpSord = "asc".equals(sord) ? " asc" : " desc";
                } else {
                    tmpSord = "asc".equals(sord) ? " desc" : " asc";
                }
            } else {
                throw new IllegalArgumentException("sidx sord error");
            }
            switch (sidx) {
                case "export_date":
                    sort += " order by apppos.export_date" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "applicant_info_id":
                    sort += " order by app.applicant_info_id";
                    break;
                case "applicant_Name":
                    sort += " order by app.en_l_name" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "gender":
                    sort += " order by app.gender" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "age":
                    sort += " order by app.dob" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "id_card_number":
                    sort += " order by app.id_card_number" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "mobile":
                    sort += " order by app.mobile" + tmpSord +
                            ",app.applicant_info_id";
                    break;

                case "apply_Position_1":
                    sort += " order by deptpos.job_title" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "job_Code_1":
                    sort += " order by posdetail.job_code" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "application_org":
                    sort += " order by app.application_org" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "mail_Status":
                    sort += " order by app.email_status" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "process_stage":
                    sort += " order by apppos.process_stage" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                default:
                    break;
            }

        }

        Query query = em.createNativeQuery(sql + where + sort);

        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql + where);

        if (startDate != null &&
                endDate != null) {
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            countQuery.setParameter("startDate", startDate);
            countQuery.setParameter("endDate", endDate);
        }

        if (departmentCode != null) {
            query.setParameter("departmentCode", departmentCode);
            countQuery.setParameter("departmentCode", departmentCode);
        }
        if (applicantId != null) {
            query.setParameter("applicantId", applicantId);
            countQuery.setParameter("applicantId", applicantId);
        }

        if (StringUtils.isNotBlank(id_card_number)) {
            query.setParameter("id_card_number", id_card_number);
            countQuery.setParameter("id_card_number", id_card_number);
        }

        if (StringUtils.isNotBlank(mobile)) {
            query.setParameter("mobile", mobile);
            countQuery.setParameter("mobile", mobile);
        }


        List<ApplicantJobDetailVO> list = EntityUtils.castEntity(query.getResultList(), ApplicantJobDetailVO.class);
        countRowspan(list, length);
        System.err.println(list);
        BigInteger count = (BigInteger) countQuery.getSingleResult();

        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }

    private void countRowspan(List<ApplicantJobDetailVO> voList, int pageLength) {

        List<Integer> hadGetKeys = new ArrayList<>(); //曾经取过的key

        int currentPage = 0;
        int rowCount = 0;
        int size = voList.size();
        for (int i = 0; i < size; i++) {


            int page = rowCount / pageLength;
            if (page != currentPage) {
                currentPage = page;
                hadGetKeys = new ArrayList<>();
            }
            rowCount++;


            int countRowspan = 0;
            Integer applicant_info_id = voList.get(i).getApplicant_info_id();


            if (hadGetKeys.contains(applicant_info_id)) {//曾经取过
                Integer old_applicant_info_id = -1;

                old_applicant_info_id = voList.get(i - 1).getApplicant_info_id();

                if (old_applicant_info_id.equals(applicant_info_id)) {
                    voList.get(i).setRowSpan(0);
                    continue;
                }
               /* else {
                    voList.get(i).setRowSpan(1);
                }
                continue;*/
            }


            for (int j = i + 1; j < size; j++) {
                if (voList.get(i).equals(voList.get(j))) {
                    countRowspan++;
                } else {
                    break;
                }
            }
            voList.get(i).setRowSpan(countRowspan + 1);
            hadGetKeys.add(applicant_info_id);

        }
    }

    @Override
    public Page<ApplicationVO> searchApplication(String employeeId, int start, int length, ApplicationVO condition, String sidx, String sord) {
        String sql =
                " select apppos.applicant_pos_id, app.applicant_info_id, apppos.dept_pos_detail_id, apppos.cr_date, " +
                        " posdetail.job_code, deptpos.job_title, " +
                        " IFNULL(apppos.bookmarked_job_id,apppos.dept_pos_detail_id) as belong_job_id, " +
                        " IFNULL(posdetail1.job_code,posdetail.job_code) as belong_job_code, " +
                        " IFNULL(deptpos1.job_title,deptpos.job_title) as belong_job_title, " +
                        " apppos.bookmarked_job_id, " +
                        " posdetail1.job_code as bookmarked_job_code, " +
                        " deptpos1.job_title as bookmarked_job_title, " +
                        " (select chn_desc from message where id = apppos.source_id) as ad_source, " +
                        " CONCAT(app.cn_l_name, app.cn_f_name, ',' ,app.en_l_name,' ',app.en_f_name) as 'Applicant_Name', " +
                        " m1.chn_desc, app.id_card_number, app.gender, " +
//                        " (year(curdate()) - year(app.dob)) + (IF(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', MONTH(app.dob), '-', DAY(app.dob)) ,'%Y-%c-%e') > CURDATE(), 0, 1)) as age, " +
                        "  TIMESTAMPDIFF(YEAR,app.dob,CURDATE()) as age, " +

                        " case app.area when 'CN' then '中國大陸' when 'MO' then '澳門' when 'HK' then '香港' else '其它' end as area, app.mobile, " +
//                        " CONCAT(e.organization_name, ' - ', e.major) as 'education', " +
                        " e.`degree`, " +
                        " IFNULL(e.organization_name, 'N/A'), " +
                        " IFNULL(e.major, 'N/A'), " +
                        
                        " (select m.chn_desc from message m, message_group mg where m.message_group_id = mg.id and mg.description = '招聘流程' and m.code = apppos.process_stage) as process_stage, " +
                        " apppos.process_stage as process_stage_code, IFNULL(apppos.dept_reply_date,'') as dept_reply_date, " +
                        " IFNULL(apppos.prefer_interview_time1,'') as prefer_interview_time1,IFNULL(apppos.prefer_interview_time2,'') as prefer_interview_time2,IFNULL(apppos.prefer_interview_time3,'') as prefer_interview_time3,IFNULL(apppos.prefer_interview_time4,'') as prefer_interview_time4,IFNULL(apppos.prefer_interview_time5,'') as prefer_interview_time5,IFNULL(apppos.interview_time,'') as interview_time, " +
                        " apppos.meeting_room_id, " +
                        " proc.last_modify_date as employ_date, " +
                        " deptpos.department_code, " +
                        " ifnull(case when apppos.process_stage = 12 then '1.1 聘用'  when apppos.process_stage = 11 then (select concat(b.index,' ',b.desc) from applicant_position_process a, applicant_position_process_result b where a.stage_action_remark = b.desc and a.applicant_position_id = apppos.applicant_pos_id and a.new_stage = apppos.process_stage order by a.applicant_position_process_id desc LIMIT 1) else '' end,'N/A') as result, " +
                        " app.blacklisted, " +
                        " case when (select count(1) from applicant_position ap1 where ap1.applicant_info_id = apppos.applicant_info_id and ap1.dept_pos_detail_id = apppos.dept_pos_detail_id and ap1.cr_date<apppos.cr_date and DATE_ADD(ap1.cr_date, INTERVAL 3 MONTH)>=apppos.cr_date)>0 then 1 else 0 end as duplicated," +
                        " group_concat(m.code ORDER BY m.code), group_concat(m.CHN_DESC ORDER BY m.code), " +
                        " mr.meeting_room_name " +
                        " from applicant_info app " +
                        " inner join applicant_position apppos on app.applicant_info_id = apppos.applicant_info_id " +
                        " inner join department_position_detail posdetail on posdetail.dept_pos_detail_id = apppos.dept_pos_detail_id " +
                        " inner join department_position deptpos on deptpos.dept_pos_id = posdetail.dept_pos_id " +
                        " inner join organization org on deptpos.org_id = org.org_id " +
                        " left join education e on (app.applicant_info_id = e.applicant_info_id and e.education_id in (select min(e2.education_id) from education e2 where app.applicant_info_id = e2.applicant_info_id)) " +
                        " left join working_experience w on (app.applicant_info_id = w.applicant_info_id and w.experience_id in (select min(w2.experience_id) from working_experience w2 where app.applicant_info_id = w2.applicant_info_id)) " +
                        " left join department_position_detail posdetail_1 on ((apppos.bookmarked_job_id is null and posdetail_1.dept_pos_detail_id = apppos.dept_pos_detail_id) or (posdetail_1.dept_pos_detail_id = apppos.bookmarked_job_id)) " +
                        " left join department_position deptpos_1 on deptpos_1.dept_pos_id = posdetail_1.dept_pos_id " +
                        " left join organization org1 on deptpos_1.org_id = org1.org_id " +
                        " left join applicant_position_process proc on proc.applicant_position_process_id = (select b.applicant_position_process_id from applicant_position_process b where b.applicant_position_id = apppos.applicant_pos_id and b.new_stage = 12 and b.applicant_position_process_id = (select max(applicant_position_process_id) from applicant_position_process c where c.applicant_position_id = apppos.applicant_pos_id)) " +
                        " left join department_position_detail posdetail1 on posdetail1.dept_pos_detail_id = apppos.bookmarked_job_id " +
                        " left join department_position deptpos1 on deptpos1.dept_pos_id = posdetail1.dept_pos_id " +
                        " left join (select message.code, message.CHN_DESC from message, message_group where message_group.id = message.MESSAGE_GROUP_ID order by message.code) as m on m.code like concat(CAST(apppos.process_stage as SIGNED),'-%') " +
                        " left join meeting_room mr on apppos.meeting_room_id = mr.meeting_room_id " +
                        " left join message m1 on app.id_type_id = m1.id " +
                        " where apppos.process_stage is not null";


        String countsql = " select count(app.applicant_info_id)" +
                " from applicant_info app " +
                " inner join applicant_position apppos on app.applicant_info_id = apppos.applicant_info_id " +
                " inner join department_position_detail posdetail on posdetail.dept_pos_detail_id = apppos.dept_pos_detail_id " +
                " inner join department_position deptpos on deptpos.dept_pos_id = posdetail.dept_pos_id " +
                " inner join organization org on deptpos.org_id = org.org_id " +
                " left join education e on (app.applicant_info_id = e.applicant_info_id and e.education_id in (select min(e2.education_id) from education e2 where app.applicant_info_id = e2.applicant_info_id)) " +
                " left join working_experience w on (app.applicant_info_id = w.applicant_info_id and w.experience_id in (select min(w2.experience_id) from working_experience w2 where app.applicant_info_id = w2.applicant_info_id)) " +
                " left join department_position_detail posdetail_1 on ((apppos.bookmarked_job_id is null and posdetail_1.dept_pos_detail_id = apppos.dept_pos_detail_id) or (posdetail_1.dept_pos_detail_id = apppos.bookmarked_job_id)) " +
                " left join department_position deptpos_1 on deptpos_1.dept_pos_id = posdetail_1.dept_pos_id " +
                " left join organization org1 on deptpos_1.org_id = org1.org_id " +
                " left join applicant_position_process proc on proc.applicant_position_process_id = (select b.applicant_position_process_id from applicant_position_process b where b.applicant_position_id = apppos.applicant_pos_id and b.new_stage = 12 and b.applicant_position_process_id = (select max(applicant_position_process_id) from applicant_position_process c where c.applicant_position_id = apppos.applicant_pos_id)) " +
                " where apppos.process_stage is not null";

        String groupBy = "";
        String where = "";


        if (StringUtils.isNotBlank(employeeId)) {
            where += " and (CASE WHEN apppos.bookmarked_job_id IS NOT NULL THEN posdetail_1.recruitment_group_id ELSE posdetail.recruitment_group_id END) in (select distinct(rgm.recruitment_group_id) from recruitment_group_member rgm where rgm.employee_id = :employee_id) ";
        }

       /*
        String group =
                " group by app.applicant_info_id, app.en_f_name, app.en_l_name, app.gender, app.dob, app.id_card_number, app.mobile, " +
                        " e.organization_name, e.major, w.company_name, w.position, app.application_org,app.email_status ";
      */


        String sort = "";


        Date startDate = condition.getStart_date();
        Date endDate = condition.getEnd_date();
        if (startDate != null &&
                endDate != null) {
            where += " and date(apppos.cr_date) between date(:startDate) and date(:endDate)";
        }

        String departmentCode = condition.getDepartment_code();
        if (departmentCode != null) {
            where += " and (CASE WHEN apppos.bookmarked_job_id IS NOT NULL THEN deptpos_1.department_code ELSE deptpos.department_code END) = :departmentCode";
        }

        String userDeptCode = condition.getUser_dept();
        if (StringUtils.isNotBlank(userDeptCode)) {
            where += " and (CASE WHEN apppos.bookmarked_job_id IS NOT NULL THEN deptpos_1.department_code ELSE deptpos.department_code END) = :userDeptCode";
        }

        Integer applicantId = condition.getApplicant_info_id();
        if (applicantId != null) {
            where += " and app.applicant_info_id = :applicantId";
        }
        Integer applicantPosId = condition.getApplicant_pos_id();
        if (applicantPosId != null) {
            where += " and apppos.applicant_pos_id = :applicantPosId";
        }

        String applicant_Name = condition.getApplicant_Name();
        if (StringUtils.isNotBlank(applicant_Name)) {
            where += " and (app.en_f_name like :applicant_Name||app.en_l_name like :applicant_Name||app.cn_f_name like :applicant_Name||app.cn_l_name like :applicant_Name)";
        }

        String id_card_number = condition.getId_card_number();
        if (StringUtils.isNotBlank(id_card_number)) {
            where += " and app.id_card_number like :id_card_number";
        }


        String mobile = condition.getMobile();
        if (StringUtils.isNotBlank(mobile)) {
            where += " and app.mobile like :mobile";
        }

        Integer jobId = condition.getDept_pos_detail_id();
        if (jobId != null) {
            where += " and posdetail.dept_pos_detail_id = :jobId";
        }

        Integer belongJobId = condition.getBelong_job_id();
        if (belongJobId != null) {
            if (belongJobId == -1) {
                where += " and ((apppos.bookmarked_job_id is not null and apppos.bookmarked_job_id in (select dept_pos_detail_id from department_position_detail where status in ('A','O'))) or (apppos.bookmarked_job_id is null and apppos.dept_pos_detail_id in (select dept_pos_detail_id from department_position_detail where status in ('A','O'))))";
            } else {
                where += " and ((apppos.bookmarked_job_id is null and apppos.dept_pos_detail_id = :belongJobId) or apppos.bookmarked_job_id = :belongJobId) ";
            }
        }

        String org = condition.getOrg();
        if (StringUtils.isNotBlank(org)) {
        	where += " and ((apppos.bookmarked_job_id is null and org.code = :org) or org1.code = :org) ";
        }

        Integer bookmarkedJobId = condition.getBookmarked_job_id();
        if (bookmarkedJobId != null) {
            if (bookmarkedJobId == -1) {
                where += " and apppos.bookmarked_job_id in (select dept_pos_detail_id from department_position_detail where status in ('A','O'))";
            } else {
                where += " and apppos.bookmarked_job_id = :bookmarkedJobId ";
            }
        }

        String status = condition.getStatus();
        if (StringUtils.isNotBlank(status)) {
        	where += " and ((apppos.bookmarked_job_id is null and posdetail.status in (:status)) or posdetail_1.status in (:status)) ";
        }

        String process_status = condition.getProcess_status();
        if (StringUtils.isNotBlank(process_status)) {
            if (process_status.compareTo("A") == 0) {
                where += " and apppos.process_stage !=11 and apppos.process_stage !=12 ";
            }
            if (process_status.compareTo("S") == 0) {
                where += " and (apppos.process_stage =11 or apppos.process_stage =12) ";
            }
        }


        String process_stage_code = condition.getProcess_stage_code();
        if (StringUtils.isNotBlank(process_stage_code)) {
        	if(process_stage_code.startsWith(">")) {
                where += " and apppos.process_stage > :process_stage_code";
        	}
        	else if(process_stage_code.startsWith("<")) {
                where += " and apppos.process_stage < :process_stage_code";
        	}
        	else {
                where += " and apppos.process_stage = :process_stage_code";
        	}
        }

//        String process_stage_code1 = condition.getProcess_stage_code1();
//        if (StringUtils.isNotBlank(process_stage_code1)) {
//        	if(process_stage_code1.compareTo("3")==0) {
//                where += " and (apppos.process_stage = :process_stage_code1 or (apppos.process_stage = 11 and apppos.dept_reply_date > DATE_ADD(CURRENT_TIMESTAMP , INTERVAL -3 DAY)))";
//            }
//        	else {
//                where += " and apppos.process_stage = :process_stage_code1";
//        	}
//        }


        String employ_status = condition.getEmploy_status();
        if (employ_status != null && employ_status.compareTo("A") == 0) {
            where += " and proc.last_modify_date is not null";
        }

        Integer interview_arranged = condition.getInterview_arranged();
        if(interview_arranged == null) {
        }
        else if(interview_arranged == 0) {
        	where += " and (apppos.interview_time is null or apppos.interview_time ='')";
        }
        else if(interview_arranged == 1) {
            	where += " and (apppos.interview_time is not null and apppos.interview_time !='')";
        }

        groupBy = " group by apppos.applicant_pos_id ";

        if (StringUtils.isNotBlank(sidx)) {
            String tmpSord;
            if (StringUtils.isNotBlank(sord)) {
                if (!"age".equals(sidx)) {
                    tmpSord = "asc".equals(sord) ? " asc" : " desc";
                } else {
                    tmpSord = "asc".equals(sord) ? " desc" : " asc";
                }
            } else {
                throw new IllegalArgumentException("sidx sord error");
            }
            switch (sidx) {
                case "apply_date":
                    sort += " order by apppos.cr_date" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "applicant_pos_id":
                    sort += " order by apppos.applicant_pos_id" + tmpSord;
                    break;
                case "applicant_info_id":
                    sort += " order by app.applicant_info_id" + tmpSord;
                    break;
                case "applicant_Name":
                    sort += " order by CONVERT(Applicant_Name USING gbk)" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "gender":
                    sort += " order by app.gender" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "age":
                    sort += " order by app.dob" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "id_type":
                    sort += " order by CONVERT(m1.chn_desc USING gbk)" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "id_card_number":
                    sort += " order by app.id_card_number" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "mobile":
                    sort += " order by app.mobile" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "education":
                    sort += " order by CONCAT(e.organization_name, ' - ', e.major)" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "ad_source":
                    sort += " order by (select chn_desc from message where id = apppos.source_id)" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "job_Code":
                    sort += " order by posdetail.job_code" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "process_stage":
                    sort += " order by apppos.process_stage" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "area":
                    sort += " order by app.area" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "blackListed":
                    sort += " order by app.blacklisted" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "duplicated":
                    sort += " order by case when (select count(1) from applicant_position ap1 where ap1.applicant_info_id = apppos.applicant_info_id and ap1.dept_pos_detail_id = apppos.dept_pos_detail_id and ap1.cr_date<apppos.cr_date and DATE_ADD(ap1.cr_date, INTERVAL 3 MONTH)>=apppos.cr_date)>0 then 1 else 0 end " + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "result":
                    sort += " order by ifnull(case when apppos.process_stage = 12 then '1.1 聘用'  when apppos.process_stage = 11 then (select concat(b.index,' ',b.desc) from applicant_position_process a, applicant_position_process_result b where a.stage_action_remark = b.desc and a.applicant_position_id = apppos.applicant_pos_id and a.new_stage = apppos.process_stage order by a.applicant_position_process_id desc LIMIT 1) else '' end,'N/A') " + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "belong_job_code":
                    sort += " order by IFNULL(posdetail1.job_code,posdetail.job_code) " + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "belong_job_title":
                    sort += " order by IFNULL(deptpos1.job_title,deptpos.job_title) " + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "job_title":
                    sort += " order by deptpos.job_title " + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "dept_reply_date":
                    sort += " order by IFNULL(apppos.dept_reply_date,'') " + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "bookmarked_job_code":
                    sort += " order by posdetail1.job_code " + tmpSord +
                            ",app.applicant_info_id";
                    break;
                default:
                    break;
            }

        }

        Query query = em.createNativeQuery(sql + where + groupBy + sort);

        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql + where);


        if (StringUtils.isNotBlank(employeeId)) {
            query.setParameter("employee_id", employeeId);
            countQuery.setParameter("employee_id", employeeId);
        }

        if (startDate != null && endDate != null) {
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            countQuery.setParameter("startDate", startDate);
            countQuery.setParameter("endDate", endDate);
        }

        if (departmentCode != null) {
            query.setParameter("departmentCode", departmentCode);
            countQuery.setParameter("departmentCode", departmentCode);
        }

        if (StringUtils.isNotBlank(userDeptCode)) {
            query.setParameter("userDeptCode", userDeptCode);
            countQuery.setParameter("userDeptCode", userDeptCode);
        }

        if (applicantId != null) {
            query.setParameter("applicantId", applicantId);
            countQuery.setParameter("applicantId", applicantId);
        }

        if (applicantPosId != null) {
            query.setParameter("applicantPosId", applicantPosId);
            countQuery.setParameter("applicantPosId", applicantPosId);
        }

        if (StringUtils.isNotBlank(applicant_Name)) {
            query.setParameter("applicant_Name", "%" + applicant_Name + "%");
            countQuery.setParameter("applicant_Name", "%" + applicant_Name + "%");
        }

        if (StringUtils.isNotBlank(id_card_number)) {
            query.setParameter("id_card_number", "%" + id_card_number + "%");
            countQuery.setParameter("id_card_number", "%" + id_card_number + "%");
        }

        if (StringUtils.isNotBlank(mobile)) {
            query.setParameter("mobile", "%" + mobile + "%");
            countQuery.setParameter("mobile", "%" + mobile + "%");
        }
        if (jobId != null) {
            query.setParameter("jobId", jobId);
            countQuery.setParameter("jobId", jobId);
        }
        if (belongJobId != null) {
            if (belongJobId == -1) {
            } else {
                query.setParameter("belongJobId", belongJobId);
                countQuery.setParameter("belongJobId", belongJobId);
            }
        }
        if (StringUtils.isNotBlank(org)) {
        	query.setParameter("org", org);
            countQuery.setParameter("org", org);
        }
        if (bookmarkedJobId != null) {
            if (bookmarkedJobId == -1) {
            } else {
                query.setParameter("bookmarkedJobId", bookmarkedJobId);
                countQuery.setParameter("bookmarkedJobId", bookmarkedJobId);
            }
        }

        if (StringUtils.isNotBlank(status)) {
        	String[] statusArray = status.split(",");
        	List<String> statusList =  Arrays.asList(statusArray);
            query.setParameter("status", statusList);
            countQuery.setParameter("status", statusList);
        }


        if (StringUtils.isNotBlank(process_stage_code)) {
        	process_stage_code = process_stage_code.replace(">", "").replace("<", "");
            query.setParameter("process_stage_code", process_stage_code);
            countQuery.setParameter("process_stage_code", process_stage_code);
        }

//        if (StringUtils.isNotBlank(process_stage_code1)) {
//        	query.setParameter("process_stage_code1", process_stage_code1);
//        	countQuery.setParameter("process_stage_code1", process_stage_code1);
//        }

        List<ApplicationVO> list = EntityUtils.castEntity(query.getResultList(), ApplicationVO.class);
        BigInteger count = (BigInteger) countQuery.getSingleResult();

        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }


    @Override
    public List<CommonVO> getJobStageHeadCount(Integer dept_pos_detail_id) {
        String sql = "select CAST(process_stage as SIGNED) as process_stage, count(1) as num from  applicant_position where dept_pos_detail_id = :dept_pos_detail_id group by process_stage ORDER BY process_stage";
        Query query = em.createNativeQuery(sql);
        query.setParameter("dept_pos_detail_id", dept_pos_detail_id);
        return EntityUtils.castEntity(query.getResultList(), CommonVO.class);
    }

    @Override
    public List<CommonVO> getApplicationStageProcessTime(Integer applicant_position_id) {
        //stage_action=0 收藏
        String sql = "select 0 as stage, DATE_FORMAT(cr_date,'%Y-%m-%d') as process_time from applicant_position where applicant_pos_id = :applicant_position_id union"
                + "(select t.* from (select CAST(case when app.stage = 10 and app.new_stage = 12 then 12 when app.new_stage = 11 then 11 else app.stage end as SIGNED) as stage, DATE_FORMAT(app.last_modify_date,'%Y-%m-%d') as process_time from applicant_position_process app where app.applicant_position_id = :applicant_position_id and app.new_stage>app.stage and app.stage_action!=0 and app.stage < (select process_stage from applicant_position where applicant_pos_id = app.applicant_position_id) order by app.last_modify_date desc) t GROUP BY t.stage ORDER BY t.stage)";
        Query query = em.createNativeQuery(sql);
        query.setParameter("applicant_position_id", applicant_position_id);
        return EntityUtils.castEntity(query.getResultList(), CommonVO.class);
    }

    @Override
    public List<ApplicationHistoryVO> getApplicationHistory(Integer applicantInfoId) {
        //stage_action=0 收藏
        String sql = "select ap.applicant_pos_id, DATE_FORMAT(ap.cr_date,'%Y-%m-%d') as cr_date, dpp.job_code, case when ap.process_stage=11 then '應聘失敗' when ap.process_stage=12 then '已入職' else '進行中' end as result, " +
                "IFNULL((select case when app.new_stage=11 then app.stage_action_remark else '' end from applicant_position_process app where app.applicant_position_id = ap.applicant_pos_id and app.stage_action!=0 order by app.applicant_position_process_id desc limit 1),'') as remark " +
                "from applicant_position ap, department_position_detail dpp where ap.dept_pos_detail_id = dpp.dept_pos_detail_id and ap.applicant_info_id = :applicantInfoId order by ap.cr_date ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("applicantInfoId", applicantInfoId);
        return EntityUtils.castEntity(query.getResultList(), ApplicationHistoryVO.class);
    }

    @Override
    public NoticeVO getNoticeInformation(Integer applicant_pos_id) {
        //stage_action=0 收藏
        String sql = "select t.*, dp.job_title, o.code from ( " +
        		"select ai.cn_f_name, ai.cn_l_name,case when ai.gender = 'M' then '先生' else '女士' end solution, ai.area_code, ai.mobile, ai.email_address,  " +
        		"DATE_FORMAT(STR_TO_DATE(ap.interview_time, '%m/%d/%Y %H:%i'),'%Y年%m月%d日 (%W) %p%h:%i') as interview_time,  " +
        		"(select mr.meeting_room_name from meeting_room mr where ap.meeting_room_id = mr.meeting_room_id) as meeting_room, " +
        		"case when ap.bookmarked_job_id is null then ap.dept_pos_detail_id else ap.bookmarked_job_id end as job_id " +
        		"from applicant_position ap, applicant_info ai " +
        		"where ap.applicant_info_id = ai.applicant_info_id  " +
        		"and ap.applicant_pos_id = :applicant_pos_id  " +
        		") as t, department_position_detail dpd, department_position dp, organization o  " +
        		"where t.job_id = dpd.dept_pos_detail_id and dpd.dept_pos_id = dp.dept_pos_id and dp.org_id = o.org_id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("applicant_pos_id", applicant_pos_id);
        List<NoticeVO> list = EntityUtils.castEntity(query.getResultList(), NoticeVO.class);
        if (list.size() > 0) return list.get(0);
        else return null;
    }


    @Override
    public Page<ApplicantionRecordReportVO> page4ApplicantReport(int start, int length, ApplicantionRecordReportVO condition, String sidx, String sord) {

        String sql =
//                "select m2.CHN_DESC as 'recruitment_source_1', ap.source_ref as 'recruitment_source_2', ai.application_org as 'application_org', dpd.job_code as 'job_code', dp.job_title as 'job_title', ap.applicant_pos_id as 'applicant_pos_id', ap.cr_date as 'applicant_cr_date', d2.description as 'applicant_dept_name', d2.code as 'applicant_dept_code', ai.applicant_info_id as 'applicant_info_id', table_count1.count_apply_times as 'count_apply_times', ai.blacklisted as 'is_blacklisted', 'null' as 'recruitment_form', ai.cn_l_name 'cn_last_name', ai.cn_f_name 'cn_first_name', ai.en_f_name 'en_first_name', ai.en_l_name 'en_last_name', m3.CHN_DESC as 'id_card_type_name', ai.id_card_number as 'id_card_number', ai.dob as 'date_of_birth', (year(curdate()) - year(ai.dob)) + (if(STR_TO_DATE(CONCAT(year(CURDATE()), '-', month(ai.dob), '-', day(ai.dob)) , '%Y-%c-%e') > CURDATE(), 0, 1)) as 'age', ai.gender as 'gender', ai.martial_status as 'martial_status', ai.area_code as 'area_code_1' , ai.mobile as 'mobile_1', ai.contact_phone_area_code as 'area_code_2', ai.contact_phone as 'mobile_2', ai.email_address as 'email_address', ai.address_1 as 'address_1', ai.address_2 as 'address_2', ai.address_3 as 'address_3' , e2.`degree` as 'degree' , c2.all_cert as 'all_certification', w2.all_company as 'all_company', 'null' as 'is_onshift', os2 .all_shift_date as 'all_shift_date', table_hr_refferral.last_modify_date as 'hr_refferral_date', 'null' as 'interview_status', 'null' as 'entry_approval', 'null' as 'send_offer', 'null' as 'entry_date', (case when ap.bookmarked_job_id is null then d2.code else d3.code end) as 'appointment_dept_code', (case when ap.bookmarked_job_id is null then dp.job_title else dp2.job_title end) as 'appointment_job_title', 'null' as 'appointment_form', table_archive.stage_action_remark as 'archived_result' from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) inner join message m2 on (ap.source_id = m2.ID) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join department d2 on (dp.department_id = d2.department_Id) inner join ( select ap.applicant_info_id , count(ap.applicant_info_id) as count_apply_times from applicant_position ap group by ap.applicant_info_id) table_count1 on (ap.applicant_info_id = table_count1.applicant_info_id) left join ( select * from education group by applicant_info_id) e2 on ap.applicant_info_id = e2.applicant_info_id left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join ( select *, group_concat(company_name) as all_company from working_experience group by applicant_info_id ) w2 on ap.applicant_info_id = w2.applicant_info_id left join ( select *, group_concat( concat_ws(' ', (case os.dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end ), concat(os.from_date, '-', os.from_date)) ) as all_shift_date from on_shift os group by applicant_info_id ) os2 on ap.applicant_info_id = os2.applicant_info_id left join ( select applicant_position_process_id , last_modify_date from applicant_position_process where stage = 1 and new_stage = 2 ) table_hr_refferral on app.applicant_position_process_id = table_hr_refferral.applicant_position_process_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_hr_refferral.applicant_position_process_id left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) left join department d3 on (dp2.department_id = d3.department_Id) where ap.process_stage is not null ";
//"select ap.applicant_pos_id as 'applicant_pos_id', ai.application_org as 'application_org', dpd.job_code as 'job_code', ap.cr_date as 'applicant_cr_date', dp.department_code as 'applicant_dept_name', dp.department_code as 'applicant_dept_code', dp.job_title as 'job_title', m2.CHN_DESC as 'recruitment_source_1', asd.chn_desc as 'recruitment_source_3', ap.source_ref as 'recruitment_source_2', ai.blacklisted as 'is_blacklisted', ai.cn_l_name 'cn_last_name', ai.cn_f_name 'cn_first_name', ai.en_l_name 'en_last_name', ai.en_f_name 'en_first_name', m3.CHN_DESC as 'id_card_type_name', ai.id_card_number as 'id_card_number', TIMESTAMPDIFF(year, ai.dob, CURDATE()) as 'age', ai.gender as 'gender', ai.martial_status as 'martial_status', ai.address_1 as 'address_1', ai.address_2 as 'address_2', ai.address_3 as 'address_3' , ai.email_address as 'email_address', ai.area_code as 'area_code_1' , ai.mobile as 'mobile_1', rel.name as 'relative_name', e2.`degree` as 'degree' , e2.organization_name as 'school_name', e2.major as 'school_major', c2.all_cert as 'all_certification', w2.company_name as 'hi_company_name', w2.`position` as 'hi_position', w2.`pay_method` as 'hi_pay_method', w2.`currency` as 'hi_currency', w2.`salary` as 'hi_salary', ap.expected_salary_type as 'expected_salary_type', ap.expected_salary as 'expected_salary', os2 .all_shift_date as 'all_shift_date', case ai.employed_before when 'Y' then '是' else '否' end employed_before, case ai.applied_baccount_before when 'Y' then '是' else '否' end applied_baccount_before, case ai.terminated_before when 'Y' then '是' else '否' end terminated_before, case ai.criminal_record when 'Y' then '是' else '否' end criminal_record, ( select m.chn_desc from message m, message_group mg where m.message_group_id = mg.id and mg.description = '招聘流程' and m.code = ap.process_stage) as 'process_stage', table_archive.stage_action_remark as 'archived_result', (case when ap.bookmarked_job_id is null then dp.department_code else dp2.department_code end) as 'appointment_dept_code', (case when ap.bookmarked_job_id is null then dp.job_title else dp2.job_title end) as 'appointment_job_title' from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m2 on (ap.source_id = m2.ID) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) left join applicant_source_detail asd on (ap.applicant_source_detail_id = asd.id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join relative_info rel on (ai.applicant_info_id = rel.applicant_info_id) left join ( select et.applicant_info_id , t1.degree, et.organization_name , et.major from ( select e.applicant_info_id , ( case (MAX(case e.`degree` when '小學畢業或以下' then 1 when '初中' then 2 when '初中畢業' then 3 when '高中' then 4 when '高中畢業' then 5 when '大專/高級文憑' then 6 when '大學/大專(中)' then 7 when '學士' then 8 when '學士以上' then 9 when '小学毕业或以下' then 1 when '初中' then 2 when '初中毕业' then 3 when '高中' then 4 when '高中毕业' then 5 when '大专/高级文凭' then 6 when '大学/大专(中)' then 7 when '学士' then 8 when '学士以上' then 9 else 0 end ) ) when 1 then '小學畢業或以下' when 2 then '初中' when 3 then '初中畢業' when 4 then '高中' when 5 then '高中畢業' when 6 then '大專/高級文憑' when 7 then '大學/大專(中)' when 8 then '學士' when 9 then '學士以上' when 0 then '其他舊學歷' else '舊資料' end ) as 'degree' from education e group by e.applicant_info_id) t1 , education et where t1.applicant_info_id = et.applicant_info_id and t1.degree = et.`degree`) e2 on ap.applicant_info_id = e2.applicant_info_id left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join working_experience w2 on (ai.applicant_info_id = w2.applicant_info_id and w2.experience_id in (select min(w3.experience_id) from working_experience w3 where ai.applicant_info_id = w3.applicant_info_id)) left join ( select *, group_concat( concat_ws(' ', (case os.dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end ), concat(os.from_date, '-', os.from_date)) ) as all_shift_date from on_shift os group by applicant_info_id ) os2 on ap.applicant_info_id = os2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) where ap.process_stage is not null ";
"select ap.applicant_pos_id as 'applicant_pos_id', ai.application_org as 'application_org', dpd.job_code as 'job_code', ap.cr_date as 'applicant_cr_date', dp.department_code as 'applicant_dept_name', dp.department_code as 'applicant_dept_code', dp.job_title as 'job_title', m2.CHN_DESC as 'recruitment_source_1', asd.chn_desc as 'recruitment_source_3', ap.source_ref as 'recruitment_source_2', ai.blacklisted as 'is_blacklisted', ai.cn_l_name 'cn_last_name', ai.cn_f_name 'cn_first_name', ai.en_l_name 'en_last_name', ai.en_f_name 'en_first_name', m3.CHN_DESC as 'id_card_type_name', ai.id_card_number as 'id_card_number', TIMESTAMPDIFF(year, ai.dob, CURDATE()) as 'age', ai.gender as 'gender', ai.martial_status as 'martial_status', ai.address_1 as 'address_1', ai.address_2 as 'address_2', ai.address_3 as 'address_3' , ai.email_address as 'email_address', ai.area_code as 'area_code_1' , ai.mobile as 'mobile_1', rel.name as 'relative_name', e2.`degree` as 'degree' , e2.organization_name as 'school_name', e2.major as 'school_major', c2.all_cert as 'all_certification', w2.company_name as 'hi_company_name', w2.`position` as 'hi_position', w2.`pay_method` as 'hi_pay_method', w2.`currency` as 'hi_currency', w2.`salary` as 'hi_salary', ap.expected_salary_type as 'expected_salary_type', ap.expected_salary as 'expected_salary', os2 .all_shift_date as 'all_shift_date', case ai.employed_before when 'Y' then '是' else '否' end employed_before, case ai.applied_baccount_before when 'Y' then '是' else '否' end applied_baccount_before, case ai.terminated_before when 'Y' then '是' else '否' end terminated_before, case ai.criminal_record when 'Y' then '是' else '否' end criminal_record, ( select m.chn_desc from message m, message_group mg where m.message_group_id = mg.id and mg.description = '招聘流程' and m.code = ap.process_stage) as 'process_stage', table_archive.stage_action_remark as 'archived_result', (case when ap.bookmarked_job_id is null then dp.department_code else dp2.department_code end) as 'appointment_dept_code', (case when ap.bookmarked_job_id is null then dp.job_title else dp2.job_title end) as 'appointment_job_title' from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m2 on (ap.source_id = m2.ID) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) left join applicant_source_detail asd on (ap.applicant_source_detail_id = asd.id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join relative_info rel on (ai.applicant_info_id = rel.applicant_info_id) left join education e2 on (ai.applicant_info_id = e2.applicant_info_id and e2.education_id in ( select min(e3.education_id) from education e3 where ai.applicant_info_id = e3.applicant_info_id)) left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join working_experience w2 on (ai.applicant_info_id = w2.applicant_info_id and w2.experience_id in ( select min(w3.experience_id) from working_experience w3 where ai.applicant_info_id = w3.applicant_info_id)) left join ( select *, group_concat( concat_ws(' ', (case os.dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end ), concat(os.from_date, '-', os.from_date)) ) as all_shift_date from on_shift os group by applicant_info_id ) os2 on ap.applicant_info_id = os2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) where ap.process_stage is not null ";
        String countsql =
//                "select count(ap.applicant_pos_id) from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) inner join message m2 on (ap.source_id = m2.ID) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join department d2 on (dp.department_id = d2.department_Id) inner join ( select ap.applicant_info_id , count(ap.applicant_info_id) as count_apply_times from applicant_position ap group by ap.applicant_info_id) table_count1 on (ap.applicant_info_id = table_count1.applicant_info_id) left join ( select * from education group by applicant_info_id) e2 on ap.applicant_info_id = e2.applicant_info_id left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join ( select *, group_concat(company_name) as all_company from working_experience group by applicant_info_id ) w2 on ap.applicant_info_id = w2.applicant_info_id left join ( select *, group_concat( concat_ws(' ', (case os.dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end ), concat(os.from_date, '-', os.from_date)) ) as all_shift_date from on_shift os group by applicant_info_id ) os2 on ap.applicant_info_id = os2.applicant_info_id left join ( select applicant_position_process_id , last_modify_date from applicant_position_process where stage = 1 and new_stage = 2 ) table_hr_refferral on app.applicant_position_process_id = table_hr_refferral.applicant_position_process_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_hr_refferral.applicant_position_process_id left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) left join department d3 on (dp2.department_id = d3.department_Id) where ap.process_stage is not null ";
//"select count(ap.applicant_pos_id) from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m2 on (ap.source_id = m2.ID) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join relative_info rel on (ai.applicant_info_id = rel.applicant_info_id) left join ( select et.applicant_info_id , t1.degree, et.organization_name , et.major from ( select e.applicant_info_id , ( case (MAX(case e.`degree` when '小學畢業或以下' then 1 when '初中' then 2 when '初中畢業' then 3 when '高中' then 4 when '高中畢業' then 5 when '大專/高級文憑' then 6 when '大學/大專(中)' then 7 when '學士' then 8 when '學士以上' then 9 when '小学毕业或以下' then 1 when '初中' then 2 when '初中毕业' then 3 when '高中' then 4 when '高中毕业' then 5 when '大专/高级文凭' then 6 when '大学/大专(中)' then 7 when '学士' then 8 when '学士以上' then 9 else 0 end ) ) when 1 then '小學畢業或以下' when 2 then '初中' when 3 then '初中畢業' when 4 then '高中' when 5 then '高中畢業' when 6 then '大專/高級文憑' when 7 then '大學/大專(中)' when 8 then '學士' when 9 then '學士以上' when 0 then '其他舊學歷' else '舊資料' end ) as 'degree' from education e group by e.applicant_info_id) t1 , education et where t1.applicant_info_id = et.applicant_info_id and t1.degree = et.`degree`) e2 on ap.applicant_info_id = e2.applicant_info_id left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join working_experience w2 on (ai.applicant_info_id = w2.applicant_info_id and w2.experience_id in (select min(w3.experience_id) from working_experience w3 where ai.applicant_info_id = w3.applicant_info_id)) left join ( select *, group_concat( concat_ws(' ', (case os.dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end ), concat(os.from_date, '-', os.from_date)) ) as all_shift_date from on_shift os group by applicant_info_id ) os2 on ap.applicant_info_id = os2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) where ap.process_stage is not null ";
"select count(ap.applicant_pos_id) from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m2 on (ap.source_id = m2.ID) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join relative_info rel on (ai.applicant_info_id = rel.applicant_info_id) left join education e2 on (ai.applicant_info_id = e2.applicant_info_id and e2.education_id in ( select min(e3.education_id) from education e3 where ai.applicant_info_id = e3.applicant_info_id)) left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join working_experience w2 on (ai.applicant_info_id = w2.applicant_info_id and w2.experience_id in ( select min(w3.experience_id) from working_experience w3 where ai.applicant_info_id = w3.applicant_info_id)) left join ( select *, group_concat( concat_ws(' ', (case os.dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end ), concat(os.from_date, '-', os.from_date)) ) as all_shift_date from on_shift os group by applicant_info_id ) os2 on ap.applicant_info_id = os2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) where ap.process_stage is not null ";

        String where = "";


        String sort = " order by ap.cr_date desc";


        Date startDate = condition.getApplicant_postition_start_date();
        Date endDate = condition.getApplicant_postition_end_date();
        if (startDate != null &&
                endDate != null) {
            where += " and date(ap.cr_date) between date(:startDate) and date(:endDate)";
        }


        String recruitmentSource1 = condition.getRecruitmentSource1();
        if (StringUtils.isNotBlank(recruitmentSource1)) {
            where += " and m2.id = :recruitmentSource1";
        }

        String applicationOrg = condition.getApplicationOrg();
        if (StringUtils.isNotBlank(applicationOrg)) {
            where += " and ai.application_org = :applicationOrg";
        }

        String departmentCode = condition.getApplicantDepartmentCode();
        if (departmentCode != null) {
            where += " and dp.department_code = :departmentCode";
        }

        String idCardTypeCode = condition.getIdCardTypeName();
        if (StringUtils.isNotBlank(idCardTypeCode)) {
            where += " and m3.code = :idCardTypeCode";
        }

        String archivedResult = condition.getArchivedResult();
        if (StringUtils.isNotBlank(archivedResult)) {
            where += " and table_archive.stage_action_remark = :archivedResult";
        }



      /*  String jobTitle = condition.getJobTitle();
        if (StringUtils.isNotBlank(jobTitle)) {
            where += " and dp.job_title = :jobTitle";
        }*/

       /* String nameLike = condition.getNameLike();
        if (StringUtils.isNotBlank(nameLike)) {
            where += " and (ai.en_f_name like :nameLike||ai.en_l_name like :nameLike||ai.cn_f_name like :nameLike||ai.cn_l_name like :nameLike)";
        }*/
       /* String idCardNumber = condition.getIdCardNumber();
        if (StringUtils.isNotBlank(idCardNumber)) {
            where += " and ai.id_card_number like :idCardNumber";
        }
        String emailAddress = condition.getEmailAddress();
        if (StringUtils.isNotBlank(emailAddress)) {
            where += " and ai.email_address like :emailAddress";
        }*/


        Query query = em.createNativeQuery(sql + where + sort);
        query.setFirstResult(start).setMaxResults(length);
        Query countQuery = em.createNativeQuery(countsql + where);


        if (startDate != null &&
                endDate != null) {
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            countQuery.setParameter("startDate", startDate);
            countQuery.setParameter("endDate", endDate);
        }

        if (StringUtils.isNotBlank(recruitmentSource1)) {
            query.setParameter("recruitmentSource1", recruitmentSource1);
            countQuery.setParameter("recruitmentSource1", recruitmentSource1);
        }
        if (StringUtils.isNotBlank(applicationOrg)) {
            query.setParameter("applicationOrg", applicationOrg);
            countQuery.setParameter("applicationOrg", applicationOrg);
        }
        if (departmentCode != null) {
            query.setParameter("departmentCode", departmentCode);
            countQuery.setParameter("departmentCode", departmentCode);
        }
        if (StringUtils.isNotBlank(idCardTypeCode)) {
            query.setParameter("idCardTypeCode", idCardTypeCode);
            countQuery.setParameter("idCardTypeCode", idCardTypeCode);
        }
        if (StringUtils.isNotBlank(archivedResult)) {
            query.setParameter("archivedResult", archivedResult);
            countQuery.setParameter("archivedResult", archivedResult);
        }

      /*  if (StringUtils.isNotBlank(jobTitle)) {
            query.setParameter("jobTitle", jobTitle);
            countQuery.setParameter("jobTitle", jobTitle);
        }*/
     /*   if (StringUtils.isNotBlank(nameLike)) {
            query.setParameter("nameLike", "%" + nameLike + "%");
            countQuery.setParameter("nameLike", "%" + nameLike + "%");
        }*/
      /*  if (StringUtils.isNotBlank(idCardNumber)) {
            query.setParameter("idCardNumber", "%" + idCardNumber + "%");
            countQuery.setParameter("idCardNumber", "%" + idCardNumber + "%");
        }
        if (StringUtils.isNotBlank(emailAddress)) {
            query.setParameter("emailAddress", "%" + emailAddress + "%");
            countQuery.setParameter("emailAddress", "%" + emailAddress + "%");
        }*/

        List<ApplicantionRecordReportVO> list = EntityUtils.mapping(query.getResultList(), ApplicantionRecordReportVO.class, "page4ApplicantReport");

        BigInteger count = (BigInteger) countQuery.getSingleResult();

//        System.err.println(list);
        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }

    @Override
    public Page<CandidateInformationReportVO> page4CandidateInformationReport(int start, int length, CandidateInformationReportVO condition, String sidx, String sord) {

        String sql =
//                "select ai.cn_l_name 'cn_last_name', ai.cn_f_name 'cn_first_name', ai.en_l_name 'en_last_name', ai.en_f_name 'en_first_name', m3.CHN_DESC as 'id_card_type_name', ai.id_card_number as 'id_card_number', ai.dob as 'date_of_birth', TIMESTAMPDIFF(YEAR,ai.dob,CURDATE()) as 'age', ai.gender as 'gender', ai.martial_status as 'martial_status', ai.address_1 as 'address_1', ai.address_2 as 'address_2', ai.address_3 as 'address_3' , ai.area_code as 'area_code_1' , ai.mobile as 'mobile_1', e2.`degree` as 'degree' , e2.organization_name as 'school_name', e2.major as 'school_major', c2.all_cert as 'all_certification', w2.all_company as 'all_company', case ai.employed_before when 'Y' then '是' else '否' end employed_before, case ai.applied_baccount_before when 'Y' then '是' else '否' end applied_baccount_before, case ai.terminated_before when 'Y' then '是' else '否' end terminated_before, case ai.criminal_record when 'Y' then '是' else '否' end criminal_record, ai.blacklisted as 'is_blacklisted', table_count1.count_apply_times as 'count_apply_times', group_concat( concat(ap.cr_date,'-',dp.job_title,'-',table_archive.stage_action_remark) SEPARATOR '||' ) as 'application_history' from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join ( select ap.applicant_info_id , count(ap.applicant_info_id) as count_apply_times from applicant_position ap group by ap.applicant_info_id) table_count1 on (ap.applicant_info_id = table_count1.applicant_info_id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join (select et.applicant_info_id , t1.degree, et.organization_name , et.major from ( select e.applicant_info_id , ( case (MAX(case e.`degree` when '小學畢業或以下' then 1 when '初中' then 2 when '初中畢業' then 3 when '高中' then 4 when '高中畢業' then 5 when '大專/高級文憑' then 6 when '大學/大專(中)' then 7 when '學士' then 8 when '學士以上' then 9 when '小学毕业或以下' then 1 when '初中' then 2 when '初中毕业' then 3 when '高中' then 4 when '高中毕业' then 5 when '大专/高级文凭' then 6 when '大学/大专(中)' then 7 when '学士' then 8 when '学士以上' then 9 else 0 end ) ) when 1 then '小學畢業或以下' when 2 then '初中' when 3 then '初中畢業' when 4 then '高中' when 5 then '高中畢業' when 6 then '大專/高級文憑' when 7 then '大學/大專(中)' when 8 then '學士' when 9 then '學士以上' when 0 then '其他舊學歷' else '舊資料' end ) as 'degree' from education e group by e.applicant_info_id) t1 , education et where t1.applicant_info_id = et.applicant_info_id and t1.degree = et.`degree`) e2 on ap.applicant_info_id = e2.applicant_info_id left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join ( select applicant_info_id, group_concat(company_name, concat('-', position), concat('-', pay_method), concat(' ', currency), concat(' ', salary) SEPARATOR '||' ) as all_company from working_experience group by applicant_info_id ) w2 on ap.applicant_info_id = w2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id where ap.process_stage is not null ";
"select ai.cn_l_name 'cn_last_name', ai.cn_f_name 'cn_first_name', ai.en_l_name 'en_last_name', ai.en_f_name 'en_first_name', m3.CHN_DESC as 'id_card_type_name', ai.id_card_number as 'id_card_number', ai.dob as 'date_of_birth', TIMESTAMPDIFF(year, ai.dob, CURDATE()) as 'age', ai.gender as 'gender', ai.martial_status as 'martial_status', ai.address_1 as 'address_1', ai.address_2 as 'address_2', ai.address_3 as 'address_3' , ai.area_code as 'area_code_1' , ai.mobile as 'mobile_1', e2.`degree` as 'degree' , e2.organization_name as 'school_name', e2.major as 'school_major', c2.all_cert as 'all_certification', w2.all_company as 'all_company', case ai.employed_before when 'Y' then '是' else '否' end employed_before, case ai.applied_baccount_before when 'Y' then '是' else '否' end applied_baccount_before, case ai.terminated_before when 'Y' then '是' else '否' end terminated_before, case ai.criminal_record when 'Y' then '是' else '否' end criminal_record, ai.blacklisted as 'is_blacklisted', table_count1.count_apply_times as 'count_apply_times', group_concat( concat(ap.cr_date, '-', dp.job_title, '-', table_archive.stage_action_remark) separator '||' ) as 'application_history' from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join ( select ap.applicant_info_id , count(ap.applicant_info_id) as count_apply_times from applicant_position ap group by ap.applicant_info_id) table_count1 on (ap.applicant_info_id = table_count1.applicant_info_id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join education e2 on (ai.applicant_info_id = e2.applicant_info_id and e2.education_id in ( select min(e3.education_id) from education e3 where ai.applicant_info_id = e3.applicant_info_id)) left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join ( select applicant_info_id, group_concat(company_name, concat('-', position), concat('-', pay_method), concat(' ', currency), concat(' ', salary) separator '||' ) as all_company from working_experience group by applicant_info_id ) w2 on ap.applicant_info_id = w2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id where ap.process_stage is not null ";
        String sqlGroupBy =
                " group by ai.applicant_info_id ";
        String countsql =
//                "select count(1) from ( select ai.applicant_info_id from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join ( select ap.applicant_info_id , count(ap.applicant_info_id) as count_apply_times from applicant_position ap group by ap.applicant_info_id) table_count1 on (ap.applicant_info_id = table_count1.applicant_info_id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join (select et.applicant_info_id , t1.degree, et.organization_name , et.major from ( select e.applicant_info_id , ( case (MAX(case e.`degree` when '小學畢業或以下' then 1 when '初中' then 2 when '初中畢業' then 3 when '高中' then 4 when '高中畢業' then 5 when '大專/高級文憑' then 6 when '大學/大專(中)' then 7 when '學士' then 8 when '學士以上' then 9 when '小学毕业或以下' then 1 when '初中' then 2 when '初中毕业' then 3 when '高中' then 4 when '高中毕业' then 5 when '大专/高级文凭' then 6 when '大学/大专(中)' then 7 when '学士' then 8 when '学士以上' then 9 else 0 end ) ) when 1 then '小學畢業或以下' when 2 then '初中' when 3 then '初中畢業' when 4 then '高中' when 5 then '高中畢業' when 6 then '大專/高級文憑' when 7 then '大學/大專(中)' when 8 then '學士' when 9 then '學士以上' when 0 then '其他舊學歷' else '舊資料' end ) as 'degree' from education e group by e.applicant_info_id) t1 , education et where t1.applicant_info_id = et.applicant_info_id and t1.degree = et.`degree`) e2 on ap.applicant_info_id = e2.applicant_info_id left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join ( select applicant_info_id, group_concat(company_name, concat('-', position), concat('-', pay_method), concat(' ', currency), concat(' ', salary) SEPARATOR '||' ) as all_company from working_experience group by applicant_info_id ) w2 on ap.applicant_info_id = w2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id where ap.process_stage is not null ";
"select count(1) from ( select ai.applicant_info_id from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join message m3 on (ai.id_type_id = m3.ID) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join ( select ap.applicant_info_id , count(ap.applicant_info_id) as count_apply_times from applicant_position ap group by ap.applicant_info_id) table_count1 on (ap.applicant_info_id = table_count1.applicant_info_id) left join ( select app1.* from applicant_position_process app1 left join applicant_position_process app2 on (app1.applicant_position_id = app2.applicant_position_id and app1.applicant_position_process_id < app2.applicant_position_process_id) where app2.applicant_position_process_id is null) app on (ap.applicant_pos_id = app.applicant_position_id) left join education e2 on (ai.applicant_info_id = e2.applicant_info_id and e2.education_id in ( select min(e3.education_id) from education e3 where ai.applicant_info_id = e3.applicant_info_id)) left join ( select *, group_concat(cert_name) as all_cert from certification group by applicant_info_id ) c2 on ap.applicant_info_id = c2.applicant_info_id left join ( select applicant_info_id, group_concat(company_name, concat('-', position), concat('-', pay_method), concat(' ', currency), concat(' ', salary) separator '||' ) as all_company from working_experience group by applicant_info_id ) w2 on ap.applicant_info_id = w2.applicant_info_id left join ( select applicant_position_process_id , stage_action_remark from applicant_position_process where stage_action = 4) table_archive on app.applicant_position_process_id = table_archive.applicant_position_process_id where ap.process_stage is not null ";
        String countsqlGroupBy =
                " group by ai.applicant_info_id ) ttt ";


        String where = "";
        String sort = "";


        String idCardTypeName = condition.getIdCardTypeName();

        if (StringUtils.isNotBlank(idCardTypeName)) {
            where +=" and m3.code = :idCardTypeName";
        }
        String degree = condition.getDegree();
        if (StringUtils.isNotBlank(degree)) {
            switch (degree) {
                case "10":where +=" and e2.`degree` in ('小學畢業或以下','小学毕业或以下')";break;
                case "20":where +=" and e2.`degree` in ('初中')";break;
                case "30":where +=" and e2.`degree` in ('初中畢業','初中毕业')";break;
                case "40":where +=" and e2.`degree` in ('高中','高中/中专')";break;
                case "50":where +=" and e2.`degree` in ('高中畢業','高中/中专毕业')";break;
                case "60":where +=" and e2.`degree` in ('大專/高級文憑','大专')";break;
//                case "70":where +=" and e2.`degree` in ('大學/大專(中)','大学/大专(中)')";break;
                case "80":where +=" and e2.`degree` in ('學士','本科')";break;
                case "90":where +=" and e2.`degree` in ('學士以上','本科以上')";break;
                default:throw new IllegalArgumentException("degree 參數異常");
            }
        }
        String is_blacklisted_str = condition.getIs_blacklisted_str();
        if (StringUtils.isNotBlank(is_blacklisted_str)) {
            if ("1".equals(is_blacklisted_str)) {
                where += " and ai.blacklisted = 1 ";
            } else if ("0".equals(is_blacklisted_str)) {
                where += " and (ai.blacklisted is null or ai.blacklisted = 0 ) ";
            } else {
                throw new IllegalArgumentException("is_blacklisted_str 參數異常");
            }
        }


        Query query = em.createNativeQuery(sql + where + sqlGroupBy + sort);
        query.setFirstResult(start).setMaxResults(length);
        Query countQuery = em.createNativeQuery(countsql + where + countsqlGroupBy);

        if (StringUtils.isNotBlank(idCardTypeName)) {
            query.setParameter("idCardTypeName", idCardTypeName);
            countQuery.setParameter("idCardTypeName", idCardTypeName);
        }



        List<CandidateInformationReportVO> list = EntityUtils.mapping(query.getResultList(), CandidateInformationReportVO.class, "page4CandidateInformationReport");

        BigInteger count = (BigInteger) countQuery.getSingleResult();

//        System.err.println(list);
        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }

    @Override
    public JSONArray findJobApplicationStatistics(ApplicationInterviewStatusSummaryVO condition) {
        Date startDate = condition.getStartDate();
        Date endDate = condition.getEndDate();
        String applicationOrg = condition.getApplicationOrg();

        List<String> dateList = DateBetweenUtil.getDateListBetween(startDate, endDate);

        StringBuilder sqlDateArr = new StringBuilder();
        for (String dateStr : dateList) {
            sqlDateArr.append("sum(if(t_hang.applicant_cr_date = '").append(dateStr).append("', t_hang.coun, 0)) as '").append(dateStr).append("',");
        }


        String sql1 =
                "select t_hang.job_code, t_hang.job_title, concat(t_hang.dept_code , ' ', t_hang.dept_name)as 'dept_name', ";

        String sql2 =
                "sum(t_hang.coun) as 'total_hang' from ( select ai.application_org , date_format(ap.cr_date , '%Y-%m-%d') as 'applicant_cr_date', dp.department_code , dp.department_code as 'dept_name', dp.department_code as 'dept_code', ap.dept_pos_detail_id, dpd.job_code as 'job_code', dp.job_title as 'job_title', count(*) coun from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) where ap.process_stage is not null and ai.application_org = :application_org and date(ap.cr_date) between date(:startDate) and date(:endDate) group by date_format(ap.cr_date, '%Y-%m-%d'), dp.department_code, ap.dept_pos_detail_id)t_hang group by t_hang.dept_pos_detail_id union select null as job_code, null as job_title, 'TOTAL' as dept_name, ";

        String sql3 =
                "sum(t_hang.coun) from ( select ai.application_org , date_format(ap.cr_date , '%Y-%m-%d') as 'applicant_cr_date', dp.department_code , dp.department_code as 'dept_name', dp.department_code as 'dept_code', ap.dept_pos_detail_id, dpd.job_code as 'job_code', dp.job_title as 'job_title', count(*) coun from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) where ap.process_stage is not null and ai.application_org = :application_org and date(ap.cr_date) between date(:startDate) and date(:endDate) group by date_format(ap.cr_date, '%Y-%m-%d'), dp.department_code, ap.dept_pos_detail_id)t_hang ";

        Query query = em.createNativeQuery(sql1 + sqlDateArr.toString() + sql2 + sqlDateArr.toString() + sql3);

        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("application_org", applicationOrg);

        List<Object> orginalResultList = query.getResultList();
        return new JSONArray(orginalResultList);


    }


    @Override
    public JSONArray findInterviewsStatistics(ApplicationInterviewStatusSummaryVO condition) {
        Date startDate = condition.getStartDate();
        Date endDate = condition.getEndDate();
        String applicationOrg = condition.getApplicationOrg();
        String stage = condition.getStage();

//        String sql =

        List<String> dateList = DateBetweenUtil.getDateListBetween(startDate, endDate);
//        System.err.println(dateList);
        StringBuilder sqlDateArr = new StringBuilder();
        for (String dateStr : dateList) {
            sqlDateArr.append("sum(if(t_hang.applicant_cr_date = '").append(dateStr).append("', t_hang.coun, 0)) as '").append(dateStr).append("',");
        }

//        System.err.println(sqlDateArr.toString());

        String sql1 =
                "select t_hang.job_code, t_hang.job_title, concat(t_hang.dept_code , ' ', t_hang.dept_name)as 'dept_name', ";

        String sql2 =
"sum(t_hang.coun) as 'total_hang' from (select ai.application_org , date_format(ap.cr_date , '%Y-%m-%d') as 'applicant_cr_date', dp.department_code , (case when ap.bookmarked_job_id is null then dp.department_code else dp2.department_code end) as 'dept_name', (case when ap.bookmarked_job_id is null then dp.department_code else dp2.department_code end) as 'dept_code', ap.dept_pos_detail_id, (case when ap.bookmarked_job_id is null then dpd.job_code else dpd2.job_code end) as 'job_code', (case when ap.bookmarked_job_id is null then dp.job_title else dp2.job_title end) as 'job_title', count(*) coun from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join applicant_position_process app11 on (ap.applicant_pos_id = app11.applicant_position_id) left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) where ap.process_stage is not null and ai.application_org = :application_org and app11.stage = :stage and app11.stage_action in (1,2) and date(app11.last_modify_date) between date(:startDate) and date(:endDate) group by date_format(ap.cr_date, '%Y-%m-%d'), dp.department_code, ap.dept_pos_detail_id)t_hang group by t_hang.dept_pos_detail_id union select null as job_code, null as job_title, 'TOTAL' as dept_name, ";

        String sql3 =
"sum(t_hang.coun) from (select ai.application_org , date_format(ap.cr_date , '%Y-%m-%d') as 'applicant_cr_date', dp.department_code , (case when ap.bookmarked_job_id is null then dp.department_code else dp2.department_code end) as 'dept_name', (case when ap.bookmarked_job_id is null then dp.department_code else dp2.department_code end) as 'dept_code', ap.dept_pos_detail_id, (case when ap.bookmarked_job_id is null then dpd.job_code else dpd2.job_code end) as 'job_code', (case when ap.bookmarked_job_id is null then dp.job_title else dp2.job_title end) as 'job_title', count(*) coun from applicant_position ap inner join applicant_info ai on (ap.applicant_info_id = ai.applicant_info_id) inner join department_position_detail dpd on (ap.dept_pos_detail_id = dpd.dept_pos_detail_id) inner join department_position dp on (dpd.dept_pos_id = dp.dept_pos_id) inner join applicant_position_process app11 on (ap.applicant_pos_id = app11.applicant_position_id) left join department_position_detail dpd2 on (ap.bookmarked_job_id = dpd2.dept_pos_detail_id) left join department_position dp2 on (dpd2.dept_pos_id = dp2.dept_pos_id) where ap.process_stage is not null and ai.application_org = :application_org and app11.stage = :stage and app11.stage_action in (1,2) and date(app11.last_modify_date) between date(:startDate) and date(:endDate) group by date_format(ap.cr_date, '%Y-%m-%d'), dp.department_code, ap.dept_pos_detail_id)t_hang ";
//        String where = "";
//        String sort = "";


        Query query = em.createNativeQuery(sql1 + sqlDateArr.toString() + sql2 + sqlDateArr.toString() + sql3);

        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("application_org", applicationOrg);
        query.setParameter("stage", stage);


        List<Object> orginalResultList = query.getResultList();
        return new JSONArray(orginalResultList);


    }

    @Override
    public List<MailVO> findApplicationMailInfo(Integer[] applicationIds){

        String sql = "select t.*, dp.job_title, group_CONCAT(u.EMAIL) as receiver from (select case when bookmarked_job_id is null then dept_pos_detail_id else bookmarked_job_id end as job_id, count(1) as application_count  " +
        		"from applicant_position where applicant_pos_id in (:applicationIds) group by job_id ) t, department_position_detail dpd, recruitment_group_member rgm, department_position dp, user u " +
        		"where t.job_id = dpd.dept_pos_detail_id and dpd.recruitment_group_id = rgm.recruitment_group_id and dpd.dept_pos_id = dp.dept_pos_id and rgm.employee_id = u.USER_NAME " +
        		"group by t.job_id ";
        List<Integer> applicationIdList = Arrays.asList(applicationIds);
        Query query = em.createNativeQuery(sql);
        query.setParameter("applicationIds", applicationIdList);
        List<MailVO> list = EntityUtils.castEntity(query.getResultList(), MailVO.class);
        return list;
    }

}
