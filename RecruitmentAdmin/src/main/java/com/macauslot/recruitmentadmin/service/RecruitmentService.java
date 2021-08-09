package com.macauslot.recruitmentadmin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.entity.ApplicantBlackList;
import com.macauslot.recruitmentadmin.entity.ApplicantInfo;
import com.macauslot.recruitmentadmin.entity.ApplicantPosition;
import com.macauslot.recruitmentadmin.entity.ApplicantPositionProcessResult;
import com.macauslot.recruitmentadmin.entity.ApplicantSourceDetail;
import com.macauslot.recruitmentadmin.entity.Certification;
import com.macauslot.recruitmentadmin.entity.DepartmentPosition;
import com.macauslot.recruitmentadmin.entity.DepartmentPositionDetail;
import com.macauslot.recruitmentadmin.entity.Education;
import com.macauslot.recruitmentadmin.entity.Language;
import com.macauslot.recruitmentadmin.entity.MeetingRoom;
import com.macauslot.recruitmentadmin.entity.Message;
import com.macauslot.recruitmentadmin.entity.OnShift;
import com.macauslot.recruitmentadmin.entity.Organization;
import com.macauslot.recruitmentadmin.entity.OtherSkill;
import com.macauslot.recruitmentadmin.entity.RecruitmentGroup;
import com.macauslot.recruitmentadmin.entity.RecruitmentGroupMember;
import com.macauslot.recruitmentadmin.entity.User;
import com.macauslot.recruitmentadmin.entity.WorkingExperience;
import com.macauslot.recruitmentadmin.util.PageInfo;
import com.macauslot.recruitmentadmin.vo.ApplicantInfoVO;
import com.macauslot.recruitmentadmin.vo.ApplicantJobDetailVO;
import com.macauslot.recruitmentadmin.vo.ApplicantionRecordReportVO;
import com.macauslot.recruitmentadmin.vo.ApplicationHistoryVO;
import com.macauslot.recruitmentadmin.vo.ApplicationInterviewStatusSummaryVO;
import com.macauslot.recruitmentadmin.vo.ApplicationVO;
import com.macauslot.recruitmentadmin.vo.ApplyDataVO;
import com.macauslot.recruitmentadmin.vo.BlackListVO;
import com.macauslot.recruitmentadmin.vo.CandidateApplicationReport1VO;
import com.macauslot.recruitmentadmin.vo.CandidateApplicationReport2VO;
import com.macauslot.recruitmentadmin.vo.CandidateCollectingStatisticsVO;
import com.macauslot.recruitmentadmin.vo.CandidateInformationReportVO;
import com.macauslot.recruitmentadmin.vo.CommonVO;
import com.macauslot.recruitmentadmin.vo.DepartmentPositionDetailVO;
import com.macauslot.recruitmentadmin.vo.DeptVO;
import com.macauslot.recruitmentadmin.vo.JobApplyHistoryVO;
import com.macauslot.recruitmentadmin.vo.JobVO;
import com.macauslot.recruitmentadmin.vo.LoginUserVO;
import com.macauslot.recruitmentadmin.vo.ProcessHistoryVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentGroupMemberVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentProgressVO;
import com.macauslot.recruitmentadmin.vo.ResumeVO;

public interface RecruitmentService {
    List<Organization> getOrganization();

    List<DeptVO> getDepartment();

    List<String> getRecruitmentGroupName();

    RecruitmentGroupMemberVO getRecruitmentGroupById(Integer groupId);

    List<RecruitmentGroup> getRecruitmentGroupListByDepartmentCode(String departmentCode);

    User getUserByUserName(String userName);

    List<User> getUserListByUserNameContaining(String userName);

    List<User> getUserListByDeptCode(String deptCode);

    @Transactional(rollbackFor = Exception.class)
    void addRecruitmentGroup(RecruitmentGroup recruitmentGroup, List<RecruitmentGroupMember> groupMemberList);

    @Transactional(rollbackFor = Exception.class)
    void editRecruitmentGroup(RecruitmentGroup recruitmentGroup, List<RecruitmentGroupMember> groupMemberList);

    PageInfo<JobVO> getPage4JobDetail(Integer draw, Integer start, Integer length, JobVO condition, String sidx, String sord);


    List<Object[]> getApplicantAndRegisterData4Main();

    PageInfo<Message> getPage4ApplicantSourceType(Integer draw, Integer start, Integer length, Message condition, String sidx, String sord);

    DepartmentPositionDetailVO getDepartmentPositionDetail(Integer deptPosDetailId);

    void editDepartmentPositionDetail(DepartmentPositionDetail departmentPositionDetail, String user);

    void saveAsNewTemplate(DepartmentPositionDetail departmentPositionDetail);

    void addDepartmentPositionDetail(DepartmentPositionDetail departmentPositionDetail);

    List<String> getDepartmentPositionJobTitleList();

    List<DeptVO> getOrganizationDepartmentList(Integer orgId);

    List<DepartmentPosition> getDepartmentPositionList(String departmentCode, int orgId, String status);

    void insertDepartmentPosition(DepartmentPosition departmentPosition);

    void deleteDepartmentPosition(DepartmentPosition departmentPosition);

    Page<JobVO> getJobPage4Main(JobVO condition);

    PageInfo<ApplicantJobDetailVO> getPage4ApplicantJobDetail(Integer draw, Integer start, Integer length, ApplicantJobDetailVO condition, String sidx, String sord);

    PageInfo<ApplicantionRecordReportVO> getPage4ApplicantReport(Integer draw, Integer start, Integer length, ApplicantionRecordReportVO condition, String sidx, String sord);


    //應徵者資料總表
    PageInfo<CandidateInformationReportVO> getPage4CandidateInformationReport(Integer draw, Integer start, Integer length, CandidateInformationReportVO condition, String sidx, String sord);


    //3.1|應徵者申請總表1
    CandidateApplicationReport1VO getCandidateApplicationReport1(CandidateApplicationReport1VO condition);

    List<CandidateApplicationReport2VO> getCandidateApplicationReport2List(CandidateApplicationReport1VO condition);

    JSONArray getJobApplicationStatisticsList(ApplicationInterviewStatusSummaryVO condition);

    //4.2 & 4.3 |HR面試情況 ，部門面試情況
    JSONArray getInterviewsStatisticsList(ApplicationInterviewStatusSummaryVO condition);

    List<CandidateCollectingStatisticsVO> getCandidateApplicationSourcesList(CandidateCollectingStatisticsVO condition);

    List<CandidateCollectingStatisticsVO> getCandidateEducationalBackgroundList(CandidateCollectingStatisticsVO condition);

    List<CandidateCollectingStatisticsVO> getCandidateAgeRatioList(CandidateCollectingStatisticsVO condition, String recruitmentForm);

    PageInfo<ApplicationVO> searchApplication(Integer draw, Integer start, Integer length, ApplicationVO condition, String sidx, String sord);

    PageInfo<ApplicationVO> searchApplication(String employeeId, Integer draw, Integer start, Integer length, ApplicationVO condition, String sidx, String sord);


    PageInfo<JobVO> getPage4OpeningJob(Integer draw, Integer start, Integer length, String sidx, String sord, char[] status);

    List<ApplyDataVO> getApplyDataListByApplicantInfoId(Integer applicantInfoId);


    List<ApplyDataVO> getApplyDataListByApplicantPosId(Integer applicantPosId);

    List<OnShift> getOnShiftDataByApplicantPositionId(Integer applicant_position_id);

    ApplicantInfoVO getApplicantPersonalInfoByApplicantId(Integer applicantInfoId);

    List<Message> getMsgIdType();

    List<Message> getMsgIdType(String applicationOrg);

    List<Education> getApplicantEducationByApplicantId(Integer applicantInfoId);

    List<Certification> getApplicantCertificationByApplicantId(Integer applicantInfoId);

    List<WorkingExperience> getApplicantExperienceByApplicantId(Integer applicantInfoId);

    List<Language> getApplicantLanguagesByApplicantId(Integer applicantInfoId);

    List<OtherSkill> getApplicantOtherSkillByApplicantId(Integer applicantInfoId);


    void updateApplicantInfo(String type, ApplicantInfo dbData, ApplicantInfo applicantInfo);

    void checkDuplicatedIdCardNumber(ApplicantInfo applicantInfo);

    void checkDuplicatedEmail(ApplicantInfo applicantInfo);



    ResumeVO getTheWholeResume(Integer applicantInfoId);

    ResumeVO getTheResumeWithMaskInspection(UserDTO loginStaff, Integer applicantPosId);

    ResumeVO getTheResumeWithMaskInspection(boolean isServerRoleDept, boolean isNotFltAdmin, Integer applicantPosId);

    ResumeVO getProfileWithMaskInspection(boolean isServerRoleDept, boolean isNotFltAdmin, Integer applicantInfoId);

    ResumeVO getProfileWithMaskInspection(UserDTO loginStaff, Integer applicantInfoId);

    ResumeVO getProfile(Integer applicantInfoId);



    void batchChangeApplicantPositionExportDate(List<Integer> applicantPosIds);

    void batchDeleteJob(Integer[] batchDeleteIds);

    @Transactional(rollbackFor = Exception.class)
    void batchChangeApplicantInfo(List<ApplicantInfo> applicantInfoList, List<ApplicantPosition> applicantPositionList);

    ApplicantInfo getApplicantInfoById(Integer applicantInfoId);

    List<JobApplyHistoryVO> getJobApplyHistory(Integer applicantInfoId);

    List<ApplicantSourceDetail> getApplicantSourceDetailListByMessageId(Integer messageId);

    void autoHandleApplicantSourceDetailList(Integer messageId, List<ApplicantSourceDetail> applicantSourceDetailList);

    Message getMessageById(Integer messageId);

    List<Object> getJobTemplateList();

    List<ApplicantPositionProcessResult> getDeniedProcessResultList();

    List<ApplicantPositionProcessResult> getAllProcessResultList();

    void batchSeal(Integer deptPosDetailId, String user, String stageActionRemark);

    void closeAndSeal(Integer deptPosDetailId, String user, String stageActionRemark);

    void updateJobStatus(Integer deptPosDetailId, String status, String user);

    public List<JobVO> getOpenJob();

    public List<JobVO> getDeptOnlineJob(String deptCode);

    public PageInfo<ApplicantInfoVO> searchApplicant(Integer draw, Integer start, Integer length, ApplicantInfoVO condition, String sidx, String sord);

    public void batchBookmark(Integer[] applicationIds, Integer bookmarkJobId, String user);

    public void batchTransfer(Integer[] applicationIds, String user);

    public void batchDeptReply(Integer[] applicationIds, String[] batchReply, String[] interview_time, String user);

    public List<MeetingRoom> getMeetingRoomList();

    public void batchArrangeInterview(Integer[] applicationIds, String[] batchInterviewTime, String[] batchMeetingRoom, String user);

    public void batchSaveResult(Integer[] applicationIds, String[] batchResult, String user);

    public void batchInformApplicantOfInterview(Integer[] applicationIds, String user);

    public void batchInformDeptOfInterview(Integer[] applicationIds, UserDTO user);

    public void applicationProcess(Integer applicationId, Integer action, String actionRemark, Integer backStage, String user);

    public List<Message> getReturnStatusList(int currentStage);

    public List<Message> getProcessStatusList();

    public PageInfo<RecruitmentProgressVO> page4RecruitmentProgress(Integer draw, Integer start, Integer length, String sidx, String sord, UserDTO user);

    public List<CommonVO> getJobStageHeadCount(Integer dept_pos_detail_id);

    public List<CommonVO> getApplicationStageProcessTime(Integer applicant_position_id);

    public List<ApplicationHistoryVO> getApplicationHistory(Integer applicantInfoId);

    public PageInfo<BlackListVO> getPage4BlackList(Integer draw, Integer start, Integer length, String sidx, String sord, BlackListVO blackListVO);

    public PageInfo<LoginUserVO> getPage4UserList(Integer draw, Integer start, Integer length, String sidx, String sord, LoginUserVO user);


    PageInfo<RecruitmentGroupMemberVO> getPage4RecruitmentGroup(Integer draw, Integer start, Integer length, String sidx, String sord, RecruitmentGroupMemberVO recruitmentGroup);

    public void saveBlackList(ApplicantBlackList applicantBlackList);

    boolean checkDuplicateBeforeSaveBlackList(ApplicantBlackList applicantBlackList);


    @Transactional(rollbackFor = Exception.class)
    int scheduledBlacklist(String date, List<String> blacklistReasons);

    public void uploadBlackList(List<List<String>> data, String employeeId);

    @Transactional(rollbackFor = Exception.class)
    void replaceBlackList(List<List<String>> data, String employeeId);

    public void applyBlackListData();

    public void applyUserPermissionData(String userList);

    public void lostUserPermissionData(String lostStr);

//    public List<Message> getBlackListReasonList();

    public void batchDeleteBlackList(Integer[] blackList);

    public void batchDeleteRecruitmentGroup(Integer[] blackList);

    public List<ProcessHistoryVO> findProcessHistoryByApplicantInfoId(Integer applicantInfoId);

    public List<ProcessHistoryVO> findProcessHistoryByApplicantPosId(Integer applicant_pos_id);

    public ApplicantBlackList findByApplicantBlackListId(Integer applicantBlackListId);


}
