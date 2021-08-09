package com.macauslot.recruitment_ms.repository;

import com.macauslot.recruitment_ms.entity.OnShift;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnShiftRepository extends BaseRepository<OnShift, Integer> {

    List<OnShift> findOnShiftByApplicantInfoId(Integer applicantInfoId);
    List<OnShift> findOnShiftByApplicantInfoIdAndApplicantPositionId(Integer applicantInfoId,Integer applicantPositionId );

}
