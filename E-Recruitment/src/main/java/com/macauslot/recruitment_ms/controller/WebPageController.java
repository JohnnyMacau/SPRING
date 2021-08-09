package com.macauslot.recruitment_ms.controller;

import com.macauslot.recruitment_ms.annotation.NoRepeatToken;
import com.macauslot.recruitment_ms.entity.ApplicantInfo;
import com.macauslot.recruitment_ms.entity.ApplicantPosition;
import com.macauslot.recruitment_ms.entity.RelativeInfo;
import com.macauslot.recruitment_ms.service.IApplicantInfoService;
import com.macauslot.recruitment_ms.service.IDepartmentPostService;
import com.macauslot.recruitment_ms.service.IMessageService;
import com.macauslot.recruitment_ms.service.ITokenService;
import com.macauslot.recruitment_ms.util.EncryptUtil;
import com.macauslot.recruitment_ms.util.SessionUtil;
import com.macauslot.recruitment_ms.vo.DeptPositionDescVO;
import com.macauslot.recruitment_ms.vo.JobApplyHistoryVO;
import com.macauslot.recruitment_ms.vo.MessageVO;
import com.macauslot.recruitment_ms.vo.VisitorSessionDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author jim.deng
 */

@Controller

public class WebPageController {

    private final
    IDepartmentPostService departmentPostService;
    private final
    IApplicantInfoService applicantInfoService;
    private final
    IMessageService messageService;
    private final
    ITokenService tokenService;

    @Value("${contact_phoneNumbers}")
    private String[] contact_phoneNumbers;



    @Autowired
    public WebPageController(IDepartmentPostService departmentPostService, IApplicantInfoService applicantInfoService, IMessageService messageService, ITokenService tokenService) {
        this.departmentPostService = departmentPostService;
        this.applicantInfoService = applicantInfoService;
        this.messageService = messageService;
        this.tokenService = tokenService;
    }


    @GetMapping(value = "/hr_post_detail.html")
    public String hrPostDetail(@RequestParam("post_id") Integer deptPosDetailId, Model model) {
        List<DeptPositionDescVO> voList = departmentPostService.getDepartmentPositionDesc(deptPosDetailId);
        model.addAttribute("vo", voList.get(0));
        model.addAttribute("deptPosDetailId",deptPosDetailId);
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        List<ApplicantPosition> positionList = applicantInfoService.getAllByApplicantInfoIdAndProcessStage(visitorId);
        model.addAttribute("suspendAllApplicant",positionList.size()>=3);
        boolean thisOneLock = positionList.stream().map(ApplicantPosition::getDeptPosDetailId).anyMatch(x -> x.equals(deptPosDetailId));
        model.addAttribute("thisOneLock",thisOneLock);
        return "/th_hr/hr_post_detail";
    }

    @GetMapping("/hr_top_m1")
    public String hr_top_m1(Model model, HttpSession session) {

        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        if (isLogIn) {
            return "forward:/hr_top1.html";
        } else {
            return "forward:/hr_top_m1.html";
        }
    }

    @GetMapping("/hr_top1.html")
    public String hrTop1(Model model, HttpSession session) {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        List<MessageVO> messageVOList = messageService.getMsgDescCard();
        model.addAttribute("messageVOList", messageVOList);
        boolean isLogIn = visitorId != null;
        model.addAttribute("isLogIn", isLogIn);
        Object incompletePageNo = SessionUtil.getSessionAttribute("incompletePageNo");

        if (isLogIn) {
//            System.err.println("有登錄---" + visitorId);
            ApplicantInfo applicantInfo = applicantInfoService.getById(visitorId);
            RelativeInfo relativeInfo = applicantInfoService.getRelativeInfoByApplicantInfoId(visitorId);
            model.addAttribute("applicantInfo", applicantInfo);
            model.addAttribute("relativeInfo", relativeInfo);

        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData != null) {
                model.addAttribute("isSessionData", incompletePageNo != null);

                model.addAttribute("applicantInfo", visitorRegistrationData.getApplicantInfo());
                model.addAttribute("relativeInfo", visitorRegistrationData.getRelativeInfo());
            }
            String token = tokenService.getToken(EncryptUtil.getMd5Token(session.getId()));
            model.addAttribute("token", token);

        }

        model.addAttribute("incompletePageNo", incompletePageNo);
        model.addAttribute("reLogin", SessionUtil.getSessionAttribute("reLogin"));
        return "/th_hr/hr_top1";
    }

    @NoRepeatToken(producer = true)
    @GetMapping("/hr_top2.html/{dept_pos_detail_id}")
    public String hrTop2(HttpServletRequest request, Model model, @PathVariable("dept_pos_detail_id") Integer deptPosDetailId) {
       /* System.err.println(deptPosDetailId);
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        model.addAttribute("isLogIn", isLogIn);
        model.addAttribute("incompletePageNo", SessionUtil.getSessionAttribute("incompletePageNo"));
        model.addAttribute("reLogin", SessionUtil.getSessionAttribute("reLogin"));*/


        List<DeptPositionDescVO> deptPositionDescVOList = departmentPostService.getDepartmentPositionDesc(deptPosDetailId);

//        System.err.println(deptPositionDescVOList);
        model.addAttribute("DeptPositionDescVO", deptPositionDescVOList.get(0));


        return "/th_hr/hr_top2";
    }


    @GetMapping("/hr_top3.html")
    public String hrTop3(Model model) {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        model.addAttribute("isLogIn", isLogIn);
        model.addAttribute("incompletePageNo", SessionUtil.getSessionAttribute("incompletePageNo"));
        model.addAttribute("reLogin", SessionUtil.getSessionAttribute("reLogin"));
        return "/th_hr/hr_top3";
    }

    @GetMapping("/hr_top4.html")
    public String hrTop4(Model model) {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        model.addAttribute("isLogIn", isLogIn);
        model.addAttribute("incompletePageNo", SessionUtil.getSessionAttribute("incompletePageNo"));
        model.addAttribute("reLogin", SessionUtil.getSessionAttribute("reLogin"));
        return "/th_hr/hr_top4";
    }

    @NoRepeatToken(producer = true)
    @GetMapping("/hr_top5.html")
    public String hrTop5(HttpServletRequest request, Model model) {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        model.addAttribute("isLogIn", isLogIn);
        model.addAttribute("incompletePageNo", SessionUtil.getSessionAttribute("incompletePageNo"));
        model.addAttribute("reLogin", SessionUtil.getSessionAttribute("reLogin"));
        return "/th_hr/hr_top5";
    }

    @GetMapping("/hr_top7.html")
    public String hrTop7(Model model) {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;

        Boolean reLogin = (Boolean) SessionUtil.getSessionAttribute("reLogin");

        if (reLogin != null && reLogin) {
            //查db
            //todo
            List<JobApplyHistoryVO> jobApplyHistory = applicantInfoService.getJobApplyHistory(visitorId);
            model.addAttribute("jobApplyHistoryList", jobApplyHistory);
        }

        model.addAttribute("isLogIn", isLogIn);
        model.addAttribute("incompletePageNo", SessionUtil.getSessionAttribute("incompletePageNo") );
        model.addAttribute("reLogin", reLogin);
        model.addAttribute("contact_phoneNumbers",String.join(",", contact_phoneNumbers));

        if (isLogIn) {
            List<ApplicantPosition> positionList = applicantInfoService.getAllByApplicantInfoIdAndProcessStage(visitorId);
            model.addAttribute("suspendAllApplicant", positionList.size() >= 3);
        } else {
            model.addAttribute("suspendAllApplicant", false);
        }


//        System.err.println("top7");
        return "/th_hr/hr_top7";
    }


    @GetMapping("/hr_top7_apply.html/{dept_pos_detail_id}")
    public String hrTop7_apply(Model model, @PathVariable ("dept_pos_detail_id") Integer deptPosDetailId) {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;


        Boolean reLogin = (Boolean) SessionUtil.getSessionAttribute("reLogin");

        if (reLogin!= null && reLogin) {
            List<JobApplyHistoryVO> jobApplyHistory = applicantInfoService.getJobApplyHistory(visitorId);
            model.addAttribute("jobApplyHistoryList", jobApplyHistory);
        }

        model.addAttribute("isLogIn", isLogIn);
        model.addAttribute("incompletePageNo", SessionUtil.getSessionAttribute("incompletePageNo"));
        model.addAttribute("reLogin", reLogin);

//        System.err.println("top7");
        return "/th_hr/hr_top7_apply";
    }



    @GetMapping("/hr_contact_us.html")
    public String hrContactUs(Model model) {
        model.addAttribute("contact_phoneNumbers",contact_phoneNumbers);
        return "/th_hr/hr_contact_us";
    }

    @GetMapping("/hr_top_m2.html")
    public String hr_top_m2(Model model) {
        model.addAttribute("contact_phoneNumbers",String.join(",", contact_phoneNumbers));
        return "/th_hr/hr_top_m2";
    }

}
