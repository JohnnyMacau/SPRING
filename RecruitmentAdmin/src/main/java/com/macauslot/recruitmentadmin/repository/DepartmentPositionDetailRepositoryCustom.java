package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.vo.JobVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentProgressVO;

public interface DepartmentPositionDetailRepositoryCustom {


    Page<JobVO> page4JobDetail(int start, int length, JobVO condition, String sidx, String sord);
    public Page<JobVO> page4OpeningJob(int start, int length, String sidx, String sord, char[] status);
    public List<JobVO> getOpenJob();
    public List<JobVO> getDeptOpenJob(String deptCode);
    public Page<RecruitmentProgressVO> page4RecruitmentProgress(int start, int length, String sidx, String sord, UserDTO user);
}
