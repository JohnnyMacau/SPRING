package com.macauslot.recruitmentadmin.repository;


import com.macauslot.recruitmentadmin.entity.RecruitmentGroupMember;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentGroupMemberRepository extends BaseRepository<RecruitmentGroupMember, Integer> {

    List<RecruitmentGroupMember> findAllByRecruitmentGroupId(Integer recruitmentGroupId);
}
