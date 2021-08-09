package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.ApplicantBlackList;
import com.macauslot.recruitmentadmin.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ApplicantBlackListRepository extends BaseRepository<ApplicantBlackList,Integer>,ApplicantBlackListRepositoryCustom {
	ApplicantBlackList findByApplicantBlackListId(Integer applicantBlackListId);
	int countByIdNumberAndApplicantBlackListIdNot(String idNumber,Integer applicantBlackListId);
	int countByIdNumber(String idNumber);
	List<ApplicantBlackList> findAllByLeaveDateBeforeAndStatusAndRemarkIn (String date,String status, List<String> blacklistReasons);

}
