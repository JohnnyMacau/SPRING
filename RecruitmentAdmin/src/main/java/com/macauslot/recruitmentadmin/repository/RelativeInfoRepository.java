package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.RelativeInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface RelativeInfoRepository extends BaseRepository<RelativeInfo, Integer> {
    RelativeInfo findByApplicantInfoId(Integer applicantInfoId);

}
