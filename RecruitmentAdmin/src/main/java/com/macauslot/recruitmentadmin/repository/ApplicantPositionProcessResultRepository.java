package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.macauslot.recruitmentadmin.entity.ApplicantPositionProcessResult;

@Repository
public interface ApplicantPositionProcessResultRepository extends BaseRepository<ApplicantPositionProcessResult, Integer> {

	List<ApplicantPositionProcessResult> findByIndexNot(String index);
	List<ApplicantPositionProcessResult> findAllByOrderByIndex();

}
