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

import com.school.entity.Student;
import com.school.util.EntityUtils;
import com.school.vo.StendentVO;

public class StudentRepositoryImpl implements StudentRepositoryCustom {
    @PersistenceContext
    private EntityManager em;



	 public Page<StendentVO> searchStudent(int start, int length, StendentVO condition, String sidx, String sord){

	        String sql ="select s.id, s.name,c.id as classid, c.name as classname from student s, class c where s.class_id= c.id ";

	        String countsql = "select count(1) from student s, class c where s.class_id= c.id ";

	        String where = " ";
	        String sort = "";

	        String name = condition.getName();
	        if (name != null &&name.compareTo("")!=0) {
	            where += " and s.name like :name ";
	        }

	        if (StringUtils.isNotBlank(sidx)) {
	            switch (sidx) {
	                case "name":
	                    sort += " order by name " + sord;
	                    break;
	                default:
	                    sort += " order by id " + sord;
	                    break;
	            }

	        }

	        Query query = em.createNativeQuery(sql + where + sort);

	        query.setFirstResult(start).setMaxResults(length);

	        Query countQuery = em.createNativeQuery(countsql + where);

	        if (name != null &&name.compareTo("")!=0) {
	        	  query.setParameter("name", "%"+name+"%");
	        	  countQuery.setParameter("name", "%"+name+"%");
	        }

	        List<StendentVO> list = EntityUtils.castEntity(query.getResultList(), StendentVO.class);
	        Integer count = (Integer) countQuery.getSingleResult();

	        Pageable pageable = PageRequest.of(start / length, length);
	        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

	    
	 }


}
