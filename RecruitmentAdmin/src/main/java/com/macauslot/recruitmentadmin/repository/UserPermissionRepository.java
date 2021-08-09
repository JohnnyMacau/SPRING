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
import org.springframework.stereotype.Repository;

import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.LoginUserVO;

@Repository
public class UserPermissionRepository{
    @PersistenceContext
    private EntityManager em;

    public Page<LoginUserVO> findUserPermission(Integer start, Integer length, String sidx, String sord, LoginUserVO user) {
		 String sql = " SELECT usr.ID, rdm.recruitment_dept as deptCode,usr.USER_NAME as userName,usr.CH_NAME as chName,usr.recruiment_admin_user as recruimentAdminUser FROM USER usr LEFT JOIN recruitment_dept_map rdm ON usr.dept_code = rdm.hr_portal_dept where usr.user_name NOT regexp '[a-z]' and status ='A' ";
		 String countSql = " SELECT COUNT(1) FROM USER usr LEFT JOIN recruitment_dept_map rdm ON usr.dept_code = rdm.hr_portal_dept where usr.user_name NOT regexp '[a-z]' and status ='A' ";
		 String where = "";
		 if(!StringUtils.isBlank(user.getDeptCode())) {
			 where += " AND (rdm.recruitment_dept like :deptCode) ";
		 }
		 if(!StringUtils.isBlank(user.getUserName())) {
			 where += " AND usr.user_name like :userName ";
		 }
		 if(!StringUtils.isBlank(user.getChName())) {
			 where += " AND usr.ch_name like :chName ";
		 }
		 if(!StringUtils.isBlank(user.getRecruimentAdminUser())) {
			 where += " AND usr.recruiment_admin_user = :recruimentAdminUser ";
		 }
		 String sort = "";
		 if (StringUtils.isNotBlank(sidx)) {
	            switch (sidx) {
	                case "deptCode":
	                    sort += " order by usr.dept_code " + sord;
	                    break;
	                case "userName":
	                    sort += " order by usr.user_name " + sord;
	                    break;
	                case "chName":
	                    sort += " order by usr.ch_name " + sord;
	                    break;
					default:
						throw new IllegalArgumentException();
	            }
		 }
	            sql = sql + where + sort;
	            countSql = countSql + where;

     Query query = em.createNativeQuery(sql);
     Query countQuery = em.createNativeQuery(countSql);

     query.setFirstResult(start).setMaxResults(length);

     if(!StringUtils.isBlank(user.getDeptCode())) {
         query.setParameter("deptCode", "%"+user.getDeptCode()+"%");
         countQuery.setParameter("deptCode", "%"+user.getDeptCode()+"%");
		 }
		 if(!StringUtils.isBlank(user.getUserName())) {
	          query.setParameter("userName", "%"+user.getUserName()+"%");
	          countQuery.setParameter("userName", "%"+user.getUserName()+"%");
		 }
		 if(!StringUtils.isBlank(user.getChName())) {
	          query.setParameter("chName", "%"+user.getChName()+"%");
	          countQuery.setParameter("chName", "%"+user.getChName()+"%");
		 }
		 if(!StringUtils.isBlank(user.getRecruimentAdminUser())) {
			 query.setParameter("recruimentAdminUser", user.getRecruimentAdminUser());
	         countQuery.setParameter("recruimentAdminUser", user.getRecruimentAdminUser());
		 }

	 List<LoginUserVO> list =  EntityUtils.castEntity(query.getResultList(), LoginUserVO.class);

     BigInteger count = (BigInteger) countQuery.getSingleResult();

     Pageable pageable = PageRequest.of(start / length, length);

     return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));
	 }

    public void applyUserPermissionData(String userList) {
    	StringBuffer sql = new StringBuffer(" UPDATE user SET user.recruiment_admin_user='1' WHERE user.USER_NAME in ( ");
    	sql.append(userList);
    	sql.append(" ) ");
    	Query query = em.createNativeQuery(sql.toString());
    	int a = query.executeUpdate();
    	em.flush();
    	em.clear();
    	em.close();
    }

    public void lostUserPermissionData(String lostStr) {
    	StringBuffer sql = new StringBuffer(" UPDATE user SET user.recruiment_admin_user='0' WHERE user.USER_NAME in ( ");
    	sql.append(lostStr);
    	sql.append(" ) ");
    	Query query = em.createNativeQuery(sql.toString());
    	int a = query.executeUpdate();
    	em.flush();
    	em.clear();
    	em.close();
    }

    public List<LoginUserVO> findLoginUserVO(String userName) {
    	StringBuffer sql = new StringBuffer(" SELECT usr.ID, rdm.recruitment_dept as deptCode,usr.USER_NAME as userName,usr.CH_NAME as chName,usr.recruiment_admin_user as recruimentAdminUser FROM USER usr LEFT JOIN recruitment_dept_map rdm ON usr.dept_code = rdm.hr_portal_dept where usr.USER_NAME = '");
    	sql.append(userName);
    	sql.append("'");
    	Query query = em.createNativeQuery(sql.toString());
    	List<LoginUserVO> list =  EntityUtils.castEntity(query.getResultList(), LoginUserVO.class);

    	return list;
    }

}
