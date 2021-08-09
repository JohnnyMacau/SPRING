package com.macauslot.recruitment_ms.service.impl;

import com.macauslot.recruitment_ms.exception.DataNotFoundException;
import com.macauslot.recruitment_ms.repository.DepartmentRepository;
import com.macauslot.recruitment_ms.service.IDepartmentPostService;
import com.macauslot.recruitment_ms.util.TimeEnum;
import com.macauslot.recruitment_ms.util.EntityUtils;
import com.macauslot.recruitment_ms.vo.DeptPositionDescVO;
import com.macauslot.recruitment_ms.vo.DeptPositionDetailVO;
import com.macauslot.recruitment_ms.vo.DeptVO;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author jim.deng
 */
@Service
public class DepartmentPostServiceImpl implements IDepartmentPostService {


    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentPostServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public List<DeptPositionDetailVO> getDepartmentPositionDetail() {
        Date now = new Date();
//        String date = DateFormatUtils.format(now, TimeEnum.YYYY_MM_DD_HH_MM_SS.getName());
        String endDate = DateFormatUtils.format(now, TimeEnum.YYYY_MM_DD.getName());
        return findDepartmentPositionDetail(endDate);
    }

    @Override
    public List<DeptPositionDescVO> getDepartmentPositionDesc(Integer deptPosDetailId) {
        return findDepartmentPositionDesc(deptPosDetailId);
    }

    @Override
    public List<DeptVO> getDepartmentDesc() {
        return findDepartmentDesc();
    }




    private List<DeptPositionDetailVO> findDepartmentPositionDetail(String endDate) {
        return EntityUtils.castEntity(departmentRepository.findDepartmentPositionDetail(endDate)
                , DeptPositionDetailVO.class);
    }
    private List<DeptPositionDescVO> findDepartmentPositionDesc(Integer deptPosDetailId) {
        List<DeptPositionDescVO> data = EntityUtils.castEntity(departmentRepository.findDepartmentPositionDescription(deptPosDetailId),DeptPositionDescVO.class);
        if (data.size()!=1){
            throw new DataNotFoundException("無此職位");
        }
        return data;
    }
    private List<DeptVO> findDepartmentDesc() {
        return EntityUtils.castEntity(departmentRepository.findDepartmentDescription() , DeptVO.class);
    }
}
