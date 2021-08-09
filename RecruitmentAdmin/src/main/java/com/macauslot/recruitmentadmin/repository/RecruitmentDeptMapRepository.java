package com.macauslot.recruitmentadmin.repository;

import org.springframework.stereotype.Repository;

import com.macauslot.recruitmentadmin.entity.RecruitmentDeptMap;


@Repository
public interface RecruitmentDeptMapRepository extends BaseRepository<RecruitmentDeptMap, Integer>, RecruitmentDeptMapRepositoryCustom {

}