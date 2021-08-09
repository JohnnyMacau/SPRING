package com.macauslot.recruitment_ms.controller;

import com.macauslot.recruitment_ms.entity.ApplicantPosition;
import com.macauslot.recruitment_ms.entity.ApplicantSourceDetail;
import com.macauslot.recruitment_ms.service.IApplicantInfoService;
import com.macauslot.recruitment_ms.service.IDepartmentPostService;
import com.macauslot.recruitment_ms.service.IMessageService;
import com.macauslot.recruitment_ms.util.ResponseResult;
import com.macauslot.recruitment_ms.util.SessionUtil;
import com.macauslot.recruitment_ms.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jim.deng
 */
@RestController
//@RequestMapping(consumes = "application/x-www-form-urlencoded;charset=utf-8",produces = "application/json;charset=UTF-8")
public class ResourceController {
    public static final Integer SUCCESS = 200;

    private final
    IDepartmentPostService departmentPostService;
    private final
    IMessageService messageService;
    private final
    IApplicantInfoService applicantInfoService;

    @Autowired
    public ResourceController(IDepartmentPostService departmentPostService, IMessageService messageService, IApplicantInfoService applicantInfoService) {
        this.departmentPostService = departmentPostService;
        this.messageService = messageService;
        this.applicantInfoService = applicantInfoService;
    }


    @GetMapping(value = "/positionDetail", produces = "application/json;charset=UTF-8")
    public ResponseResult<PositionDetailVO> getPositionDetail() {
        List<DeptPositionDetailVO> list = departmentPostService.getDepartmentPositionDetail();
        Integer visitorId = SessionUtil.getVisitorIdFromSession();

        List<ApplicantPosition> positionList = applicantInfoService.getAllByApplicantInfoIdAndProcessStage(visitorId);

//        System.err.println(visitorId);
//        System.err.println(positionList);
        return new ResponseResult<>(SUCCESS, new PositionDetailVO(list,positionList.size()>=3,positionList));
//        return new ResponseResult<>(SUCCESS, new PositionDetailVO(list,true,positionList));

    }

    @GetMapping(value = "/positionDesc",produces = "application/json;charset=UTF-8")
    public ResponseResult<List<DeptPositionDescVO>> getPositionDetail(@RequestParam("post_id") Integer deptPosDetailId) {
        List<DeptPositionDescVO> data = departmentPostService.getDepartmentPositionDesc(deptPosDetailId);
        System.err.println(data);
        return new ResponseResult<>(SUCCESS, data);
    }


    @GetMapping(value = "/msgDescCard",produces = "application/json;charset=UTF-8")
    public ResponseResult<List<MessageVO>> getMsgDescCard() {
        List<MessageVO> list = messageService.getMsgDescCard();
        return new ResponseResult<>(SUCCESS, list);
    }

    @GetMapping(value = "/msgDescSource",produces = "application/json;charset=UTF-8")
    public ResponseResult<List<MessageVO>> getMsgDescSource() {
        List<MessageVO> list = messageService.getMsgDescSource();
        return new ResponseResult<>(SUCCESS, list);
    }


    @GetMapping(value = "/deptDesc",produces = "application/json;charset=UTF-8")
    public ResponseResult<List<DeptVO>> getDeptDesc() {
        List<DeptVO> list = departmentPostService.getDepartmentDesc();
        return new ResponseResult<>(SUCCESS, list);
    }


    @GetMapping(value = "/applicantSourceDetailList", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<ApplicantSourceDetail>> getApplicantSourceDetailList(Integer messageId) {
        List<ApplicantSourceDetail> list = applicantInfoService.getApplicantSourceDetailListByMessageId(messageId);
        return new ResponseResult<>(SUCCESS, list);
    }


}
