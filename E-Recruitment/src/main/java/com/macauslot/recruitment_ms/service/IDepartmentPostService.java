package com.macauslot.recruitment_ms.service;

import com.macauslot.recruitment_ms.vo.DeptPositionDescVO;
import com.macauslot.recruitment_ms.vo.DeptPositionDetailVO;
import com.macauslot.recruitment_ms.vo.DeptVO;

import java.util.List;

public interface IDepartmentPostService {
    List<DeptPositionDetailVO> getDepartmentPositionDetail ();

    List<DeptPositionDescVO> getDepartmentPositionDesc(Integer deptPosDetailId);

    List<DeptVO> getDepartmentDesc();
}
