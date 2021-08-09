package com.macauslot.recruitmentadmin.repository;


import com.macauslot.recruitmentadmin.vo.RecruitmentGroupMemberVO;
import org.springframework.data.domain.Page;

public interface RecruitmentGroupRepositoryCustom {
    Page<RecruitmentGroupMemberVO> page4RecruitmentGroup(int start, int length, RecruitmentGroupMemberVO condition, String sidx, String sord);
}
