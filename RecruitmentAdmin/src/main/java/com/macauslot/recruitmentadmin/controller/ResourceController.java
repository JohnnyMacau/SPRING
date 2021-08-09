package com.macauslot.recruitmentadmin.controller;

import com.alibaba.fastjson.JSONArray;
import com.macauslot.recruitmentadmin.annotation.UserLoginvalidation;
import com.macauslot.recruitmentadmin.entity.ApplicantSourceDetail;
import com.macauslot.recruitmentadmin.service.IHrService;
import com.macauslot.recruitmentadmin.service.RecruitmentService;
import com.macauslot.recruitmentadmin.util.ResponseResult;
import com.macauslot.recruitmentadmin.vo.ApplicationInterviewStatusSummaryVO;
import com.macauslot.recruitmentadmin.vo.CandidateApplicationReport1VO;
import com.macauslot.recruitmentadmin.vo.CandidateCollectingStatisticsVO;
import com.macauslot.recruitmentadmin.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: jim.deng
 * @Date: 2021/1/27 15:28
 */
@RestController
@RequestMapping("/ajax")

public class ResourceController {
    public static final Integer SUCCESS = 200;

    private final IHrService hrService;
    private final RecruitmentService recruitmentService;

    @Autowired
    public ResourceController(IHrService hrService, RecruitmentService recruitmentService) {
        this.hrService = hrService;
        this.recruitmentService = recruitmentService;
    }
    @UserLoginvalidation
    @GetMapping(value = "/msgDescSource", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<MessageVO>> getMsgDescSource(@RequestParam String applicationOrg) {
        List<MessageVO> list = hrService.getMsgDescSource(applicationOrg);
        ResponseResult<List<MessageVO>> listResponseResult = new ResponseResult<>(SUCCESS, list);
        System.err.println(listResponseResult);
        return listResponseResult;
    }
    @UserLoginvalidation
    @GetMapping(value = "/applicantSourceDetailList", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<ApplicantSourceDetail>> getApplicantSourceDetailList(Integer messageId) {
        List<ApplicantSourceDetail> list = hrService.getApplicantSourceDetailListByMessageId(messageId);
        return new ResponseResult<>(SUCCESS, list);
    }

    //3|應徵者申請總表
    @UserLoginvalidation
    @RequestMapping(value = "/report/candidateApplicationReport", produces = "application/json;charset=UTF-8")
    public ResponseResult<CandidateApplicationReport1VO> getCandidateApplicationReport(CandidateApplicationReport1VO condition) {
        CandidateApplicationReport1VO applicationReport1VO = recruitmentService.getCandidateApplicationReport1(condition);
        applicationReport1VO.setCandidateApplicationReport2VOList(recruitmentService.getCandidateApplicationReport2List(condition));
        return new ResponseResult<>(SUCCESS, applicationReport1VO);
    }


    //5.1|應徵者收集統計-應徵者申請來源
    @UserLoginvalidation
    @RequestMapping(value = "/report/candidateApplicationSources", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<CandidateCollectingStatisticsVO>> getCandidateApplicationSources(CandidateCollectingStatisticsVO condition) {
        List<CandidateCollectingStatisticsVO> applicationSourcesList = recruitmentService.getCandidateApplicationSourcesList(condition);

        return new ResponseResult<>(SUCCESS, applicationSourcesList);
    }

    //5.2|應徵者收集統計-應徵者學歷背景
    @UserLoginvalidation
    @RequestMapping(value = "/report/candidateEducationalBackground", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<CandidateCollectingStatisticsVO>> getCandidateEducationalBackground(CandidateCollectingStatisticsVO condition) {
        List<CandidateCollectingStatisticsVO> educationalBackgroundList = recruitmentService.getCandidateEducationalBackgroundList(condition);

        return new ResponseResult<>(SUCCESS, educationalBackgroundList);
    }

    //5.3|應徵者收集統計-應徵者年齡比例
    @UserLoginvalidation
    @RequestMapping(value = "/report/candidateAgeRatio", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<CandidateCollectingStatisticsVO>> getCandidateAgeRatio(CandidateCollectingStatisticsVO condition, @RequestParam String recruitmentForm) {
        List<CandidateCollectingStatisticsVO> ageRatioList = recruitmentService.getCandidateAgeRatioList(condition, recruitmentForm);

        return new ResponseResult<>(SUCCESS, ageRatioList);
    }

}
