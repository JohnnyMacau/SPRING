package com.macauslot.recruitmentadmin.repository;

import org.springframework.data.domain.Page;

import com.macauslot.recruitmentadmin.vo.ApplicantInfoVO;

public interface ApplicantInfoRepositoryCustom {

    public Page<ApplicantInfoVO> searchApplicant(int start, int length, ApplicantInfoVO condition, String sidx, String sord);


}
