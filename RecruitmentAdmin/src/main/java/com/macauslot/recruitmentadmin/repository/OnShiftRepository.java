package com.macauslot.recruitmentadmin.repository;

import com.macauslot.recruitmentadmin.entity.OnShift;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnShiftRepository extends BaseRepository<OnShift, Integer> {

    List<OnShift> findOnShiftByApplicantInfoId(Integer applicantInfoId);


    /**
     * GetApplicantOnShift
     *
     * @param applicant_position_id applicant_position_id
     * @return OnShift
     */
    @Query(value =
//            "select case dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end dayofweek, from_date, to_date from on_shift where applicant_info_id = :applicantId"
       "select case dayofweek when '1' then '星期一' when '2' then '星期二' when '3' then '星期三' when '4' then '星期四' when '5' then '星期五' when '6' then '星期六' when '7' then '星期日' when 'f1t5' then '週一至五' when '6 and 7' then '週六日' when 'every day' then '每日' end dayofweek, from_date, to_date from on_shift where applicant_position_id = :applicant_position_id"
            , nativeQuery = true)
    List<Object[]> findOnShiftDataByApplicantPositionId(
            @Param("applicant_position_id") Integer applicant_position_id
    );

    List<OnShift> findOnShiftByApplicantInfoIdAndApplicantPositionId(Integer applicantInfoId,Integer applicantPositionId );

}
