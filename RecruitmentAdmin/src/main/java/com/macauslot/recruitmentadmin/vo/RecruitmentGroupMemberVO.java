package com.macauslot.recruitmentadmin.vo;

import com.macauslot.recruitmentadmin.annotation.Sign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RecruitmentGroupMemberVO {
    private Integer groupId;
    private String deptCode;
    private String groupName;
    private String departmentHead;
    private String interviewer;
    private String administrativePersonnel;

    @Sign("page4RecruitmentGroup_findGroupById")
    public RecruitmentGroupMemberVO(Integer groupId, String deptCode, String groupName, String departmentHead, String interviewer, String administrativePersonnel) {
        this.groupId = groupId;
        this.deptCode = deptCode;
        this.groupName = groupName;
        this.departmentHead = departmentHead;
        this.interviewer = interviewer;
        this.administrativePersonnel = administrativePersonnel;
    }

    //查詢條件：員工編號，組名，部門代號
    private String employeeId;

}
