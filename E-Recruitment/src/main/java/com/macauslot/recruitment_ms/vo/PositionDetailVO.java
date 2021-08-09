package com.macauslot.recruitment_ms.vo;


import com.macauslot.recruitment_ms.entity.ApplicantPosition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString

public class PositionDetailVO {
    List<DeptPositionDetailVO> deptPositionDetailVOList;
    Boolean suspendAllApplicant;
    List<ApplicantPosition> applicantPositionList;

    public PositionDetailVO(List<DeptPositionDetailVO> deptPositionDetailVOList, Boolean suspendAllApplicant, List<ApplicantPosition> applicantPositionList) {
        this.deptPositionDetailVOList = deptPositionDetailVOList;
        this.suspendAllApplicant = suspendAllApplicant;
        this.applicantPositionList = applicantPositionList;
    }
}
