package com.macauslot.recruitmentadmin.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.fastjson.JSONArray;
import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.entity.ApplicantBlackList;
import com.macauslot.recruitmentadmin.entity.ApplicantInfo;
import com.macauslot.recruitmentadmin.entity.ApplicantPosition;
import com.macauslot.recruitmentadmin.entity.ApplicantPositionProcess;
import com.macauslot.recruitmentadmin.entity.ApplicantPositionProcessResult;
import com.macauslot.recruitmentadmin.entity.ApplicantSourceDetail;
import com.macauslot.recruitmentadmin.entity.BaseEntity;
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
import com.macauslot.recruitmentadmin.entity.RecruitmentDeptMap;
import com.macauslot.recruitmentadmin.entity.RecruitmentGroup;
import com.macauslot.recruitmentadmin.entity.RecruitmentGroupMember;
import com.macauslot.recruitmentadmin.entity.RelativeInfo;
import com.macauslot.recruitmentadmin.entity.User;
import com.macauslot.recruitmentadmin.entity.WorkingExperience;
import com.macauslot.recruitmentadmin.exception.DataNotFoundException;
import com.macauslot.recruitmentadmin.exception.DuplicateKeyException;
import com.macauslot.recruitmentadmin.repository.ApplicantBlackListRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantInfoRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantPositionProcessRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantPositionProcessResultRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantPositionRepository;
import com.macauslot.recruitmentadmin.repository.ApplicantSourceDetailRepository;
import com.macauslot.recruitmentadmin.repository.BaseRepository;
import com.macauslot.recruitmentadmin.repository.CertificationRepository;
import com.macauslot.recruitmentadmin.repository.DepartmentPositionDetailRepository;
import com.macauslot.recruitmentadmin.repository.DepartmentPositionRepository;
import com.macauslot.recruitmentadmin.repository.EducationRepository;
import com.macauslot.recruitmentadmin.repository.LanguageRepository;
import com.macauslot.recruitmentadmin.repository.MeetingRoomRepository;
import com.macauslot.recruitmentadmin.repository.MessageRepository;
import com.macauslot.recruitmentadmin.repository.OnShiftRepository;
import com.macauslot.recruitmentadmin.repository.OrganizationRepository;
import com.macauslot.recruitmentadmin.repository.OtherSkillRepository;
import com.macauslot.recruitmentadmin.repository.RecruitmentDeptMapRepository;
import com.macauslot.recruitmentadmin.repository.RecruitmentGroupMemberRepository;
import com.macauslot.recruitmentadmin.repository.RecruitmentGroupRepository;
import com.macauslot.recruitmentadmin.repository.RelativeInfoRepository;
import com.macauslot.recruitmentadmin.repository.UserPermissionRepository;
import com.macauslot.recruitmentadmin.repository.UserRepository;
import com.macauslot.recruitmentadmin.repository.WorkingExperienceRepository;
import com.macauslot.recruitmentadmin.service.AbstractBaseService;
import com.macauslot.recruitmentadmin.service.RecruitmentService;
import com.macauslot.recruitmentadmin.util.Common;
import com.macauslot.recruitmentadmin.util.EntityUtils;
import com.macauslot.recruitmentadmin.util.FlowEnum;
import com.macauslot.recruitmentadmin.util.PageInfo;
import com.macauslot.recruitmentadmin.util.PersonalDataEncryptionPolicy4LoginStaff;
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
import com.macauslot.recruitmentadmin.vo.MailVO;
import com.macauslot.recruitmentadmin.vo.NoticeVO;
import com.macauslot.recruitmentadmin.vo.ProcessHistoryVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentGroupMemberVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentProgressVO;
import com.macauslot.recruitmentadmin.vo.ResumeVO;

import freemarker.template.Template;

@Service
public class RecruitmentServiceImpl extends AbstractBaseService implements RecruitmentService {


    @Value("${sms.service.url}")
    private String smsServiceUrl;
    @Value("${sms.user}")
    private String smsUser;
    @Value("${sms.password}")
    private String smsPassword;
    @Value("${mail.receiver.test}")
    private String mailReceiverTest;
    @Value("${mail.from}")
    private String mailFrom;
    @Value("${transfer.mail.subject}")
    private String transferMailSubject;
    @Value("${interview.mail.subject}")
    private String interviewMailSubject;
    @Value("${recruitment.mail.subject}")
    private String recruitmentMailSubject;
    @Value("${reply.mail.subject}")
    private String replyMailSubject;
    @Value("${system.link}")
    private String systemLink;
    @Value("${hr.recruitment.team}")
    private String[] hrRecruitmentTeam;
    @Value("${sms.receiver.test}")
    private String smsReceiverTest;


    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    private final ApplicantInfoRepository applicantInfoRepository;

    private final RelativeInfoRepository relativeInfoRepository;

    private final ApplicantPositionRepository applicantPositionRepository;

    private final EducationRepository educationRepository;

    private final CertificationRepository certificationRepository;

    private final WorkingExperienceRepository workingExperienceRepository;

    private final LanguageRepository languageRepository;

    private final OtherSkillRepository otherSkillRepository;

    private final OrganizationRepository organizationRepository;

    private final OnShiftRepository onShiftRepository;

    private final DepartmentPositionDetailRepository departmentPositionDetailRepository;

    private final DepartmentPositionRepository departmentPositionRepository;
    private final MessageRepository messageRepository;
    private final ApplicantSourceDetailRepository applicantSourceDetailRepository;
    private final ApplicantPositionProcessResultRepository applicantPositionProcessResultRepository;
    private final ApplicantPositionProcessRepository applicantPositionProcessRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final ApplicantBlackListRepository applicantBlackListRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final RecruitmentGroupRepository recruitmentGroupRepository;
    private final RecruitmentGroupMemberRepository recruitmentGroupMemberRepository;
    private final RecruitmentDeptMapRepository recruitmentDeptMapRepository;


    private final UserRepository userRepository;

    @Autowired
    public RecruitmentServiceImpl(JavaMailSender mailSender, FreeMarkerConfigurer freeMarkerConfigurer, ApplicantInfoRepository applicantInfoRepository, RelativeInfoRepository relativeInfoRepository, ApplicantPositionRepository applicantPositionRepository, EducationRepository educationRepository, CertificationRepository certificationRepository, WorkingExperienceRepository workingExperienceRepository, LanguageRepository languageRepository, OtherSkillRepository otherSkillRepository, OrganizationRepository organizationRepository, OnShiftRepository onShiftRepository, DepartmentPositionDetailRepository departmentPositionDetailRepository, DepartmentPositionRepository departmentPositionRepository, MessageRepository messageRepository, ApplicantSourceDetailRepository applicantSourceDetailRepository, ApplicantPositionProcessResultRepository applicantPositionProcessResultRepository, ApplicantPositionProcessRepository applicantPositionProcessRepository, MeetingRoomRepository meetingRoomRepository, ApplicantBlackListRepository applicantBlackListRepository, UserPermissionRepository userPermissionRepository, RecruitmentGroupRepository recruitmentGroupRepository, RecruitmentGroupMemberRepository recruitmentGroupMemberRepository, UserRepository userRepository, RecruitmentDeptMapRepository recruitmentDeptMapRepository) {
        this.mailSender = mailSender;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.applicantInfoRepository = applicantInfoRepository;
        this.relativeInfoRepository = relativeInfoRepository;
        this.applicantPositionRepository = applicantPositionRepository;
        this.educationRepository = educationRepository;
        this.certificationRepository = certificationRepository;
        this.workingExperienceRepository = workingExperienceRepository;
        this.languageRepository = languageRepository;
        this.otherSkillRepository = otherSkillRepository;
        this.organizationRepository = organizationRepository;
        this.onShiftRepository = onShiftRepository;
        this.departmentPositionDetailRepository = departmentPositionDetailRepository;
        this.departmentPositionRepository = departmentPositionRepository;
        this.messageRepository = messageRepository;
        this.applicantSourceDetailRepository = applicantSourceDetailRepository;
        this.applicantPositionProcessResultRepository = applicantPositionProcessResultRepository;
        this.applicantPositionProcessRepository = applicantPositionProcessRepository;
        this.meetingRoomRepository = meetingRoomRepository;
        this.applicantBlackListRepository = applicantBlackListRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.recruitmentGroupRepository = recruitmentGroupRepository;
        this.recruitmentGroupMemberRepository = recruitmentGroupMemberRepository;
        this.userRepository = userRepository;
        this.recruitmentDeptMapRepository = recruitmentDeptMapRepository;
        Map<Class<? extends BaseEntity>, BaseRepository> tmpMap = new HashMap<>();
        tmpMap.put(ApplicantInfo.class, this.applicantInfoRepository);
        tmpMap.put(RelativeInfo.class, this.relativeInfoRepository);
        tmpMap.put(Education.class, this.educationRepository);
        tmpMap.put(Certification.class, this.certificationRepository);
        tmpMap.put(WorkingExperience.class, this.workingExperienceRepository);
        tmpMap.put(Language.class, this.languageRepository);
        tmpMap.put(OtherSkill.class, this.otherSkillRepository);
        tmpMap.put(OnShift.class, this.onShiftRepository);

        tmpMap.put(DepartmentPositionDetail.class, this.departmentPositionDetailRepository);
        tmpMap.put(DepartmentPosition.class, this.departmentPositionRepository);
        tmpMap.put(ApplicantSourceDetail.class, this.applicantSourceDetailRepository);
        tmpMap.put(ApplicantPosition.class, this.applicantPositionRepository);
        tmpMap.put(ApplicantPositionProcessResult.class, this.applicantPositionProcessResultRepository);
        tmpMap.put(ApplicantBlackList.class, this.applicantBlackListRepository);
        tmpMap.put(RecruitmentGroup.class, this.recruitmentGroupRepository);
        tmpMap.put(RecruitmentGroupMember.class, this.recruitmentGroupMemberRepository);
        tmpMap.put(RecruitmentDeptMap.class, this.recruitmentDeptMapRepository);

        CRUD_DB_MAP = Collections.unmodifiableMap(tmpMap);

    }


    @Override
    public List<Organization> getOrganization() {
        return organizationRepository.findAllByOrderByOrgId();
    }

    @Override
    public List<DeptVO> getDepartment() {
        return recruitmentDeptMapRepository.findAllRecruitmentDepts();
    }

    @Override
    public List<String> getRecruitmentGroupName() {
        return recruitmentGroupRepository.findGroupNameList();
    }

    @Override
    public RecruitmentGroupMemberVO getRecruitmentGroupById(Integer groupId) {
        List<RecruitmentGroupMemberVO> groupMemberVOList = EntityUtils.mapping(recruitmentGroupRepository.findGroupById(groupId), RecruitmentGroupMemberVO.class, "page4RecruitmentGroup_findGroupById");
        return getSingleResult(groupMemberVOList, groupId);
    }

    private <T> T getSingleResult(List<T> list, Integer id) {
        int size = list.size();
        if (size == 0) {
            throw new DataNotFoundException("getSingleResult() DataNotFound, the DataNotFound-id is:" + id);
        }
        if (size == 1) {
            return list.get(0);
        }
        throw new DuplicateKeyException("getSingleResult() DuplicateKey, the DuplicateKey-id is:" + id);
    }

    private <T> T getSingleResult(List<T> list, String str) {
        int size = list.size();
        if (size == 0) {
            throw new DataNotFoundException(str + " 無數據");
        }
        if (size == 1) {
            return list.get(0);
        }
        throw new DuplicateKeyException(str + " 數據重複");
    }


    @Override
    public List<RecruitmentGroup> getRecruitmentGroupListByDepartmentCode(String departmentCode) {
        return recruitmentGroupRepository.findAllByDepartmentCode(departmentCode);
    }


    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<User> getUserListByUserNameContaining(String userName) {
        return userRepository.findByUserNameContaining(userName);
    }

    @Override
    public List<User> getUserListByDeptCode(String deptCode) {
        List<User> userList = userRepository.findByDeptCode(deptCode);
        return userList.stream().filter(x -> isInteger(x.getUserName())).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRecruitmentGroup(RecruitmentGroup recruitmentGroup, List<RecruitmentGroupMember> groupMemberList) {
        RecruitmentGroup result = dbInsert(recruitmentGroup);
        for (RecruitmentGroupMember groupMember : groupMemberList) {
            groupMember.setRecruitmentGroupId(result.getId());
        }
//        dbBatchInsert(groupMemberList);
        dbAutohandle(Collections.emptyList(), groupMemberList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editRecruitmentGroup(RecruitmentGroup recruitmentGroup, List<RecruitmentGroupMember> groupMemberList) {
        Integer recruitmentGroupId = recruitmentGroup.getId();
        RecruitmentGroup dbData_r_group = recruitmentGroupRepository.findById(recruitmentGroupId).orElseThrow(DataNotFoundException::new);
        dbUpdate(dbData_r_group, recruitmentGroup);
        List<RecruitmentGroupMember> dbData_r_group_members = recruitmentGroupMemberRepository.findAllByRecruitmentGroupId(recruitmentGroupId);
        for (RecruitmentGroupMember groupMember : groupMemberList) {
            groupMember.setRecruitmentGroupId(recruitmentGroupId);
        }
        dbAutohandle(dbData_r_group_members, groupMemberList);
    }


    @Override
    public PageInfo<JobVO> getPage4JobDetail(Integer draw, Integer start, Integer length, JobVO condition, String sidx, String sord) {
        Page<JobVO> page = departmentPositionDetailRepository.page4JobDetail(start, length, condition, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public PageInfo<JobVO> getPage4OpeningJob(Integer draw, Integer start, Integer length, String sidx, String sord, char[] status) {
        Page<JobVO> page = departmentPositionDetailRepository.page4OpeningJob(start, length, sidx, sord, status);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public Page<JobVO> getJobPage4Main(JobVO condition) {
        return departmentPositionDetailRepository.page4JobDetail(0, 200, condition, "job_code", "asc");
    }

    @Override
    public PageInfo<ApplicantJobDetailVO> getPage4ApplicantJobDetail(Integer draw, Integer start, Integer length, ApplicantJobDetailVO condition, String sidx, String sord) {
        Page<ApplicantJobDetailVO> page = applicantPositionRepository.page4ApplicantJobDetail(start, length, condition, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    //1.申請記錄總表
    @Override
    public PageInfo<ApplicantionRecordReportVO> getPage4ApplicantReport(Integer draw, Integer start, Integer length, ApplicantionRecordReportVO condition, String sidx, String sord) {
        Page<ApplicantionRecordReportVO> page = applicantPositionRepository.page4ApplicantReport(start, length, condition, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    //2.應徵者資料總表
    @Override
    public PageInfo<CandidateInformationReportVO> getPage4CandidateInformationReport(Integer draw, Integer start, Integer length, CandidateInformationReportVO condition, String sidx, String sord) {
        Page<CandidateInformationReportVO> page = applicantPositionRepository.page4CandidateInformationReport(start, length, condition, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    //3.1|應徵者申請總表1
    @Override
    public CandidateApplicationReport1VO getCandidateApplicationReport1(CandidateApplicationReport1VO condition) {
        List<CandidateApplicationReport1VO> report1VOList = EntityUtils.mapping(applicantPositionRepository.find4CandidateApplicationReport1(condition.getId_card_number(), condition.getApplicationOrg()), CandidateApplicationReport1VO.class, "find4CandidateApplicationReport1");
//        System.err.println(report1VOList);
        return getSingleResult(report1VOList, condition.getId_card_number());
    }

    //3.2|應徵者申請總表2
    @Override
    public List<CandidateApplicationReport2VO> getCandidateApplicationReport2List(CandidateApplicationReport1VO condition) {
        return EntityUtils.mapping(applicantPositionRepository.find4CandidateApplicationReport2(condition.getId_card_number(), condition.getApplicationOrg()), CandidateApplicationReport2VO.class, "find4CandidateApplicationReport2");
    }

    //4.1|職位申請情況
    @Override
    public JSONArray getJobApplicationStatisticsList(ApplicationInterviewStatusSummaryVO condition) {
        return applicantPositionRepository.findJobApplicationStatistics(condition);
    }

    //4.2 & 4.3 |HR面試情況 ，部門面試情況
    @Override
    public JSONArray getInterviewsStatisticsList(ApplicationInterviewStatusSummaryVO condition) {
        return applicantPositionRepository.findInterviewsStatistics(condition);
    }


    //5.1|應徵者收集統計-應徵者申請來源
    @Override
    public List<CandidateCollectingStatisticsVO> getCandidateApplicationSourcesList(CandidateCollectingStatisticsVO condition) {
        return EntityUtils.mapping(applicantPositionRepository.find4CandidateApplicationSources(condition.getApplicant_postition_start_date(), condition.getApplicant_postition_end_date()), CandidateCollectingStatisticsVO.class, "candidateCollectingStatistics");
    }

    //5.2|應徵者收集統計-應徵者學歷背景
    @Override
    public List<CandidateCollectingStatisticsVO> getCandidateEducationalBackgroundList(CandidateCollectingStatisticsVO condition) {
        return EntityUtils.mapping(applicantPositionRepository.find4CandidateEducationalBackground(condition.getApplicant_postition_start_date(), condition.getApplicant_postition_end_date()), CandidateCollectingStatisticsVO.class, "candidateCollectingStatistics");
    }

    //5.3|應徵者收集統計-應徵者年齡比例
    @Override
    public List<CandidateCollectingStatisticsVO> getCandidateAgeRatioList(CandidateCollectingStatisticsVO condition, String recruitmentForm) {
        return EntityUtils.mapping(applicantPositionRepository.find4CandidateAgeRatio(condition.getApplicant_postition_start_date(), condition.getApplicant_postition_end_date(), recruitmentForm), CandidateCollectingStatisticsVO.class, "candidateCollectingStatistics");
    }


    @Override
    public PageInfo<ApplicationVO> searchApplication(Integer draw, Integer start, Integer length, ApplicationVO condition, String sidx, String sord) {
        Page<ApplicationVO> page = applicantPositionRepository.searchApplication(null, start, length, condition, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public PageInfo<ApplicationVO> searchApplication(String employeeId, Integer draw, Integer start, Integer length, ApplicationVO condition, String sidx, String sord) {
        Page<ApplicationVO> page = applicantPositionRepository.searchApplication(employeeId, start, length, condition, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }



    /*@Override
    public Object[] getRegisterData4Main(String type) {
        List<Object[]> registerData4Main = new ArrayList<>();
        if ("mslot".equalsIgnoreCase(type)) {
            registerData4Main = applicantInfoRepository.findRegisterData4Main_mslot();
        } else if ("flt".equalsIgnoreCase(type)) {
            registerData4Main = applicantInfoRepository.findRegisterData4Main_flt();
        }
        if (registerData4Main.isEmpty()) {
            throw new DataNotFoundException("applicantData4Main is empty");
        }
        return registerData4Main.get(0);
    }
    @Override
    public Object[] getApplicantData4Main(String type) {
        List<Object[]> applicantData4Main = new ArrayList<>();
        if ("mslot".equalsIgnoreCase(type)) {
            applicantData4Main = applicantInfoRepository.findApplicantData4Main_mslot();
        } else if ("flt".equalsIgnoreCase(type)) {
            applicantData4Main = applicantInfoRepository.findApplicantData4Main_flt();
        }
        if (applicantData4Main.isEmpty()) {
            throw new DataNotFoundException("applicantData4Main is empty");
        }
        return applicantData4Main.get(0);
    }*/


    @Override
    public List<Object[]> getApplicantAndRegisterData4Main() {
        return applicantInfoRepository.findApplicantAndRegisterData4Main();
    }


    @Override
    public PageInfo<Message> getPage4ApplicantSourceType(Integer draw, Integer start, Integer length, Message condition, String sidx, String sord) {
        Page<Message> page = messageRepository.page4ApplicantSourceType(start, length, condition, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }


    @Override
    public DepartmentPositionDetailVO getDepartmentPositionDetail(Integer deptPosDetailId) {
        List<DepartmentPositionDetailVO> voList = EntityUtils.mapping(departmentPositionDetailRepository.findDepartmentPositionDetail(deptPosDetailId), DepartmentPositionDetailVO.class, "findDepartmentPositionDetail");
        return getSingleResult(voList, deptPosDetailId);
    }

    @Override
    public void editDepartmentPositionDetail(DepartmentPositionDetail departmentPositionDetail, String user) {
        DepartmentPositionDetail dbData = departmentPositionDetailRepository.findById(departmentPositionDetail.getDeptPosDetailId())
                .orElseThrow(DataNotFoundException::new);
        if (dbData.getStatus().compareTo("S") != 0 && departmentPositionDetail.getStatus().compareTo("S") == 0) {
            dbData.setCloseDate(new Date());
            dbData.setClosedBy(user);
        }
        if (departmentPositionDetail.getStatus().compareTo("S") != 0) {
            dbData.setCloseDate(null);
            dbData.setClosedBy(null);
        }
        dbUpdate(dbData, departmentPositionDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAsNewTemplate(DepartmentPositionDetail departmentPositionDetail) {
        //save old data
        DepartmentPositionDetail dbData = departmentPositionDetailRepository.findById(departmentPositionDetail.getDeptPosDetailId())
                .orElseThrow(DataNotFoundException::new);
        dbUpdate(dbData, departmentPositionDetail);

        //insert new data
        departmentPositionDetail.setDeptPosDetailId(null);
        departmentPositionDetail.setDeptPosId(dbData.getDeptPosId());
        departmentPositionDetail.setStatus("T");
        dbInsert(departmentPositionDetail);
    }

    @Override
    public void addDepartmentPositionDetail(DepartmentPositionDetail departmentPositionDetail) {
        dbInsert(departmentPositionDetail);
    }

    @Override
    public List<DepartmentPosition> getDepartmentPositionList(String departmentCode, int orgId, String status) {
        return departmentPositionRepository.findAllByDepartmentCodeAndOrgIdAndStatus(departmentCode, orgId, status);
    }

    @Override
    public List<String> getDepartmentPositionJobTitleList() {
        return departmentPositionRepository.findAllDepartmentPositionJobTitle();
    }

    @Override
    public List<DeptVO> getOrganizationDepartmentList(Integer orgId) {
        return recruitmentDeptMapRepository.findDepartmentByOrgId(orgId);
    }

    @Override
    public void insertDepartmentPosition(DepartmentPosition departmentPosition) {
        List<DepartmentPosition> dbDataList = departmentPositionRepository.findAllByDepartmentCodeAndOrgIdAndStatus(departmentPosition.getDepartmentCode(), departmentPosition.getOrgId(), "A");
        String jobTitle = departmentPosition.getJobTitle().trim();
        if (dbDataList.stream().map(DepartmentPosition::getJobTitle).anyMatch(x -> x.trim().equalsIgnoreCase(jobTitle))) {
            throw new DuplicateKeyException("job title already exists");
        }
        dbInsert(departmentPosition);
    }

    @Override
    public void deleteDepartmentPosition(DepartmentPosition departmentPosition) {
//        dbDelete(departmentPosition);  所謂的delete只是suspend
        DepartmentPosition dbData = departmentPositionRepository.findById(departmentPosition.getDeptPosId())
                .orElseThrow(DataNotFoundException::new);
        dbData.setStatus("S");
        dbUpdate(dbData);
    }

    @Override
    public List<ApplyDataVO> getApplyDataListByApplicantInfoId(Integer applicantInfoId) {
        List<ApplyDataVO> applyDataVOList = EntityUtils.mapping(applicantPositionRepository.findApplyDataListByApplicantInfoId(applicantInfoId), ApplyDataVO.class, "getApplyDataListByApplicantInfoId");
        if (applyDataVOList.isEmpty()) {
            throw new DataNotFoundException("getApplyDataListByApplicantInfoId error");
        }
        explainApplyDataList(applyDataVOList);
        return applyDataVOList;
    }

    @Override
    public List<ApplyDataVO> getApplyDataListByApplicantPosId(Integer applicantPosId) {
        List<ApplyDataVO> applyDataVOList = EntityUtils.mapping(applicantPositionRepository.findApplyDataListByApplicantPosId(applicantPosId), ApplyDataVO.class, "getApplyDataListByApplicantPosId");
        if (applyDataVOList.isEmpty()) {
            throw new DataNotFoundException("getApplyDataListByApplicantPosId error");
        }
        explainApplyDataList(applyDataVOList);
        return applyDataVOList;
    }

    private void explainApplyDataList(List<ApplyDataVO> applyDataVOList) {
        for (ApplyDataVO applyDataVO : applyDataVOList) {
            String expected_salary_type = applyDataVO.getExpected_salary_type();
            if ("monthly".equalsIgnoreCase(expected_salary_type)) {
                applyDataVO.setExpected_salary_type("月薪");
            } else if ("hourly".equalsIgnoreCase(expected_salary_type)) {
                applyDataVO.setExpected_salary_type("时薪");
            }


            String notice_day_type = applyDataVO.getNotice_day_type();
            if ("month".equalsIgnoreCase(notice_day_type)) {
                applyDataVO.setNotice_day_type("月");
            } else if ("day".equalsIgnoreCase(notice_day_type)) {
                applyDataVO.setNotice_day_type("日");
            }

            String process_stage = applyDataVO.getProcess_stage();
          /*  if (process_stage.compareTo("11")==0||process_stage.compareTo("12")==0) {
                applyDataVO.setProcess_stage("已完成");
            } else {
                applyDataVO.setProcess_stage("處理中");
            }*/
            for (FlowEnum flowEnum : FlowEnum.values()) {
                if (flowEnum.getCode().equals(process_stage)) {
                    applyDataVO.setProcess_stage(flowEnum.getCnName());
                    break;
                }
            }


            String need_shift = applyDataVO.getNeed_shift();
            if ("1".equalsIgnoreCase(need_shift)) {
                applyDataVO.setNeed_shift("需要");
            } else {
                applyDataVO.setNeed_shift("不需要");
            }


            Integer applicant_position_id = applyDataVO.getApplicant_position_id();
            if (applicant_position_id != null) {
                applyDataVO.setOnShiftList(getOnShiftDataByApplicantPositionId(applicant_position_id));
            }


        }

       /* for (ApplyDataVO applyDataVO : applyDataVOList) {
            Integer applicant_position_id = applyDataVO.getApplicant_position_id();
            if (applicant_position_id != null) {
                applyDataVO.setOnShiftList(getOnShiftDataByApplicantPositionId(applicant_position_id));
            }
        }*/
    }

    @Override
    public List<OnShift> getOnShiftDataByApplicantPositionId(Integer applicant_position_id) {
        List<OnShift> onShiftList = EntityUtils.castEntity(onShiftRepository.findOnShiftDataByApplicantPositionId(applicant_position_id), OnShift.class);
        zeroFill(onShiftList);
        return onShiftList;
    }

    private void zeroFill(List<OnShift> onShiftList) {
        for (OnShift onShift : onShiftList) {
            String fromDate = onShift.getFromDate();
            String toDate = onShift.getToDate();

            if (StringUtils.isBlank(fromDate) || StringUtils.isBlank(toDate)) {
                continue;
            }
            String[] split1 = fromDate.split(":");
            String[] split2 = toDate.split(":");

            if (split1.length != 2 || split2.length != 2) {
                continue;
            }

            if (split1[0].length() == 1 || split1[1].length() == 1) {
                if (split1[0].length() == 1) {
                    fromDate = "0" + fromDate;
                }
                if (split1[1].length() == 1) {
                    fromDate += "0";
                }
                onShift.setFromDate(fromDate);
            }


            if (split2[0].length() == 1 || split2[1].length() == 1) {
                if (split2[0].length() == 1) {
                    toDate = "0" + toDate;
                }
                if (split2[1].length() == 1) {
                    toDate += "0";
                }
                onShift.setToDate(toDate);
            }


        }
    }

    @Override
    public ApplicantInfoVO getApplicantPersonalInfoByApplicantId(Integer applicantInfoId) {
        List<ApplicantInfoVO> applicantInfoVOList = EntityUtils.mapping(applicantInfoRepository.findApplicantPersonalInfoByApplicantId(applicantInfoId), ApplicantInfoVO.class, "findApplicantPersonalInfoByApplicantId");
        return getSingleResult(applicantInfoVOList, applicantInfoId);
    }

    @Override
    public List<Message> getMsgIdType() {
        return EntityUtils.castEntity(messageRepository.findAllMsgIdType(Arrays.asList("珠海身份證類別", "身份證類別")), Message.class, new Class[]{Integer.class, String.class, String.class});
    }

    @Override
    public List<Message> getMsgIdType(String applicationOrg) {
        if ("FLT".equalsIgnoreCase(applicationOrg)) {
            return EntityUtils.castEntity(messageRepository.findMsgIdType(Collections.singletonList("珠海身份證類別")), Message.class, new Class[]{Integer.class, String.class, String.class});
        } else {//其他applicationOrg的值，可能是空值NULL或字符串“null”
            return EntityUtils.castEntity(messageRepository.findMsgIdType(Collections.singletonList("身份證類別")), Message.class, new Class[]{Integer.class, String.class, String.class});
        }
    }


    @Override
    public List<Education> getApplicantEducationByApplicantId(Integer applicantInfoId) {
        return educationRepository.findEducationByApplicantInfoIdOrderByApplicantInfoId(applicantInfoId);
    }

    @Override
    public List<Certification> getApplicantCertificationByApplicantId(Integer applicantInfoId) {
        return certificationRepository.findCertificationByApplicantInfoIdOrderByApplicantInfoId(applicantInfoId);
    }


    @Override
    public List<WorkingExperience> getApplicantExperienceByApplicantId(Integer applicantInfoId) {
        return workingExperienceRepository.findWorkingExperienceByApplicantInfoIdOrderByApplicantInfoId(applicantInfoId);
    }

    @Override
    public List<Language> getApplicantLanguagesByApplicantId(Integer applicantInfoId) {
        return languageRepository.findLanguageByApplicantInfoIdOrderByApplicantInfoId(applicantInfoId);
    }

    @Override
    public List<OtherSkill> getApplicantOtherSkillByApplicantId(Integer applicantInfoId) {
        return otherSkillRepository.findOtherSkillByApplicantInfoIdOrderBySkillType(applicantInfoId);
    }

    @Override
    public void updateApplicantInfo(String type, ApplicantInfo dbData, ApplicantInfo applicantInfo) {
        if ("updateEmail".equals(type)) {
            checkDuplicatedEmail(applicantInfo);
        }
        if ("updateIdCard".equals(type)) {
            checkDuplicatedIdCardNumber(applicantInfo);
        }

        dbUpdate(dbData, applicantInfo);
    }

    @Override
    public void checkDuplicatedIdCardNumber(ApplicantInfo applicantInfo) {
        int count = applicantInfoRepository.countByIdCardNumberAndApplicationOrgAndApplicantInfoIdNot(applicantInfo.getIdCardNumber(), applicantInfo.getApplicationOrg(), applicantInfo.getApplicantInfoId());
        if (count > 0) {
            throw new DuplicateKeyException("輸入的 身份證號 已存在！");
        }
    }

    @Override
    public void checkDuplicatedEmail(ApplicantInfo applicantInfo) {
        int count = applicantInfoRepository.countByEmailAddressAndApplicationOrgAndApplicantInfoIdNot(applicantInfo.getEmailAddress(), applicantInfo.getApplicationOrg(), applicantInfo.getApplicantInfoId());
        if (count > 0) {
            throw new DuplicateKeyException("輸入的 電郵地址 已存在！");
        }
    }


    /**
     * 個人資料 + 所有申請歷史
     *
     * @param applicantInfoId
     * @return
     */
    @Override
    public ResumeVO getTheWholeResume(Integer applicantInfoId) {
        ResumeVO profile = getProfile(applicantInfoId);
        List<ApplyDataVO> applyDataVOList = getApplyDataListByApplicantInfoId(applicantInfoId);
        profile.setApplyDataVOList(applyDataVOList);
        return profile;
    }

    /**
     * 個人資料 + 當次申請的資料,部分可能屏蔽,可能不屏蔽(根據權限)
     *
     * @param loginStaff
     * @param applicantPosId
     * @return
     */
    @Override
    public ResumeVO getTheResumeWithMaskInspection(UserDTO loginStaff, Integer applicantPosId) {
        List<ApplyDataVO> applyDataVOList = getApplyDataListByApplicantPosId(applicantPosId);
        Integer applicantInfoId = applyDataVOList.get(0).getApplicant_info_id();
        ResumeVO profile = getProfileWithMaskInspection(loginStaff, applicantInfoId);
        profile.setApplyDataVOList(applyDataVOList);
        return profile;
    }


    /**
     * 個人資料 + 當次申請的資料,部分可能屏蔽,可能不屏蔽(根據權限) ps: 與上面的參數不同, 是為了避免在批量列印時, 需要循環判斷 UserDTO 的問題
     *
     * @param isServerRoleDept
     * @param isNotFltAdmin
     * @param applicantPosId
     * @return
     */
    @Override
    public ResumeVO getTheResumeWithMaskInspection(boolean isServerRoleDept, boolean isNotFltAdmin, Integer applicantPosId) {
        List<ApplyDataVO> applyDataVOList = getApplyDataListByApplicantPosId(applicantPosId);
        Integer applicantInfoId = applyDataVOList.get(0).getApplicant_info_id();
        ResumeVO profile = getProfileWithMaskInspection(isServerRoleDept, isNotFltAdmin, applicantInfoId);
        profile.setApplyDataVOList(applyDataVOList);
        return profile;
    }


    /**
     * 僅個人資料,部分可能屏蔽,可能不屏蔽(根據權限)
     *
     * @param loginStaff
     * @param applicantInfoId
     * @return
     */
    @Override
    public ResumeVO getProfileWithMaskInspection(UserDTO loginStaff, Integer applicantInfoId) {
        ResumeVO profile = getProfile(applicantInfoId);
        PersonalDataEncryptionPolicy4LoginStaff.setProfileMask(loginStaff, profile);
        return profile;
    }


    /**
     * 僅個人資料,部分可能屏蔽,可能不屏蔽(根據權限) ps: 與上面的參數不同, 是為了避免在批量列印時, 需要循環判斷 UserDTO 的問題
     *
     * @param isServerRoleDept
     * @param isNotFltAdmin
     * @param applicantInfoId
     * @return
     */
    @Override
    public ResumeVO getProfileWithMaskInspection(boolean isServerRoleDept, boolean isNotFltAdmin, Integer applicantInfoId) {
        ResumeVO profile = getProfile(applicantInfoId);
        PersonalDataEncryptionPolicy4LoginStaff.setProfileMask(isServerRoleDept, isNotFltAdmin, profile);
        return profile;
    }


    @Override
    public ResumeVO getProfile(Integer applicantInfoId) {

        ApplicantInfoVO applicantInfoVO = getApplicantPersonalInfoByApplicantId(applicantInfoId);
        if ("M".equalsIgnoreCase(String.valueOf(applicantInfoVO.getGender()))) {
            applicantInfoVO.setCnGender("男");
        } else if ("F".equalsIgnoreCase(String.valueOf(applicantInfoVO.getGender()))) {
            applicantInfoVO.setCnGender("女");
        }

        byte zero = 0, one = 1, two = 2;
        if (applicantInfoVO.getIn_service() != null) {
            if (zero == applicantInfoVO.getIn_service()) {
                applicantInfoVO.setCnIn_service(" - ");//舊資料,當時沒有該字段所以沒填
            } else if (one == applicantInfoVO.getIn_service()) {
                applicantInfoVO.setCnIn_service("已離任");
            } else if (two == applicantInfoVO.getIn_service()) {
                applicantInfoVO.setCnIn_service("現職");
            }
        }
        String m_status_tmp = applicantInfoVO.getMartialStatus();
        String m_status;
        if ("Single".equals(m_status_tmp)) {
            m_status = "未婚";
        } else if ("Married".equals(m_status_tmp)) {
            m_status = "已婚";
        } else if ("Divorce".equals(m_status_tmp)) {
            m_status = "離婚";
        } else if ("MA".equals(m_status_tmp)) {
            m_status = "MA";
        } else if ("Divorced".equals(m_status_tmp)) {
            m_status = "離婚";
        } else if ("Widowed".equals(m_status_tmp)) {
            m_status = "鳏寡";
        } else {
            m_status = " - ";
        }
        applicantInfoVO.setMartialStatus(m_status);

        List<Message> messageList = getMsgIdType(applicantInfoVO.getApplicationOrg());

        List<Education> educationList = getApplicantEducationByApplicantId(applicantInfoId);

        List<Certification> certificationList = getApplicantCertificationByApplicantId(applicantInfoId);

        List<WorkingExperience> experienceList = getApplicantExperienceByApplicantId(applicantInfoId);

        for (WorkingExperience workingExperience : experienceList) {

            String pay_method = workingExperience.getPayMethod();

            if ("hourly".equalsIgnoreCase(pay_method)) {
                pay_method = "時薪";
            } else if ("monthly".equalsIgnoreCase(pay_method)) {
                pay_method = "月薪";
            } else if ("daily".equalsIgnoreCase(pay_method)) {
                pay_method = "日薪";
            } else {
                pay_method = "";
            }
            workingExperience.setPayMethod(pay_method);

            String currency = workingExperience.getCurrency();
            if ("-1".equalsIgnoreCase(currency)) {
                workingExperience.setCurrency("");
            }


        }

        List<Language> languageList = getApplicantLanguagesByApplicantId(applicantInfoId);

        List<OtherSkill> otherSkillList = getApplicantOtherSkillByApplicantId(applicantInfoId);
        for (OtherSkill otherSkill : otherSkillList) {
            if ("computer skill".equals(otherSkill.getName())) {
                otherSkill.setName("電腦技能");
            } else if ("other skill".equals(otherSkill.getName())) {
                otherSkill.setName("其他技能");
            }
        }

        return new ResumeVO(null,
                applicantInfoVO,
                messageList, educationList, certificationList,
                experienceList, languageList, otherSkillList);

    }


    @Override
    public void batchChangeApplicantPositionExportDate(List<Integer> applicantPosIds) {
        applicantPositionRepository.batchUpdateExportDate(applicantPosIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchChangeApplicantInfo(List<ApplicantInfo> applicantInfoList, List<ApplicantPosition> applicantPositionList) {
        for (ApplicantInfo applicantInfo : applicantInfoList) {
            ApplicantInfo dbData = getApplicantInfoById(applicantInfo.getApplicantInfoId());
            dbUpdate(dbData, applicantInfo);
        }
        for (ApplicantPosition applicantPosition : applicantPositionList) {
            ApplicantPosition dbData = applicantPositionRepository.findById(applicantPosition.getApplicantPosId()).orElseThrow(DataNotFoundException::new);
            dbUpdate(dbData, applicantPosition);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteJob(Integer[] batchDeleteIds) {
        for (int deptPosDetailId : batchDeleteIds) {
            departmentPositionDetailRepository.deleteByDeptPosDetailId(deptPosDetailId);
        }
    }

    @Override
    public ApplicantInfo getApplicantInfoById(Integer applicantInfoId) {
        return applicantInfoRepository.findById(applicantInfoId).orElseThrow(DataNotFoundException::new);
    }

    @Override
    public List<JobApplyHistoryVO> getJobApplyHistory(Integer applicantInfoId) {
        List<JobApplyHistoryVO> jobApplyHistoryVOList = EntityUtils.castEntity(applicantPositionRepository.findJobApplyHistory(applicantInfoId), JobApplyHistoryVO.class);
        for (JobApplyHistoryVO jobApplyHistoryVO : jobApplyHistoryVOList) {
            String process_stage = jobApplyHistoryVO.getProcess_stage();
            if (process_stage.compareTo("11") == 0 || process_stage.compareTo("12") == 0) {
                jobApplyHistoryVO.setProcess_stage("已完成");
            } else {
                jobApplyHistoryVO.setProcess_stage("處理中");
            }
        }
        return jobApplyHistoryVOList;
    }

    @Override
    public List<ApplicantSourceDetail> getApplicantSourceDetailListByMessageId(Integer messageId) {
        return applicantSourceDetailRepository.findAllByMessageIdOrderByOrderNum(messageId);
    }


    @Override
    public void autoHandleApplicantSourceDetailList(Integer messageId, List<ApplicantSourceDetail> applicantSourceDetailList) {
        dbAutohandleWithOutDelete(applicantSourceDetailRepository.findAllByMessageIdOrderById(messageId), applicantSourceDetailList);
    }

    @Override
    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElseThrow(DataNotFoundException::new);
    }

    @Override
    public List<Object> getJobTemplateList() {
        return departmentPositionDetailRepository.getJobTemplateList();
    }

    @Override
    public List<ApplicantPositionProcessResult> getDeniedProcessResultList() {
        return applicantPositionProcessResultRepository.findByIndexNot("1.1");
    }

    @Override
    public List<ApplicantPositionProcessResult> getAllProcessResultList() {
        return applicantPositionProcessResultRepository.findAllByOrderByIndex();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSeal(Integer deptPosDetailId, String user, String stageActionRemark) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        DepartmentPositionDetail departmentPositionDetail = departmentPositionDetailRepository.findById(deptPosDetailId).orElseThrow(DataNotFoundException::new);
        List<ApplicantPosition> applicantPositionList = applicantPositionRepository.findApplicationByJobId(deptPosDetailId);

        departmentPositionDetail.setIsSealed(true);
        departmentPositionDetail.setLastModifyBy(user);
        departmentPositionDetail.setLastModifyDate(now);
        departmentPositionDetailRepository.save(departmentPositionDetail);

        for (ApplicantPosition applicantPosition : applicantPositionList) {
            if (applicantPosition.getProcessStage() > 10) {    //11,12狀態不會受影響
                continue;
            }
            ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
            applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
            applicantPositionProcess.setDeptPosDetailId(applicantPosition.getBookmarkedJobId() != null ? applicantPosition.getBookmarkedJobId() : applicantPosition.getDeptPosDetailId());
            applicantPositionProcess.setStage(applicantPosition.getProcessStage());
            applicantPositionProcess.setNewStage(11);
            applicantPositionProcess.setStageAction(4); //歸檔
            applicantPositionProcess.setStageActionRemark(stageActionRemark);
            applicantPositionProcess.setLastModifyBy(user);
            applicantPositionProcess.setLastModifyDate(now);
            applicantPositionProcessRepository.save(applicantPositionProcess);

            applicantPosition.setProcessStage(11); //結束
            applicantPosition.setLastModifyBy(user);
            applicantPosition.setLastModifyDate(now);
            applicantPositionRepository.save(applicantPosition);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeAndSeal(Integer deptPosDetailId, String user, String stageActionRemark) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        DepartmentPositionDetail departmentPositionDetail = departmentPositionDetailRepository.findById(deptPosDetailId).orElseThrow(DataNotFoundException::new);
        List<ApplicantPosition> applicantPositionList = applicantPositionRepository.findApplicationByJobId(deptPosDetailId);

        if (departmentPositionDetail.getStatus().compareTo("S") != 0) {
            departmentPositionDetail.setCloseDate(new Date());
            departmentPositionDetail.setClosedBy(user);
        }

        departmentPositionDetail.setStatus("S");
        departmentPositionDetail.setIsSealed(true);
        departmentPositionDetail.setLastModifyBy(user);
        departmentPositionDetail.setLastModifyDate(now);
        departmentPositionDetailRepository.save(departmentPositionDetail);

        for (ApplicantPosition applicantPosition : applicantPositionList) {
            if (applicantPosition.getProcessStage() > 10) {    //11,12狀態不會受影響
                continue;
            }
            ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
            applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
            applicantPositionProcess.setDeptPosDetailId(applicantPosition.getBookmarkedJobId() != null ? applicantPosition.getBookmarkedJobId() : applicantPosition.getDeptPosDetailId());
            applicantPositionProcess.setStage(applicantPosition.getProcessStage());
            applicantPositionProcess.setNewStage(11);
            applicantPositionProcess.setStageAction(4); //歸檔
            applicantPositionProcess.setStageActionRemark(stageActionRemark);
            applicantPositionProcess.setLastModifyBy(user);
            applicantPositionProcess.setLastModifyDate(now);
            applicantPositionProcessRepository.save(applicantPositionProcess);

            applicantPosition.setProcessStage(11); //結束
            applicantPosition.setLastModifyBy(user);
            applicantPosition.setLastModifyDate(now);
            applicantPositionRepository.save(applicantPosition);

        }
    }

    @Override
    public void updateJobStatus(Integer deptPosDetailId, String status, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        DepartmentPositionDetail departmentPositionDetail = departmentPositionDetailRepository.findById(deptPosDetailId).orElseThrow(DataNotFoundException::new);
        if (departmentPositionDetail.getStatus().compareTo("S") != 0 && status.compareTo("S") == 0) {
            departmentPositionDetail.setCloseDate(new Date());
            departmentPositionDetail.setClosedBy(user);
        }
        departmentPositionDetail.setStatus(status);
        departmentPositionDetail.setLastModifyBy(user);
        departmentPositionDetail.setLastModifyDate(now);
        departmentPositionDetailRepository.save(departmentPositionDetail);

    }

    @Override
    public List<JobVO> getOpenJob() {
        return departmentPositionDetailRepository.getOpenJob();
    }

    @Override
    public List<JobVO> getDeptOnlineJob(String deptCode) {
        return departmentPositionDetailRepository.getDeptOpenJob(deptCode);
    }

    @Override
    public PageInfo<ApplicantInfoVO> searchApplicant(Integer draw, Integer start, Integer length, ApplicantInfoVO condition, String sidx, String sord) {

        Page<ApplicantInfoVO> page = applicantInfoRepository.searchApplicant(start, length, condition, sidx, sord);
//        System.err.println("CCTT-----"+page.getContent());
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public void batchBookmark(Integer[] applicationIds, Integer bookmarkJobId, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (Integer applicationId : applicationIds) {
            ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);
            applicantPosition.setBookmarkedJobId(bookmarkJobId);
            applicantPosition.setLastModifyBy(user);
            applicantPosition.setLastModifyDate(now);
            applicantPositionRepository.save(applicantPosition);

            ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
            applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
            applicantPositionProcess.setDeptPosDetailId(bookmarkJobId);
            applicantPositionProcess.setStage(applicantPosition.getProcessStage());
            applicantPositionProcess.setNewStage(applicantPosition.getProcessStage());
            applicantPositionProcess.setStageAction(0); // 收藏
            applicantPositionProcess.setStageActionRemark("收藏");
            applicantPositionProcess.setLastModifyBy(user);
            applicantPositionProcess.setLastModifyDate(now);
            applicantPositionProcessRepository.save(applicantPositionProcess);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchTransfer(Integer[] applicationIds, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (Integer applicationId : applicationIds) {
            ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);
            //先加ApplicantPositionProcess後改applicantPosition
            ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
            applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
            applicantPositionProcess.setDeptPosDetailId(applicantPosition.getBookmarkedJobId() != null ? applicantPosition.getBookmarkedJobId() : applicantPosition.getDeptPosDetailId());
            applicantPositionProcess.setStage(applicantPosition.getProcessStage());
            applicantPositionProcess.setNewStage(2);
            applicantPositionProcess.setStageAction(1); // 通過HR篩選
            applicantPositionProcess.setLastModifyBy(user);
            applicantPositionProcess.setLastModifyDate(now);
            applicantPositionProcessRepository.save(applicantPositionProcess);

            applicantPosition.setProcessStage(2); // 部門篩選簡曆
            applicantPosition.setInformDept(1);
            applicantPosition.setLastModifyBy(user);
            applicantPosition.setLastModifyDate(now);

            applicantPositionRepository.save(applicantPosition);
        }

        List<MailVO> mailList = applicantPositionRepository.findApplicationMailInfo(applicationIds);
        if (mailList.size() > 0) {
            for (MailVO vo : mailList) {
                Template template = null;
                String html = "";
                Map<String, String> model = new HashMap<>(20);
                model.put("job_title", vo.getJob_title());
                model.put("application_count", vo.getApplication_count().toString());
                model.put("link", systemLink);

                String[] mailTo = vo.getGroup_member().split(",");
                if (StringUtils.isNotBlank(mailReceiverTest)) {
                    mailTo = mailReceiverTest.split(",");
                }
                try {
                    template = freeMarkerConfigurer.getConfiguration().getTemplate("/backend/message/transfer_to_dept.html");
                    html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                    Common.sendMail(mailSender, mailFrom, mailTo, transferMailSubject, html);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeptReply(Integer[] applicationIds, String[] batchReply, String[] interview_time, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < applicationIds.length; i++) {
            Integer applicationId = applicationIds[i];
            String reply = batchReply[i];
            //先加ApplicantPositionProcess後改applicantPosition
            ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);
            if (reply.compareTo("Y") == 0) {
                ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
                applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
                applicantPositionProcess.setDeptPosDetailId(applicantPosition.getBookmarkedJobId() != null ? applicantPosition.getBookmarkedJobId() : applicantPosition.getDeptPosDetailId());
                applicantPositionProcess.setStage(applicantPosition.getProcessStage());
                applicantPositionProcess.setNewStage(3);
                applicantPositionProcess.setStageAction(1); // 通過部門篩選
                applicantPositionProcess.setLastModifyBy(user);
                applicantPositionProcess.setLastModifyDate(now);
                applicantPositionProcessRepository.save(applicantPositionProcess);

                applicantPosition.setProcessStage(3); //  HR預約面試
                for (int j = 0; j < interview_time.length; j++) {
                    try {
                        Common.invokeMethod(applicantPosition, "setPreferInterviewTime" + (j + 1), new Object[]{interview_time[j]});
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                applicantPosition.setDeptReplyDate(now);
                applicantPosition.setLastModifyBy(user);
                applicantPosition.setLastModifyDate(now);
                applicantPosition.setInformDept(1);
                applicantPositionRepository.save(applicantPosition);

            } else {
                ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
                applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
                applicantPositionProcess.setDeptPosDetailId(applicantPosition.getBookmarkedJobId() != null ? applicantPosition.getBookmarkedJobId() : applicantPosition.getDeptPosDetailId());
                applicantPositionProcess.setStage(applicantPosition.getProcessStage());
                applicantPositionProcess.setNewStage(11);
                applicantPositionProcess.setStageAction(2); // 沒通過部門篩選
                applicantPositionProcess.setLastModifyBy(user);
                applicantPositionProcess.setLastModifyDate(now);
                applicantPositionProcessRepository.save(applicantPositionProcess);

                applicantPosition.setProcessStage(11); //  結束
                applicantPosition.setDeptReplyDate(now);
                applicantPosition.setLastModifyBy(user);
                applicantPosition.setLastModifyDate(now);
                applicantPosition.setInformDept(1);
                applicantPositionRepository.save(applicantPosition);
            }
        }

        List<MailVO> mailList = applicantPositionRepository.findApplicationMailInfo(applicationIds);
        if (mailList.size() > 0) {
            for (MailVO vo : mailList) {
                Template template = null;
                String html = "";
                Map<String, String> model = new HashMap<>(20);
                model.put("job_title", vo.getJob_title());
                model.put("link", systemLink);

                String[] mailTo = hrRecruitmentTeam;
                if (StringUtils.isNotBlank(mailReceiverTest)) {
                    mailTo = mailReceiverTest.split(",");
                }
                try {
                    template = freeMarkerConfigurer.getConfiguration().getTemplate("/backend/message/dept_reply.html");
                    html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                    Common.sendMail(mailSender, mailFrom, mailTo, replyMailSubject, html);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchArrangeInterview(Integer[] applicationIds, String[] batchInterviewTime, String[] batchMeetingRoom, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < applicationIds.length; i++) {
            Integer applicationId = applicationIds[i];
            String interviewTime = batchInterviewTime[i];
            String meetingRoom = batchMeetingRoom[i];
            ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);
            applicantPosition.setMeetingRoomId(Integer.parseInt(meetingRoom));
            applicantPosition.setInterviewTime(interviewTime);
            applicantPosition.setLastModifyBy(user);
            applicantPosition.setLastModifyDate(now);
            applicantPositionRepository.save(applicantPosition);
        }
    }

    @Override
    public void batchSaveResult(Integer[] applicationIds, String[] batchResult, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < applicationIds.length; i++) {
            Integer applicationId = applicationIds[i];
            String stageActionRemark = batchResult[i];
            ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);

            ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
            applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
            applicantPositionProcess.setDeptPosDetailId(applicantPosition.getBookmarkedJobId() != null ? applicantPosition.getBookmarkedJobId() : applicantPosition.getDeptPosDetailId());
            applicantPositionProcess.setStage(applicantPosition.getProcessStage());
            applicantPositionProcess.setNewStage(11);
            applicantPositionProcess.setStageAction(5); //HR手動結束流程並選擇原因
            applicantPositionProcess.setStageActionRemark(stageActionRemark);
            applicantPositionProcess.setLastModifyBy(user);
            applicantPositionProcess.setLastModifyDate(now);
            applicantPositionProcessRepository.save(applicantPositionProcess);

            applicantPosition.setProcessStage(11); //結束
            applicantPosition.setLastModifyBy(user);
            applicantPosition.setLastModifyDate(now);
            applicantPositionRepository.save(applicantPosition);
        }
    }

    @Override
    public void batchInformApplicantOfInterview(Integer[] applicationIds, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < applicationIds.length; i++) {
            Integer applicationId = applicationIds[i];
            ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);

            NoticeVO notice = applicantPositionRepository.getNoticeInformation(applicantPosition.getApplicantPosId());

            if (notice != null) {
                String org = notice.getOrg().toLowerCase();
                String interviewTime = notice.getInterviewTime().replace("Monday", "星期一").replace("Tuesday", "星期二").replace("Wednesday", "星期三").replace("Thursday", "星期四").replace("Friday", "星期五").replace("Saturday", "星期六").replace("Sunday", "星期日").replace("AM", "上午").replace("PM", "下午");

                if (org.compareTo("flt") == 0) {
                    Template template = null;
                    String html = "";
                    Map<String, String> model = new HashMap<>(20);
                    model.put("name", notice.getCnLName() + notice.getCnFName());
                    model.put("solution", notice.getSolution());
                    model.put("job_title", notice.getJobTitle());
                    model.put("time", interviewTime);

                    String[] mailTo = new String[]{notice.getEmailAddress()};
                    if (StringUtils.isNotBlank(mailReceiverTest)) {
                        mailTo = mailReceiverTest.split(",");
                    }
                    try {
                        template = freeMarkerConfigurer.getConfiguration().getTemplate("/backend/message/flt_interview_mail.html");
                        html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                        Common.sendMail(mailSender, mailFrom, mailTo, interviewMailSubject, html);

                        applicantPosition.setInformApplicant(1);
                        applicantPosition.setLastModifyBy(user);
                        applicantPosition.setLastModifyDate(now);
                        applicantPositionRepository.save(applicantPosition);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Template template = null;
                    String html = "";
                    Map<String, String> model = new HashMap<>(20);
                    model.put("job_title", notice.getJobTitle());
                    model.put("time", interviewTime);
                    try {
                        template = freeMarkerConfigurer.getConfiguration().getTemplate("/backend/message/" + org + "_interview_sms.html");
                        html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                        String receiver = notice.getAreaCode() + notice.getMobile();
                        if (StringUtils.isNotBlank(smsReceiverTest)) {
                            receiver = smsReceiverTest;
                        }
                        int smsResult = Common.sendSMS(receiver, html, smsServiceUrl, smsUser, smsPassword);

                        if (smsResult == 200) {
                            applicantPosition.setInformApplicant(1);
                            applicantPosition.setLastModifyBy(user);
                            applicantPosition.setLastModifyDate(now);
                            applicantPositionRepository.save(applicantPosition);
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void batchInformDeptOfInterview(Integer[] applicationIds, UserDTO user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Boolean isFLTAdmin = user.getIsFLTAdmin();

        for (int i = 0; i < applicationIds.length; i++) {
            Integer applicationId = applicationIds[i];
            ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);
            applicantPosition.setInformDept(1);
            applicantPosition.setLastModifyBy(user.getEmployeeId());
            applicantPosition.setLastModifyDate(now);
            applicantPositionRepository.save(applicantPosition);
        }

        List<MailVO> mailList = applicantPositionRepository.findApplicationMailInfo(applicationIds);
        if (mailList.size() > 0) {
            for (MailVO vo : mailList) {
                Template template = null;
                String html = "";
                Map<String, String> model = new HashMap<>(20);
                model.put("job_title", vo.getJob_title());
                model.put("opt", (isFLTAdmin != null && isFLTAdmin) ? "/HR招聘組同事" : "");
                model.put("link", systemLink);

                String[] mailTo = vo.getGroup_member().split(",");
                if (isFLTAdmin != null && isFLTAdmin) {
                    List<String> list = new ArrayList<String>();
                    for (String groupMember : mailTo) {
                        list.add(groupMember);
                    }
                    for (String hr : hrRecruitmentTeam) {
                        list.add(hr);
                    }
                    mailTo = list.toArray(new String[list.size()]);
                }

                if (StringUtils.isNotBlank(mailReceiverTest)) {
                    mailTo = mailReceiverTest.split(",");
                }
                try {
                    template = freeMarkerConfigurer.getConfiguration().getTemplate("/backend/message/arrange_interview.html");
                    html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                    Common.sendMail(mailSender, mailFrom, mailTo, interviewMailSubject, html);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applicationProcess(Integer applicationId, Integer action, String actionRemark, Integer backStage, String user) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        ApplicantPosition applicantPosition = applicantPositionRepository.findById(applicationId).orElseThrow(DataNotFoundException::new);

        ApplicantPositionProcess applicantPositionProcess = new ApplicantPositionProcess();
        applicantPositionProcess.setApplicantPositionId(applicantPosition.getApplicantPosId());
        applicantPositionProcess.setDeptPosDetailId(applicantPosition.getBookmarkedJobId() != null ? applicantPosition.getBookmarkedJobId() : applicantPosition.getDeptPosDetailId());
        applicantPositionProcess.setStage(applicantPosition.getProcessStage());
        if (action == 1) {
            if (applicantPosition.getProcessStage() == 10) applicantPositionProcess.setNewStage(12); //聘用成功
            else applicantPositionProcess.setNewStage(applicantPosition.getProcessStage() + 1);    //下一步
        }
        if (action == 2) {
            applicantPositionProcess.setNewStage(11); //結束(沒入職)
            applicantPositionProcess.setStageActionRemark(actionRemark);
        }
        if (action == 3) {
            applicantPositionProcess.setNewStage(backStage);
        }
        applicantPositionProcess.setStageAction(action); // 通過部門篩選
        applicantPositionProcess.setLastModifyBy(user);
        applicantPositionProcess.setLastModifyDate(now);
        applicantPositionProcessRepository.save(applicantPositionProcess);


        if (action == 1) {
            if (applicantPosition.getProcessStage() == 10) applicantPosition.setProcessStage(12); //聘用成功
            else applicantPosition.setProcessStage(applicantPosition.getProcessStage() + 1);    //下一步
        }
        if (action == 2) {
            applicantPosition.setProcessStage(11); //結束(沒入職)
        }
        if (action == 3) {
            applicantPosition.setProcessStage(backStage);
        }
        applicantPosition.setLastModifyBy(user);
        applicantPosition.setLastModifyDate(now);
        applicantPositionRepository.save(applicantPosition);

        if (action == 1 && applicantPosition.getProcessStage() == 9) {
            List<MailVO> mailList = applicantPositionRepository.findApplicationMailInfo(new Integer[]{applicationId});
            if (mailList.size() > 0) {
                for (MailVO vo : mailList) {
                    Template template = null;
                    String html = "";
                    Map<String, String> model = new HashMap<>(20);
                    model.put("job_title", vo.getJob_title());
                    model.put("link", systemLink);

                    String[] mailTo = vo.getGroup_member().split(",");
                    if (StringUtils.isNotBlank(mailReceiverTest)) {
                        mailTo = mailReceiverTest.split(",");
                    }
                    try {
                        template = freeMarkerConfigurer.getConfiguration().getTemplate("/backend/message/recruit_success.html");
                        html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                        Common.sendMail(mailSender, mailFrom, mailTo, recruitmentMailSubject, html);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public List<MeetingRoom> getMeetingRoomList() {
        return meetingRoomRepository.findAll();
    }

    @Override
    public List<Message> getReturnStatusList(int currentStage) {
        return messageRepository.getReturnStatusList(currentStage);
    }

    @Override
    public List<Message> getProcessStatusList() {
        return messageRepository.getProcessStatusList();
    }

    @Override
    public PageInfo<RecruitmentProgressVO> page4RecruitmentProgress(Integer draw, Integer start, Integer length, String sidx, String sord, UserDTO user) {
        Page<RecruitmentProgressVO> page = departmentPositionDetailRepository.page4RecruitmentProgress(start, length, sidx, sord, user);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public List<CommonVO> getJobStageHeadCount(Integer dept_pos_detail_id) {
        return applicantPositionRepository.getJobStageHeadCount(dept_pos_detail_id);
    }

    @Override
    public List<CommonVO> getApplicationStageProcessTime(Integer applicant_position_id) {
        return applicantPositionRepository.getApplicationStageProcessTime(applicant_position_id);
    }

    @Override
    public List<ApplicationHistoryVO> getApplicationHistory(Integer applicantInfoId) {
        return applicantPositionRepository.getApplicationHistory(applicantInfoId);
    }

    @Override
    public PageInfo<BlackListVO> getPage4BlackList(Integer draw, Integer start, Integer length, String sidx, String sord, BlackListVO blackListVO) {
        Page<BlackListVO> page = applicantBlackListRepository.searchBlackList(start, length, sidx, sord, blackListVO);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public PageInfo<LoginUserVO> getPage4UserList(Integer draw, Integer start, Integer length, String sidx, String sord, LoginUserVO loginUserVO) {
        Page<LoginUserVO> page = userPermissionRepository.findUserPermission(start, length, sidx, sord, loginUserVO);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public PageInfo<RecruitmentGroupMemberVO> getPage4RecruitmentGroup(Integer draw, Integer start, Integer length, String sidx, String sord, RecruitmentGroupMemberVO recruitmentGroup) {
        Page<RecruitmentGroupMemberVO> page = recruitmentGroupRepository.page4RecruitmentGroup(start, length, recruitmentGroup, sidx, sord);
        return PageInfo.convertPage2PageInfo(draw, page);
    }

    @Override
    public void saveBlackList(ApplicantBlackList applicantBlackList) {
        dbInsert(applicantBlackList);
    }

    @Override
    public boolean checkDuplicateBeforeSaveBlackList(ApplicantBlackList applicantBlackList) {
        String idNumber = applicantBlackList.getIdNumber();
        if (idNumber == null) {
            throw new IllegalArgumentException();
        }
        Integer applicantBlackListId = applicantBlackList.getApplicantBlackListId();
        int count;
        if (applicantBlackListId == null) {
            count = applicantBlackListRepository.countByIdNumber(idNumber);
        } else {
            count = applicantBlackListRepository.countByIdNumberAndApplicantBlackListIdNot(idNumber, applicantBlackListId);
        }
        return count > 0;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int scheduledBlacklist(String date, List<String> blacklistReasons) {
        List<ApplicantBlackList> list = applicantBlackListRepository.findAllByLeaveDateBeforeAndStatusAndRemarkIn(date, "A", blacklistReasons);
        List<ApplicantBlackList> collect = list.stream().filter(x -> StringUtils.isNotBlank(x.getLeaveDate())).collect(Collectors.toList());
//        System.err.println(collect);
        for (ApplicantBlackList applicantBlackList : collect) {
            applicantBlackList.setStatus("S");
            applicantBlackList.setLastModifyBy("AUTO");
            applicantBlackList.setLastModifyDate(new Timestamp(System.currentTimeMillis()));
            dbUpdate(applicantBlackList);
        }
        return collect.size();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadBlackList(List<List<String>> data, String employeeId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) continue; //skip title
            if (data.get(i).size() == 0) {
                break;
            }
            for (int j = 0; j < data.get(i).size(); j++) {
                System.out.print(data.get(i).get(j) + ",");
            }

            ApplicantBlackList applicantBlackList = new ApplicantBlackList();
            applicantBlackList.setNameEn(data.get(i).get(0));
            applicantBlackList.setNameCn(data.get(i).get(1));
            applicantBlackList.setTel(data.get(i).get(2));
            applicantBlackList.setIdNumber(data.get(i).get(3));
            applicantBlackList.setLeaveDate(data.get(i).get(4));
            applicantBlackList.setRemark(data.get(i).get(5));
            applicantBlackList.setStatus("A");
            applicantBlackList.setLastModifyBy(employeeId);
            applicantBlackList.setLastModifyDate(now);
            applicantBlackListRepository.save(applicantBlackList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceBlackList(List<List<String>> data, String employeeId) {
        applicantBlackListRepository.deleteAll();
        uploadBlackList(data, employeeId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyBlackListData() {
        List<Integer> blackList = applicantBlackListRepository.searchBlackListedApplicant();

        List<ApplicantInfo> applicantList = applicantInfoRepository.findAll();

        for (ApplicantInfo applicantInfo : applicantList) {
            if (blackList.contains(applicantInfo.getApplicantInfoId())) {
                if (applicantInfo.getBlacklisted() == null || !applicantInfo.getBlacklisted()) {
                    applicantInfo.setBlacklisted(true);
                    applicantInfoRepository.save(applicantInfo);
                }
            } else {
                if (applicantInfo.getBlacklisted() != null && applicantInfo.getBlacklisted()) {
                    applicantInfo.setBlacklisted(false);
                    applicantInfoRepository.save(applicantInfo);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyUserPermissionData(String userList) {
        userPermissionRepository.applyUserPermissionData(userList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lostUserPermissionData(String lostStr) {
        userPermissionRepository.lostUserPermissionData(lostStr);

    }

//    @Override
//    public List<Message> getBlackListReasonList() {
//        return messageRepository.getBlackListReasonList();
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteBlackList(Integer[] blackList) {
        for (Integer id : blackList) {
            applicantBlackListRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRecruitmentGroup(Integer[] groupList) {
        for (Integer id : groupList) {
            recruitmentGroupRepository.deleteById(id);
        }
    }

    @Override
    public List<ProcessHistoryVO> findProcessHistoryByApplicantInfoId(Integer applicantInfoId) {
        List<Object[]> result = applicantPositionProcessRepository.findProcessHistoryByApplicantInfoId(applicantInfoId);
        return EntityUtils.castEntity(result, ProcessHistoryVO.class);
    }

    @Override
    public List<ProcessHistoryVO> findProcessHistoryByApplicantPosId(Integer applicant_pos_id) {
        List<Object[]> result = applicantPositionProcessRepository.findProcessHistoryByApplicantPosId(applicant_pos_id);
        return EntityUtils.castEntity(result, ProcessHistoryVO.class);
    }


    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    public ApplicantBlackList findByApplicantBlackListId(Integer applicantBlackListId) {
        return applicantBlackListRepository.findByApplicantBlackListId(applicantBlackListId);
    }
}
