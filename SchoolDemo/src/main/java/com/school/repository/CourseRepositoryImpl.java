package com.school.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.school.entity.Course;
import com.school.util.EntityUtils;
import com.school.vo.StendentVO;

public class CourseRepositoryImpl implements CourseRepositoryCustom {
    @PersistenceContext
    private EntityManager em;



	 public Page<Course> searchCourse(int start, int length, int institudeId, String sidx, String sord){

	        String sql ="select c.* from course c where 1=1  ";

	        String countsql = "select count(1) from course c where 1=1 ";

	        String where = " ";
	        String sort = "";

	        if (institudeId>0) {
	            where += " and c.institude_id= :institudeId ";
	        }

	        if (StringUtils.isNotBlank(sidx)) {
	            switch (sidx) {
	                case "name":
	                    sort += " order by c.name " + sord;
	                    break;
	                default:
	                    sort += " order by c.id " + sord;
	                    break;
	            }

	        }

	        Query query = em.createNativeQuery(sql + where + sort);

	        query.setFirstResult(start).setMaxResults(length);

	        Query countQuery = em.createNativeQuery(countsql + where);

	        if (institudeId>0) {
	        	  query.setParameter("institudeId", institudeId);
	        	  countQuery.setParameter("institudeId", institudeId);
	        }

	        List<Course> list = EntityUtils.castEntity(query.getResultList(), Course.class);
	        Integer count = (Integer) countQuery.getSingleResult();

	        Pageable pageable = PageRequest.of(start / length, length);
	        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

	    
	 }


}
