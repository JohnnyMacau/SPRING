package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.macauslot.recruitmentadmin.vo.BlackListVO;

public interface ApplicantBlackListRepositoryCustom {

	Page<BlackListVO> searchBlackList(int start, int length, String sidx, String sord, BlackListVO blackListVO);
	List<Integer> searchBlackListedApplicant();
}
