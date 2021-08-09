package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.RelativeInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface RelativeInfoRepository extends BaseRepository<RelativeInfo, Integer> {
    RelativeInfo findByApplicantInfoId(Integer applicantInfoId);

}
