package com.macauslot.recruitmentadmin.repository;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.macauslot.recruitmentadmin.vo.*;
import org.springframework.data.domain.Page;

public interface ApplicantPositionRepositoryCustom {


    Page<ApplicantJobDetailVO> page4ApplicantJobDetail(int start, int length, ApplicantJobDetailVO condition, String sidx, String sord);
    Page<ApplicationVO> searchApplication(String employeeId, int start, int length, ApplicationVO condition, String sidx, String sord);
    public List<CommonVO> getJobStageHeadCount(Integer dept_pos_detail_id);
    public List<CommonVO> getApplicationStageProcessTime(Integer applicant_position_id);
    public List<ApplicationHistoryVO> getApplicationHistory(Integer applicantInfoId);
    public NoticeVO getNoticeInformation(Integer applicant_position_id);

    Page<ApplicantionRecordReportVO> page4ApplicantReport(int start, int length, ApplicantionRecordReportVO condition, String sidx, String sord);

    Page<CandidateInformationReportVO> page4CandidateInformationReport(int start, int length, CandidateInformationReportVO condition, String sidx, String sord);


    JSONArray findJobApplicationStatistics(ApplicationInterviewStatusSummaryVO condition);

    JSONArray findInterviewsStatistics(ApplicationInterviewStatusSummaryVO condition);
    
    public List<MailVO> findApplicationMailInfo(Integer[] applicationIds);
}
