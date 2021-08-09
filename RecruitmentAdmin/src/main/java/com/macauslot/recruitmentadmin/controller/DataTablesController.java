package com.macauslot.recruitmentadmin.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.macauslot.recruitmentadmin.annotation.UserLoginvalidation;
import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.entity.DepartmentPosition;
import com.macauslot.recruitmentadmin.entity.Message;
import com.macauslot.recruitmentadmin.entity.RecruitmentGroup;
import com.macauslot.recruitmentadmin.entity.User;
import com.macauslot.recruitmentadmin.exception.UserSkipValidation;
import com.macauslot.recruitmentadmin.service.RecruitmentService;
import com.macauslot.recruitmentadmin.util.PageInfo;
import com.macauslot.recruitmentadmin.util.ResponseResult;
import com.macauslot.recruitmentadmin.util.ServerRoleTagEnum;
import com.macauslot.recruitmentadmin.vo.ApplicantInfoVO;
import com.macauslot.recruitmentadmin.vo.ApplicantJobDetailVO;
import com.macauslot.recruitmentadmin.vo.ApplicantionRecordReportVO;
import com.macauslot.recruitmentadmin.vo.ApplicationHistoryVO;
import com.macauslot.recruitmentadmin.vo.ApplicationInterviewStatusSummaryVO;
import com.macauslot.recruitmentadmin.vo.ApplicationVO;
import com.macauslot.recruitmentadmin.vo.BlackListVO;
import com.macauslot.recruitmentadmin.vo.CandidateInformationReportVO;
import com.macauslot.recruitmentadmin.vo.CommonVO;
import com.macauslot.recruitmentadmin.vo.DeptVO;
import com.macauslot.recruitmentadmin.vo.JobVO;
import com.macauslot.recruitmentadmin.vo.LoginUserVO;
import com.macauslot.recruitmentadmin.vo.ProcessHistoryVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentGroupMemberVO;
import com.macauslot.recruitmentadmin.vo.RecruitmentProgressVO;

@RestController
@RequestMapping("/dataTables")
public class DataTablesController {


    private static final Integer SUCCESS = 200;
    private final RecruitmentService recruitmentService;

    @Autowired
    public DataTablesController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @UserLoginvalidation
    @RequestMapping("/getSourceType")
    public PageInfo<Message> getSourceType(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            Message condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4ApplicantSourceType(draw, start, length, condition, sidx, sord);
    }

    @UserLoginvalidation
    @RequestMapping("/getBlackList")
    public PageInfo<BlackListVO> getBlackList(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            BlackListVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4BlackList(draw, start, length, sidx, sord, condition);
    }

    @UserLoginvalidation
    @RequestMapping("/getLoginPermission")
    public PageInfo<LoginUserVO> getLoginPermission(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "3") Integer length,
            LoginUserVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4UserList(draw, start, length, sidx, sord, condition);
    }

    @UserLoginvalidation
    @RequestMapping("/getRecruitmentGroup")
    public PageInfo<RecruitmentGroupMemberVO> getRecruitmentGroup(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            RecruitmentGroupMemberVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
//        System.err.println(sidx+","+sord);
        return recruitmentService.getPage4RecruitmentGroup(draw, start, length, sidx, sord, condition);
    }

    @UserLoginvalidation
    @GetMapping("/getDepartmentPositionList")
    public ResponseResult<List<DepartmentPosition>> getDepartmentPositionList(String departmentCode, int orgId) {
        List<DepartmentPosition> positionList = recruitmentService.getDepartmentPositionList(departmentCode, orgId, "A");
        return new ResponseResult<>(SUCCESS, positionList);
    }

    @UserLoginvalidation
    @GetMapping("/getRecruitmentGroupList")
    public ResponseResult<List<RecruitmentGroup>> getRecruitmentGroupList(String departmentCode) {
        List<RecruitmentGroup> recruitmentGroupList = recruitmentService.getRecruitmentGroupListByDepartmentCode(departmentCode);
        System.err.println(recruitmentGroupList);
        return new ResponseResult<>(SUCCESS, recruitmentGroupList);
    }


    @UserLoginvalidation
    @GetMapping("/getOrganizationDepartmentList")
    public ResponseResult<List<DeptVO>> getOrganizationDepartmentList(Integer orgId) {
        List<DeptVO> departmentList = recruitmentService.getOrganizationDepartmentList(orgId);
        return new ResponseResult<>(SUCCESS, departmentList);
    }

    @UserLoginvalidation
    @RequestMapping("/getJobList")
    public PageInfo<JobVO> getJobList(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            JobVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4JobDetail(draw, start, length, condition, sidx, sord);
    }

    @UserLoginvalidation
    @GetMapping("/getOpenJobList")
    public PageInfo<JobVO> getOpenJobList(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            ApplicantJobDetailVO condition) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4OpeningJob(draw, start, length, sidx, sord, new char[]{'A', 'O'});
    }

    /*
    /application/search_d.html 用，deprecated
    @UserLoginvalidation
    @RequestMapping("/getApplicationList")
    public PageInfo<ApplicantJobDetailVO> getApplicationList(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            ApplicantJobDetailVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4ApplicantJobDetail(draw, start, length, condition, sidx, sord);
    }*/

    //1.申請記錄總表
    @UserLoginvalidation
    @RequestMapping("/getApplicantReportList")
    public PageInfo<ApplicantionRecordReportVO> getApplicantReportList(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            ApplicantionRecordReportVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4ApplicantReport(draw, start, length, condition, sidx, sord);
    }

    //2.應徵者資料總表
    @UserLoginvalidation
    @RequestMapping("/getCandidateInformationReportList")
    public PageInfo<CandidateInformationReportVO> getCandidateInformationReportList(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            CandidateInformationReportVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.getPage4CandidateInformationReport(draw, start, length, condition, sidx, sord);
    }

    //4.1|職位申請情況
    @UserLoginvalidation
    @RequestMapping(value = "/getJobApplicationStatisticsList")
    public JSONArray getJobApplicationStatisticsList(ApplicationInterviewStatusSummaryVO condition) {
        if (StringUtils.isBlank(condition.getApplicationOrg()) ||
                condition.getStartDate() == null ||
                condition.getEndDate() == null) {
            throw new UserSkipValidation("backend 搜尋條件不能為空");
        }
        return recruitmentService.getJobApplicationStatisticsList(condition);
    }

    //4.2|HR面試情況
    @UserLoginvalidation
    @RequestMapping(value = "/getHRInterviewsStatisticsList")
    public JSONArray getHRInterviewsStatisticsList(ApplicationInterviewStatusSummaryVO condition) {
        if (StringUtils.isBlank(condition.getApplicationOrg()) ||
                condition.getStartDate() == null ||
                condition.getEndDate() == null) {
            throw new UserSkipValidation("backend 搜尋條件不能為空");
        }
        condition.setStage("4");
        return recruitmentService.getInterviewsStatisticsList(condition);
    }

    //4.3|部門面試情況
    @UserLoginvalidation
    @RequestMapping(value = "/getDepartmentInterviewsStatisticsList")
    public JSONArray getDepartmentInterviewsStatisticsList(ApplicationInterviewStatusSummaryVO condition) {
        if (StringUtils.isBlank(condition.getApplicationOrg()) ||
                condition.getStartDate() == null ||
                condition.getEndDate() == null) {
            throw new UserSkipValidation("backend 搜尋條件不能為空");
        }
        condition.setStage("5");
        return recruitmentService.getInterviewsStatisticsList(condition);
    }


    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/searchApplication")
    public PageInfo<ApplicationVO> searchApplication(
            HttpServletRequest request,
            UserDTO userDTO,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            ApplicationVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");

        if(condition.getStatus()!=null && condition.getStatus().compareTo("a")==0) {
        	condition.setStatus("A,O");
        }

        if (userDTO.getRoleTag() == ServerRoleTagEnum.HR &&
                request.getParameter("is_dept_reply_html") == null) {
            condition.setUser_dept(null);//HR且非访问dept_reply.html的话，不筛选部门,不檢查群組
            return recruitmentService.searchApplication(draw, start, length, condition, sidx, sord);
        }

        Boolean isFLTAdmin = userDTO.getIsFLTAdmin();
        if (isFLTAdmin != null && isFLTAdmin &&
                request.getParameter("is_arrange_interview_html") != null) {
            condition.setUser_dept(null);//FLTAdmin且访问arrange_interview.html的话，不筛选部门
            condition.setOrg("FLT");//筛选, 只能看FLT[Cathie,Debbie,Kitty可以見到安排約見版面,只可以見到FLT數據]
            return recruitmentService.searchApplication(draw, start, length, condition, sidx, sord);
        }

        //非HR 或者 HR但访问dept_reply.html 的话，只有关联部门的关联用户才能查看对应数据！
        return recruitmentService.searchApplication(userDTO.getEmployeeId(), draw, start, length, condition, sidx, sord);
    }


    @UserLoginvalidation
    @RequestMapping("/searchApplicant")
    public PageInfo<ApplicantInfoVO> searchApplicant(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length,
            ApplicantInfoVO condition
    ) {
        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");
        return recruitmentService.searchApplicant(draw, start, length, condition, sidx, sord);
    }

    @UserLoginvalidation
    @GetMapping("/getReturnStatusList")
    public ResponseResult<List<Message>> getReturnStatusList(int currentStage) {
        List<Message> returnStatusList = recruitmentService.getReturnStatusList(currentStage);
        return new ResponseResult<>(SUCCESS, returnStatusList);
    }

    @UserLoginvalidation(serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @GetMapping("/getRecruitmentProgress")
    public PageInfo<RecruitmentProgressVO> getRecruitmentProgress(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") Integer draw,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length) {

        String sidx = request.getParameter("columns[" +
                request.getParameter("order[0][column]") +
                "][data]");
        String sord = request.getParameter("order[0][dir]");

        UserDTO user = ((UserDTO) request.getSession().getAttribute("user"));

        return recruitmentService.page4RecruitmentProgress(draw, start, length, sidx, sord, user);

    }


    @UserLoginvalidation(serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @RequestMapping("/getJobStageHeadCount")
    public ResponseResult getJobStageHeadCount(
            HttpServletRequest request,
            @RequestParam Integer jobId
    ) {
        List<CommonVO> list = recruitmentService.getJobStageHeadCount(jobId);
        return new ResponseResult<>(SUCCESS, list);
    }

    @UserLoginvalidation
    @RequestMapping("/getApplicationStageProcessTime")
    public ResponseResult getApplicationStageProcessTime(
            HttpServletRequest request,
            @RequestParam Integer applicant_position_id
    ) {
        List<CommonVO> list = recruitmentService.getApplicationStageProcessTime(applicant_position_id);
        return new ResponseResult<>(SUCCESS, list);
    }

    @UserLoginvalidation
    @RequestMapping("/getApplicationHistory")
    public ResponseResult getApplicationHistory(
            HttpServletRequest request,
            @RequestParam Integer applicantInfoId
    ) {
        List<ApplicationHistoryVO> list = recruitmentService.getApplicationHistory(applicantInfoId);
        return new ResponseResult<>(SUCCESS, list);
    }


    @UserLoginvalidation
    @RequestMapping("/getApplicantProcessHistory")
    public ResponseResult getApplicantProcessHistory(
            HttpServletRequest request,
            @RequestParam Integer applicantInfoId
    ) {
        List<ProcessHistoryVO> list = recruitmentService.findProcessHistoryByApplicantInfoId(applicantInfoId);
        return new ResponseResult<>(SUCCESS, list);
    }

    @UserLoginvalidation
    @RequestMapping("/getApplicationProcessHistory")
    public ResponseResult getApplicationProcessHistory(
            HttpServletRequest request,
            @RequestParam Integer applicantPosId
    ) {
        List<ProcessHistoryVO> list = recruitmentService.findProcessHistoryByApplicantPosId(applicantPosId);
        return new ResponseResult<>(SUCCESS, list);
    }


    @UserLoginvalidation
    @GetMapping("/setting/getUserListLike")
    public ResponseResult<List<User>> getUserListLike(HttpServletRequest request, RecruitmentGroupMemberVO groupMemberVO) {
        String employeeId = groupMemberVO.getEmployeeId();
        if (StringUtils.isBlank(employeeId)) {
            return new ResponseResult<>(SUCCESS);
        }
        List<User> userList = recruitmentService.getUserListByUserNameContaining(employeeId);
        return new ResponseResult<>(SUCCESS, userList);
    }

    @UserLoginvalidation
    @GetMapping("/setting/getEmployeeByDeptCode")
    public ResponseResult<List<User>> getEmployeeByDeptCode(HttpServletRequest request, String deptCode) {
        List<User> userList = recruitmentService.getUserListByDeptCode(deptCode);
//        System.err.println(userList);
        return new ResponseResult<>(SUCCESS, userList);
    }
}
