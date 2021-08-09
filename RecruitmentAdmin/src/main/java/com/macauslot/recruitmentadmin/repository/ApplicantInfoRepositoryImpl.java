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

import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.ApplicantInfoVO;

public class ApplicantInfoRepositoryImpl implements ApplicantInfoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    /**
     * GetApplicantJobDetail
     */
    @Override
    public Page<ApplicantInfoVO> searchApplicant(int start, int length, ApplicantInfoVO condition, String sidx, String sord) {

        /*String sql = "select app.applicant_info_id, CONCAT(app.en_f_name, ' ',  app.en_l_name) as 'Applicant_Name', app.id_card_number, app.gender, case app.area when 'CN' then '中國大陸' when 'MO' then '澳門' when 'HK' then '香港' else '其它' end as area, CONCAT(IFNULL(e.organization_name,'N/A'), ' - ', IFNULL(e.major,'N/A')) as 'education', app.blacklisted, app.status, app.application_org "
        			+ " from applicant_info app"
        			+ " left join (select * from education group by applicant_info_id) e on app.applicant_info_id = e.applicant_info_id where 1=1 ";
*/
        String sql =
"select app.applicant_info_id, CONCAT(app.en_f_name, ' ', app.en_l_name) as 'Applicant_Name', m.chn_desc, app.id_card_number, app.gender, case app.area when 'CN' then '中國大陸' when 'MO' then '澳門' when 'HK' then '香港' else '其它' end as area, e.`degree`, IFNULL(e.organization_name, 'N/A'), IFNULL(e.major, 'N/A'), app.blacklisted, app.status, app.application_org from applicant_info app left join education e on (app.applicant_info_id = e.applicant_info_id and e.education_id in ( select min(e2.education_id) from education e2 where app.applicant_info_id = e2.applicant_info_id)) left join message m on app.id_type_id = m.id where 1 = 1 ";


        String countsql = "select count(1) from applicant_info app left join education e on (app.applicant_info_id = e.applicant_info_id and e.education_id in (select min(e2.education_id) from education e2 where app.applicant_info_id = e2.applicant_info_id)) where 1=1 ";

        String where = " ";
        String sort = "";

        String applicantName = condition.getApplicantName();
        if (applicantName != null &&applicantName.compareTo("")!=0) {
            where += " and (app.en_f_name like :applicantName ||app.en_l_name like :applicantName ||app.cn_f_name like :applicantName ||app.cn_l_name like :applicantName)";
        }
        String idCardNumber = condition.getIdCardNumber();
        if (idCardNumber != null &&idCardNumber.compareTo("")!=0) {
            where += " and app.id_card_number like :idCardNumber";
        }
        String status = condition.getStatus();
     /*   if (status != null &&status.compareTo("")!=0) {
            where += " and app.status = :status";
        }*/
        if (StringUtils.isNotBlank(status)) {
            if ("Y".equalsIgnoreCase(status)) {
                where += " and app.status in ('Y','O')";
            } else if ("H".equalsIgnoreCase(status)) {
                where += " and app.status in ('H')";
            } else {
                throw new IllegalArgumentException("param status error");
            }
        }
        else {
            where += " and app.status in ('Y','H','O')";
        }



        String degree = condition.getDegree();
        if (StringUtils.isNotBlank(degree)) {
            switch (degree) {
                case "10":where +=" and e.`degree` in ('小學畢業或以下','小学毕业或以下')";break;
                case "20":where +=" and e.`degree` in ('初中')";break;
                case "30":where +=" and e.`degree` in ('初中畢業','初中毕业')";break;
                case "40":where +=" and e.`degree` in ('高中','高中/中专')";break;
                case "50":where +=" and e.`degree` in ('高中畢業','高中/中专毕业')";break;
                case "60":where +=" and e.`degree` in ('大專/高級文憑','大专')";break;
//                case "70":where +=" and e.`degree` in ('大學/大專(中)','大学/大专(中)')";break;
                case "80":where +=" and e.`degree` in ('學士','本科')";break;
                case "90":where +=" and e.`degree` in ('學士以上','本科以上')";break;
                default:throw new IllegalArgumentException("degree 參數異常");
            }
        }

        String major = condition.getMajor();
        if (StringUtils.isNotBlank(major)) {
            where += " and e.major like :major ";
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
                case "applicantName":
                    sort += " order by CONVERT(Applicant_Name USING gbk)" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "id_type":
                    sort += " order by CONVERT(m.chn_desc USING gbk)" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "idCardNumber":
                    sort += " order by id_Card_Number" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "gender":
                    sort += " order by gender" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "degree":
                    sort += " order by degree" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "area":
                    sort += " order by app.area" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                case "blackListed":
                    sort += " order by app.blackListed" + tmpSord +
                            ",app.applicant_info_id";
                    break;
                default:
                	sort += " order by id_Card_Number, app.applicant_info_id";
                    break;
            }

        }

        Query query = em.createNativeQuery(sql + where + sort);

        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql + where);

        if (applicantName != null &&applicantName.compareTo("")!=0) {
        	  query.setParameter("applicantName", "%"+applicantName+"%");
        	  countQuery.setParameter("applicantName", "%"+applicantName+"%");
        }
        if (idCardNumber != null &&idCardNumber.compareTo("")!=0) {
        	  query.setParameter("idCardNumber", "%"+idCardNumber+"%");
        	  countQuery.setParameter("idCardNumber", "%"+idCardNumber+"%");
        }
      /*  if (status != null &&status.compareTo("")!=0) {
        	query.setParameter("status", status);
      	    countQuery.setParameter("status", status);
        }*/

        if (StringUtils.isNotBlank(major)) {
            query.setParameter("major", "%"+major+"%");
            countQuery.setParameter("major", "%"+major+"%");
        }

        List<ApplicantInfoVO> list = EntityUtils.mapping(query.getResultList(), ApplicantInfoVO.class,"searchApplicant");
        BigInteger count = (BigInteger) countQuery.getSingleResult();

        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }

}
