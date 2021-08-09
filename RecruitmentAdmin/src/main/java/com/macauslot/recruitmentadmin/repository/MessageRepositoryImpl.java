package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.Message;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.vo.ApplicantJobDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepositoryCustom {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<Message> page4ApplicantSourceType(int start, int length, Message condition, String sidx, String sord) {

        String sql =
"select m.id, mg.DESCRIPTION , m.CHN_DESC, GROUP_CONCAT(asd.chn_desc SEPARATOR '<br>') " + 
"from message m JOIN message_group mg " + 
"on m.MESSAGE_GROUP_ID = mg.ID and m.STATUS = 'A' and mg.DESCRIPTION in ('資料來源類別', '珠海資料來源類別') " + 
"and m.CHN_DESC in ('招聘網站', '報章', '招聘展', '招聘网站', '报章', '招聘展') " + 
"LEFT JOIN applicant_source_detail asd " + 
"on m.id = asd.message_id and asd.status='A' " + 
"group by m.id";
        String countsql =
        		"select count(1) from (select 1 " + 
        				"from message m JOIN message_group mg " + 
        				"on m.MESSAGE_GROUP_ID = mg.ID and m.STATUS = 'A' and mg.DESCRIPTION in ('資料來源類別', '珠海資料來源類別') " + 
        				"and m.CHN_DESC in ('招聘網站', '報章', '招聘展', '招聘网站', '报章', '招聘展') " + 
        				"LEFT JOIN applicant_source_detail asd " + 
        				"on m.id = asd.message_id and asd.status='A' " + 
        				"group by m.id) as t";
        String where = "";
        String sort = " order by mg.DESCRIPTION desc";


      /*  String status = condition.getStatus();
        if (StringUtils.isNotBlank(status)) {
            where += " and m.STATUS = :status";
        }
        String description = condition.getDescription();
        if (StringUtils.isNotBlank(description)) {
            where += " and mg.description = :description";
        }
        Integer id = condition.getAstId();
        if (id != null) {
            where += " and ast.id = :id";
        }*/

/*
        if (StringUtils.isNotBlank(sidx)) {
            switch (sidx) {
                case "code":
                    sort += " order by m.code";
                    break;
                case "engDesc":
                    sort += " order by m.eng_desc";
                    break;
                case "chnDesc":
                    sort += " order by m.chn_desc";
                    break;
                case "description":
                    sort += " order by mg.description";
                    break;
                case "status":
                    sort += " order by m.status";
                    break;
                case "orderNum":
                    sort += " order by ast.order_num";
                    break;


                default:
                    break;
            }
            if (StringUtils.isNotBlank(sort)) {
                if (!"age".equals(sidx)) {
                    sort += "asc".equals(sord) ? " asc" : " desc";
                } else {
                    sort += "asc".equals(sord) ? " desc" : " asc";
                }
            }
        }
*/


        Query query = em.createNativeQuery(sql + where + sort);

        query.setFirstResult(start).setMaxResults(length);

        Query countQuery = em.createNativeQuery(countsql + where);


       /* if (StringUtils.isNotBlank(status)) {
            query.setParameter("status", status);
            countQuery.setParameter("status", status);
        }

        if (StringUtils.isNotBlank(description)) {
            query.setParameter("description", description);
            countQuery.setParameter("description", description);
        }
        if (id != null) {
            query.setParameter("id", id);
            countQuery.setParameter("id", id);
        }*/


        List<Message> list = EntityUtils.castEntity(query.getResultList(), Message.class, new Class[]{Integer.class, String.class, String.class, String.class});


        BigInteger count = (BigInteger) countQuery.getSingleResult();

        Pageable pageable = PageRequest.of(start / length, length);
        return new PageImpl<>(list, pageable, Long.parseLong(count.toString()));

    }
    
    @Override
    public List<Message> getReturnStatusList(int currentStage) {
    	String hql = "from Message where messageGroupId=10 and cast(code as int) <:currentStage and status='A'";
    	Query query = em.createQuery(hql);
    	query.setParameter("currentStage", currentStage);
    	List<Message> list = query.getResultList();
    	return list;
    }
    
    @Override
    public List<Message> getProcessStatusList() {
    	String hql = "from Message where messageGroupId=10 and status='A'";
    	Query query = em.createQuery(hql);
    	List<Message> list = query.getResultList();
    	return list;
    }
    
//    @Override
//    public List<Message> getBlackListReasonList() {
//    	String hql = "from Message where messageGroupId=11 and status='A'";
//    	Query query = em.createQuery(hql);
//    	List<Message> list = query.getResultList();
//    	return list;
//    }
}
