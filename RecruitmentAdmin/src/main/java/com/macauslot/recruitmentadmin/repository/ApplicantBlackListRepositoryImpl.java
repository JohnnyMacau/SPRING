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
import com.macauslot.recruitmentadmin.vo.ApplicationVO;
import com.macauslot.recruitmentadmin.vo.BlackListVO;

public class ApplicantBlackListRepositoryImpl implements ApplicantBlackListRepositoryCustom {
    @PersistenceContext
    private EntityManager em;


	 public Page<BlackListVO> searchBlackList(int start, int length, String sidx, String sord, BlackListVO blackListVO){
		 String sql = "select l.* from applicant_black_list l where 1=1 ";
		 String countSql = "select count(1) from applicant_black_list l where 1=1 ";
		 String where = "";
		 if(!StringUtils.isBlank(blackListVO.getName())) {
			 where += " and (l.name_en like :name or l.name_cn like :name) ";
		 }
		 if(!StringUtils.isBlank(blackListVO.getTel())) {
			 where += " and l.tel like :tel ";
		 }
		 if(!StringUtils.isBlank(blackListVO.getIdNumber())) {
			 where += " and l.id_number like :idNumber ";
		 }
		 String sort = "";
		 if (StringUtils.isNotBlank(sidx)) {
	            switch (sidx) {
	                case "nameEn":
	                    sort += " order by l.name_En " + sord;
	                    break;
	                case "nameCn":
	                    sort += " order by l.name_Cn " + sord;
	                    break;
	                case "tel":
	                    sort += " order by l.tel " + sord;
	                    break;
	                case "idNumber":
	                    sort += " order by l.id_number " + sord;
	                    break;
	                case "leaveDate":
	                    sort += " order by l.leave_date " + sord;
	                    break;
	                case "remark":
	                    sort += " order by l.remark " + sord;
	                    break;
	                case "lastModifyDate":
	                    sort += " order by l.last_Modify_Date " + sord;
	                    break;
	                default:
	                	sort += " order by l.leave_date desc, l.last_modify_date desc ";
	                    break;
	            }
		 }
	            sql = sql + where + sort;
	            countSql = countSql + where;

      Query query = em.createNativeQuery(sql);
      Query countQuery = em.createNativeQuery(countSql);

      query.setFirstResult(start).setMaxResults(length);

      if(!StringUtils.isBlank(blackListVO.getName())) {
          query.setParameter("name", "%"+blackListVO.getName()+"%");
          countQuery.setParameter("name", "%"+blackListVO.getName()+"%");
		 }
		 if(!StringUtils.isBlank(blackListVO.getTel())) {
	          query.setParameter("tel", "%"+blackListVO.getTel()+"%");
	          countQuery.setParameter("tel", "%"+blackListVO.getTel()+"%");
		 }
		 if(!StringUtils.isBlank(blackListVO.getIdNumber())) {
	          query.setParameter("idNumber", "%"+blackListVO.getIdNumber()+"%");
	          countQuery.setParameter("idNumber", "%"+blackListVO.getIdNumber()+"%");
		 }

	  List<BlackListVO> list =  EntityUtils.castEntity(query.getResultList(), BlackListVO.class);

      BigInteger count = (BigInteger) countQuery.getSingleResult();

      Pageable pageable = PageRequest.of(start / length, length);

      return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));
	 }

	 public List<Integer> searchBlackListedApplicant(){
		 String sql = "select applicant_info_id from (\r\n" +
		 		"select t.applicant_info_id, \r\n" +
		 		"case when a_id_number = b_id_number then 'Y' when\r\n" +
		 		"(case when a_name_en = b_name_en then 1 else 0 end) +\r\n" +
		 		"(case when (CONVERT(a_name_cn USING utf8) COLLATE utf8_bin) = b_name_cn then 1 else 0 end) +\r\n" +
		 		"(case when a_tel = b_tel then 1 else 0 end) >=2 then 'Y' else 'N' end as blacklisted\r\n" +
		 		"from (\r\n" +
		 		"select a.applicant_info_id, CONCAT(a.en_l_name, ' ',a.en_f_name) as a_name_en, CONCAT(a.cn_l_name, a.cn_f_name) as a_name_cn , a.mobile as a_tel, a.id_card_number as a_id_number, \r\n" +
		 		"b.name_en as b_name_en, b.name_cn as b_name_cn, b.tel as b_tel, b.id_number as b_id_number \r\n" +
		 		"from (\r\n" +
		 		"select app.applicant_info_id, CONCAT(app.en_f_name, ' ',  app.en_l_name) as 'Applicant_Name', app.id_card_number, app.gender, CONCAT(IFNULL(e.organization_name,'N/A'), ' - ', IFNULL(e.major,'N/A')) as 'education' \r\n" +
		 		",app.en_f_name, app.en_l_name, app.cn_f_name, app.cn_l_name, app.mobile\r\n" +
		 		"from applicant_info app\r\n" +
		 		"left join education e on (app.applicant_info_id = e.applicant_info_id and e.education_id in (select min(e2.education_id) from education e2 where app.applicant_info_id = e2.applicant_info_id)) \r\n" +
		 		") a, (select * from applicant_black_list where status ='A') b \r\n" +
		 		") t \r\n" +
		 		")t1 \r\n" +
		 		"where blacklisted = 'Y' order by applicant_info_id";

		 Query query = em.createNativeQuery(sql);
		 return query.getResultList();
	 }

}
