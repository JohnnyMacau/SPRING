package com.demo.repository;

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

import com.demo.entity.Users;
import com.demo.util.EntityUtils;
import com.demo.vo.UserVO;

public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager em;


    @Override
	 public List<Users> getUsers(String username, String password){
		 String sql = "select * from users where username = :username and password = :password";
		 Query query = em.createNativeQuery(sql);
		 query.setParameter("username", username);
		 query.setParameter("password", password);
		 List l = query.getResultList();
		 List<Users> list = EntityUtils.castEntity(l, Users.class);
	        
		 return list;
	 }

    @Override
    public Page<Users> searchUser(int start, int length, UserVO condition, String sidx, String sord) {


        String sql ="select * from users where 1 = 1 ";


        String countsql = "select count(1) from users where 1 = 1 ";

        String where = " ";
        String sort = "";

        String username = condition.getUsername();
        if (username != null &&username.compareTo("")!=0) {
            where += " u.username = :username ";
        }

        if (StringUtils.isNotBlank(sidx)) {
            switch (sidx) {
                case "username":
                    sort += " order by username " + sord;
                    break;
                default:
                    sort += " order by id " + sord;
                    break;
            }

        }

        Query query = em.createNativeQuery(sql + where + sort);

        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql + where);

        if (username != null &&username.compareTo("")!=0) {
        	  query.setParameter("username", username);
        	  countQuery.setParameter("username", username);
        }

        List<Users> list = EntityUtils.castEntity(query.getResultList(), Users.class);
        Integer count = (Integer) countQuery.getSingleResult();

        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }

}
