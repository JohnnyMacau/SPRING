package com.macauslot.recruitment_ms.controller;

import com.macauslot.recruitment_ms.annotation.NoRepeatToken;
import com.macauslot.recruitment_ms.annotation.XsrfToken;
import com.macauslot.recruitment_ms.controller.exception.RequestException;
import com.macauslot.recruitment_ms.entity.*;
import com.macauslot.recruitment_ms.exception.*;
import com.macauslot.recruitment_ms.service.IApplicantInfoService;
import com.macauslot.recruitment_ms.service.ITokenService;
import com.macauslot.recruitment_ms.util.*;
import com.macauslot.recruitment_ms.vo.ApplicantPositionVO;
import com.macauslot.recruitment_ms.vo.VisitorSessionDataVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jim.deng
 */
@RequestMapping(value = "/applicant")
@RestController
public class ApplicantController {
    public static final Integer SUCCESS = 200;

    private final
    IApplicantInfoService applicantInfoService;

    private final
    ITokenService tokenService;

    @Autowired
    public ApplicantController(IApplicantInfoService applicantInfoService, ITokenService tokenService) {
        this.applicantInfoService = applicantInfoService;
        this.tokenService = tokenService;
    }


    @PostMapping(value = "/session", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<ApplicantInfo> handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session
    ) {
        ApplicantInfo visitor = applicantInfoService.login(username, password);
        setAttribute(session, visitor);
        return new ResponseResult<>(SUCCESS, visitor);
    }

    private void setAttribute(HttpSession session, ApplicantInfo visitor) {
        System.err.println("id1-->" + session.getId());
        session = SessionUtil.reGenerateSessionId();
        System.err.println("id2-->" + session.getId());
        Integer visitorId = visitor.getApplicantInfoId();
        SessionUtil.setSessionAttribute("visitorId", visitorId);
        List<ApplicantPosition> positionList = applicantInfoService.getAllByApplicantInfoIdAndProcessStage(visitorId);
        List<Integer> positionIdInApplicationList = positionList.stream().map(ApplicantPosition::getDeptPosDetailId).collect(Collectors.toList());
        visitor.setPositionIdInApplicationList(positionIdInApplicationList);



        SessionUtil.setSessionAttribute("username", visitor.getEmailAddress());

        SessionUtil.setSessionAttribute("reLogin", true);

        boolean isOldData = "O".equalsIgnoreCase(visitor.getStatus());
        SessionUtil.setSessionAttribute("OLD_DATA", isOldData);
        if (isOldData) {
            visitor.setIncompletePageNo(1);
        }
        SessionUtil.setSessionAttribute("incompletePageNo", visitor.getIncompletePageNo());

        System.err.println("用戶登錄,更換sessionID,獲得visitorId: <---->");
        String token = tokenService.getToken(EncryptUtil.getMd5Token(session.getId()));
        visitor.setToken(token);
    }

    @PostMapping(value = "/delete/session", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
//        Integer visitorId = SessionUtil.getVisitorIdFromSession();
//        System.err.println("visitorId-----" + visitorId);
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("mslot-ID".equals(name) ||
                        "status".equals(name) ||
                        "XSRF-TOKEN".equals(name)
                ) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
//                    System.err.println("刪除會話cookie");
                }


            }
        }
        return new ResponseResult<>(SUCCESS);
    }

    @XsrfToken
    @PostMapping(value = "/password", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> changePassword(
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password1") String newPassword,
            @RequestParam String type
    ) {
        if (!"password".equals(type)) {
            throw new RequestException("警告!繞過前端驗證");
        }

        Integer visitorId = SessionUtil.getVisitorIdFromSession();

        if (checkBlank(oldPassword).equals(newPassword)) {
            throw new RequestException("新密碼不能和舊密碼相同");
        }


        if (!TextValidator.checkPassword(newPassword)) {
            throw new RequestException("密碼必須由英文字母及數字組成且長度最少6個字");
        }

        applicantInfoService.changePassword(visitorId, oldPassword, newPassword);


        return new ResponseResult<>(SUCCESS);

    }

    @PostMapping(value = "/forgetPassword", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> forgetPassword(String idCardNumber, Integer idTypeId) {
        checkBlank(idCardNumber);
        applicantInfoService.forgetPassword(idCardNumber, idTypeId);
        return new ResponseResult<>(SUCCESS);
    }

    @XsrfToken
    @GetMapping(value = "/t1/data1", produces = "application/json;charset=UTF-8")
    public ResponseResult<ApplicantInfo> getTop1Data1() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        ApplicantInfo data;
        if (isLogIn) {
            data = applicantInfoService.getById(visitorId);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");
            }
            data = visitorRegistrationData.getApplicantInfo();
        }
        return new ResponseResult<>(SUCCESS, data);
    }

   /* @XsrfToken
    @GetMapping(value = "/t1/data2", produces = "application/json;charset=UTF-8")
    public ResponseResult<RelativeInfo> getTop1Data2() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        RelativeInfo data = applicantInfoService.getRelativeInfoByApplicantInfoId(visitorId);
        return new ResponseResult<>(SUCCESS, data);
    }*/

    @XsrfToken
    @GetMapping(value = "/t2/data", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<ApplicantPositionVO>> getTop2Data() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        List<ApplicantPositionVO> data = applicantInfoService.getHistoryApplicantPositionByApplicantInfoId(visitorId);
//        System.err.println(data);
        return new ResponseResult<>(SUCCESS, data);
    }

    @XsrfToken
    @GetMapping(value = "/t3/data1", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<Education>> getTop3Data1() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        List<Education> data;
        if (isLogIn) {
            data = applicantInfoService.getEducationByApplicantInfoId(visitorId);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");
            }
            data = visitorRegistrationData.getEducationList();
        }
        return new ResponseResult<>(SUCCESS, data == null ? Collections.emptyList() : data);
    }

    @XsrfToken
    @GetMapping(value = "/t3/data2", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<Certification>> getTop3Data2() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        List<Certification> data;
        if (isLogIn) {
            data = applicantInfoService.getCertificationByApplicantInfoId(visitorId);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");
            }
            data = visitorRegistrationData.getCertificationList();
        }
        return new ResponseResult<>(SUCCESS, data == null ? Collections.emptyList() : data);
    }

    @XsrfToken
    @GetMapping(value = "/t4/data", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<WorkingExperience>> getTop4Data() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        List<WorkingExperience> data;
        if (isLogIn) {
            data = applicantInfoService.getWorkingExperienceByApplicantInfoId(visitorId);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");
            }
            data = visitorRegistrationData.getWorkingExperienceList();
        }
        return new ResponseResult<>(SUCCESS, data == null ? Collections.emptyList() : data);
    }

    @XsrfToken
    @GetMapping(value = "/t5/data1", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<Language>> getTop5Data1() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        List<Language> data;
        if (isLogIn) {
            data = applicantInfoService.getLanguageByApplicantInfoId(visitorId);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");
            }
            data = visitorRegistrationData.getLanguageList();
        }
        return new ResponseResult<>(SUCCESS, data == null ? Collections.emptyList() : data);
    }

    @XsrfToken
    @GetMapping(value = "/t5/data2", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<OtherSkill>> getTop5Data2() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        boolean isLogIn = visitorId != null;
        List<OtherSkill> data;
        if (isLogIn) {
            data = applicantInfoService.getOtherSkillByApplicantInfoId(visitorId);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");
            }
            data = visitorRegistrationData.getOtherSkillList();
        }
        return new ResponseResult<>(SUCCESS, data == null ? Collections.emptyList() : data);
    }

    @XsrfToken
    @GetMapping(value = "/t6/data", produces = "application/json;charset=UTF-8")
    public ResponseResult<List<OnShift>> getTop6Data() {
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        List<OnShift> data = applicantInfoService.getOnShiftByApplicantInfoId(visitorId);
        return new ResponseResult<>(SUCCESS, data);
    }


    @GetMapping(value = "/pageNo", produces = "application/json;charset=UTF-8")
    public String getpageNo() {
        return String.valueOf(SessionUtil.getSessionAttribute("incompletePageNo"));
    }


    @PostMapping(value = "/checkSessionTimeout", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> checkSessionTimeout() {

        HttpSession session = SessionUtil.getSession();
        if (session == null) {
            System.err.println("checkSessionTimeout---session is null");
            throw new SessionTimeOutException();
        }

        return new ResponseResult<>(SUCCESS);

    }

    @PostMapping(value = "/checkLogin", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> checkLogin() {

        Integer id = SessionUtil.getVisitorIdFromSession();
        if (id == null) {
            System.err.println("checkLogin---VisitorId is null");
            throw new SessionTimeOutAfterLoginException();
        }
        if (applicantInfoService.checkOldData(id)) {
            throw new RejectOldDataException();
        }

        return new ResponseResult<>(SUCCESS);

    }

    @PostMapping(value = "/top0", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> prepareNewApplicant() {
        VisitorSessionDataVO visitorSessionDataVO = new VisitorSessionDataVO();
        visitorSessionDataVO.setApplicantInfo(new ApplicantInfo());
        SessionUtil.setSessionAttribute("visitorRegistrationData", visitorSessionDataVO);
        return new ResponseResult<>(SUCCESS);
    }

    @PostMapping(value = "/top1", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<ApplicantInfo> newApplicant(


            String enFName,
            String enLName,
            String cnFName,
            String cnLName,
            Integer idTypeId,
            String idCardNumber,
          /*  @RequestParam String year,
            @RequestParam String month,
            @RequestParam String day,*/
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,

            @RequestParam String areaCode,
            @RequestParam String mobile,
            @RequestParam(name = "contactPhoneAreaCode", required = false, defaultValue = "") String areaCode_2,

            @RequestParam(name = "contactPhone", required = false, defaultValue = "") String mobile_2,

            String emailAddress,
//            @RequestParam String nationality,//住址

            @RequestParam String address_1,//住址
            @RequestParam String address_2,//住址
            @RequestParam String address_3,//住址
            @RequestParam String area,//地區


            @RequestParam String gender,


//            @RequestParam String account_0, //戶口性質
//            @RequestParam String account_1,
//            @RequestParam String account_2,
//            @RequestParam String social, //社保狀況
//            @RequestParam String contactName,
//            @RequestParam String contactPhone,
//            @RequestParam String contactRelation,

//            @RequestParam String country,//"39">其他
//            @RequestParam String district,

            @RequestParam String martialStatus,

            @RequestParam String have_relative,
            @RequestParam String departmentName,

            @RequestParam String name,
            @RequestParam String relationship,
            @RequestParam String type,
            @RequestParam Integer inService,

            HttpSession session
    ) throws ParseException {


        if (!"profile".equals(type)) {
            throw new RequestException("警告!繞過前端驗證");
        }
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        if (visitorId == null) {
            checkReLogin(enFName, enLName, idTypeId, idCardNumber, emailAddress);
            if (!TextValidator.checkEmail(emailAddress) ||
                    !TextValidator.checkEnglish(enFName) ||
                    !TextValidator.checkEnglish(enLName)
//                ||
//                !TextValidator.checkEnglishChinese(contactName)
            ) {
                throw new RequestException("部份填寫內容格式不正確");
            }
        }
        if (
         /*       StringUtils.isBlank(year) ||
                        StringUtils.isBlank(month) ||
                        StringUtils.isBlank(day) ||*/
                dob == null ||
                        StringUtils.isBlank(areaCode) ||
                        StringUtils.isBlank(mobile) ||
//                        StringUtils.isBlank(nationality) ||
                        StringUtils.isBlank(area) ||
                        StringUtils.isBlank(address_1) ||
                        StringUtils.isBlank(address_2) ||
                        StringUtils.isBlank(address_3) ||

                        StringUtils.isBlank(martialStatus) ||

                        StringUtils.isBlank(gender) || "-1".equals(gender)
//                        ||
//                        StringUtils.isBlank(account_0) || "-1".equals(account_0) ||
//                        StringUtils.isBlank(account_1) || "-1".equals(account_1) ||
//                        StringUtils.isBlank(account_2) || "-1".equals(account_2) ||
//                        StringUtils.isBlank(social) || "-1".equals(social) ||
//                        StringUtils.isBlank(contactName) ||
//                        StringUtils.isBlank(contactPhone) ||
//                        StringUtils.isBlank(contactRelation)
        ) {
            throw new RequestException("部份內容未填寫");
        }

        ApplicantInfo p = new ApplicantInfo();
        if (StringUtils.isNotBlank(enFName) && StringUtils.isNotBlank(enLName)) {
            p.setEnFName(enFName.toUpperCase());
            p.setEnLName(enLName.toUpperCase());
        }


        if (StringUtils.isNotBlank(cnFName) && StringUtils.isNotBlank(cnLName)) {
//            System.err.println(cnFName);
//            System.err.println(cnLName);
            if (TextValidator.checkChinese(cnFName) && TextValidator.checkChinese(cnLName)) {
                p.setCnFName(cnFName);
                p.setCnLName(cnLName);
            } else {
                throw new RequestException("中文名格式錯誤");
            }
        }

        p.setIdTypeId(idTypeId);

//        if (idTypeId == 39) {
//
        if (visitorId == null) {
            if (!TextValidator.checkForeignID(idCardNumber)) {
                //                throw new RequestException("國外身份證格式錯誤");
                throw new RequestException("證件格式錯誤");

            }
        }

//            if (StringUtils.isBlank(country) || StringUtils.isBlank(district)) {
//                throw new RequestException("部份內容未填寫");
//            } else {
//                p.setCountry(country);
//                p.setDistrict(district);
//            }
//        } else {
//            if (!TextValidator.checkID(idCardNumber)) {
//                throw new RequestException("國內身份證格式錯誤");
//            }
//        }

        p.setIdCardNumber(idCardNumber);

/*
        Date dob = DateTool.getDate(year, month, day);
*/

        p.setDob(dob);
        p.setAreaCode(areaCode);
        p.setMobile(mobile);

        if (StringUtils.isNotBlank(mobile_2) && StringUtils.isNotBlank(areaCode_2)) {
            p.setContactPhone(mobile_2);
            p.setContactPhoneAreaCode(areaCode_2);
        } else {
            p.setContactPhone("");
            p.setContactPhoneAreaCode("");
        }


        p.setEmailAddress(emailAddress);
        p.setUserName(emailAddress);
//        p.setNationality(nationality);
        p.setArea(area);
        p.setAddress_1(address_1);
        p.setAddress_2(address_2);
        p.setAddress_3(address_3);

        p.setGender(gender);
//        p.setAccount(account_0 + "-" + account_1 + "-" + account_2);
//        p.setSocial(social);
//        p.setContactName(contactName);
//        p.setContactPhone(contactPhone);
//        p.setContactRelation(contactRelation);

        p.setMartialStatus(martialStatus);

        RelativeInfo r = null;
        if ("yes".equals(have_relative) && StringUtils.isNotBlank(name)
                && StringUtils.isNotBlank(departmentName)
                && StringUtils.isNotBlank(relationship)
                && inService != null && inService != -1) {
            r = new RelativeInfo();
            r.setName(name);
            r.setDepartmentName(departmentName);
            r.setRelationship(relationship);
            r.setInService(inService);
        }


//        System.err.println(p.toString());


        //沒有sessionId的話,應該去註冊
        if (visitorId == null) {

            applicantInfoService.checkDuplicate(p);

            VisitorSessionDataVO visitorSessionDataVO;
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");

            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");

            }


            visitorSessionDataVO = visitorRegistrationData;
            ApplicantInfo old_p = visitorSessionDataVO.getApplicantInfo();
            BeanCopyUtil.beanCopyExceptNull(p, old_p);
            visitorSessionDataVO.setApplicantInfo(old_p);

            RelativeInfo old_r = visitorSessionDataVO.getRelativeInfo();
            if (old_r != null) {
                BeanCopyUtil.beanCopyExceptNull(r, old_r);
                visitorSessionDataVO.setRelativeInfo(old_r);
            } else {
                visitorSessionDataVO.setRelativeInfo(r);
            }


            Integer incompletePageNo = getIncompletePageNo();
            if (incompletePageNo == null || incompletePageNo < 3) {
                SessionUtil.setSessionAttribute("incompletePageNo", 3);
            }

            SessionUtil.setSessionAttribute("visitorRegistrationData", visitorSessionDataVO);

//            ApplicantInfo visitor = applicantInfoService.regTop1(p, r);
//
//            setAttribute(session, visitor);


        }
        //有sessionId的話,去修改
        else {
            applicantInfoService.updateTop1(visitorId, p, r);
        }


        return new ResponseResult<>(SUCCESS);

    }

    private Integer getIncompletePageNo() {
        String strIncompletePageNo = String.valueOf(SessionUtil.getSessionAttribute("incompletePageNo"));
        if ("null".equalsIgnoreCase(strIncompletePageNo)) {
            return null;
        }
        return Integer.parseInt(strIncompletePageNo);
    }

    private void checkReLogin(String enFName, String enLName, Integer idTypeId, String idCardNumber, String emailAddress) {
        if (StringUtils.isBlank(enFName) ||
                StringUtils.isBlank(enLName) ||
                idTypeId == null || idTypeId == -1 ||
                StringUtils.isBlank(idCardNumber) ||
                StringUtils.isBlank(emailAddress)
        ) {
            throw new SessionTimeOutAfterLoginException("會話過期");
        }
    }

    @XsrfToken
    @NoRepeatToken(consumer = true, consumeMethodName = "hrTop2")
    @PostMapping(value = "/top2", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> newApplicant2(
//            @RequestParam Integer department_select1,
//            @RequestParam Integer title1,
//            @RequestParam Integer department_select2,
//            @RequestParam Integer title2,
//            @RequestParam Integer department_select3,
//            @RequestParam Integer title3,

            @RequestParam Integer topTitle,

            @RequestParam(value = "title", required = false) Integer[] titles,

            @RequestParam String expectedSalary,
            @RequestParam String expectedSalaryType,

         /*   @RequestParam String day,
            @RequestParam String month,
            @RequestParam String year,*/
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date availableWorkDate,

            @RequestParam Integer noticeDay,
            @RequestParam String noticeDayType,

            @RequestParam Integer sourceId,

            @RequestParam(required = false, defaultValue = "") String sourceRef,
            String sourceRef1,
            String sourceRef2,
            String sourceRef3,

            @RequestParam String type,

            @RequestParam Integer needShift,
            @RequestParam Integer onShift_total,

            @RequestParam(value = "dayofweek", required = false) String[] dayofweek,
            @RequestParam(value = "from_hour[]", required = false) Integer[] from_hour,
            @RequestParam(value = "from_min[]", required = false) Integer[] from_min,
            @RequestParam(value = "to_hour[]", required = false) Integer[] to_hour,
            @RequestParam(value = "to_min[]", required = false) Integer[] to_min,
            Integer applicantSourceDetailId,
            HttpServletRequest request

    ) throws ParseException {
        if (!"position".equals(type)) {
            throw new RequestException("警告!繞過前端驗證");
        }

        if (
//                department_select1 == null ||
//                        title1 == null ||
//                        department_select1 == -1 ||
//                        title1 == -1 ||
//                        department_select2 == null ||
//                        title2 == null ||
//                        department_select3 == null ||
//                        title3 == null ||
                topTitle == null ||
                        topTitle == -1 ||
                        StringUtils.isBlank(expectedSalary) ||
                        StringUtils.isBlank(expectedSalaryType) ||
                     /*   StringUtils.isBlank(noticeDayType) ||
                        StringUtils.isBlank(year) ||
                        StringUtils.isBlank(month) ||
                        StringUtils.isBlank(day) ||
                        noticeDay == null ||*/
                        sourceId == null ||

                        needShift == null ||
                        onShift_total == null
        ) {
            throw new RequestException("繞過前端驗證");
        }


//        System.err.println(department_select1);
//        System.err.println(title1);


        Integer visitorId = SessionUtil.getVisitorIdFromSession();

        List<ApplicantPosition> positionList = applicantInfoService.getAllByApplicantInfoIdAndProcessStage(visitorId);
        List<Integer> positionIdInApplicationList = positionList.stream().map(ApplicantPosition::getDeptPosDetailId).collect(Collectors.toList());
        if (positionIdInApplicationList.contains(topTitle)) {
            throw new AccessDeniedException("您已申請此職位");
        }
        if (positionList.size() >= 3) {
            throw new AccessDeniedException("目前您已有三個應徵申請處理中,請待工作人員處理完再申請");
        }


//        List<ApplicantPosition> applicantPositionList = new ArrayList<>();

        ApplicantPosition position = new ApplicantPosition();
        position.setApplicantInfoId(visitorId);
        position.setDeptPosDetailId(topTitle);
        position.setJobPriority(1);


        /*if (titles != null && titles.length > 0) {
            for (int i = 0; i < titles.length; i++) {
                ApplicantPosition applicantPosition = new ApplicantPosition();
                applicantPosition.setApplicantInfoId(visitorId);
                applicantPosition.setDeptPosDetailId(titles[i]);
                applicantPosition.setJobPriority(i + 2);
                applicantPositionList.add(applicantPosition);
            }
        }*/

        position.setExpectedSalary(expectedSalary);
        position.setExpectedSalaryType(expectedSalaryType);
        if (availableWorkDate != null) {
            position.setAvailableWorkDate(availableWorkDate);
        }
        if (noticeDay != null) {
            position.setNoticeDay(noticeDay);
            position.setNoticeDayType(noticeDayType);
        }

        position.setSourceId(sourceId);

        position.setSourceRef(sourceRef);

        if (StringUtils.isNotBlank(sourceRef1)) {
            position.setSourceRef(replaceWithComma(sourceRef1) + "||" + replaceWithComma(sourceRef2) + "||" + replaceWithComma(sourceRef3));
        }

        position.setApplicantSourceDetailId(applicantSourceDetailId);
        position.setProcessStage(1);

        ApplicantPosition dbData = applicantInfoService.updateTop2(visitorId, position);

        /*
        開始top6的工作...
         */


        if (needShift == 0) {
            applicantInfoService.updateTop6(visitorId, dbData.getApplicantPosId(), Collections.emptyList());
            return new ResponseResult<>(SUCCESS);
        }


        List<OnShift> onShiftList = new ArrayList<>();
        for (int i = 0; i < onShift_total; i++) {
            OnShift o = new OnShift();
//            o.setDayofweek(String.valueOf(i + 1));
//            o.setDayofweek("every day");
            o.setDayofweek(dayofweek[i]);
            o.setFromDate(from_hour[i] + ":" + from_min[i]);
            o.setToDate(to_hour[i] + ":" + to_min[i]);
            o.setApplicantInfoId(visitorId);
            o.setApplicantPositionId(dbData.getApplicantPosId());

            onShiftList.add(o);
        }
//        System.err.println(onShiftList);
        applicantInfoService.updateTop6(visitorId, dbData.getApplicantPosId(), onShiftList);

        return new ResponseResult<>(SUCCESS);


    }

    @XsrfToken
    @PostMapping(value = "/top3", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> newApplicant3(
            @RequestParam("education_count[]") Integer[] education_count,
            @RequestParam("certification_count[]") Integer[] certification_count,
            @RequestParam String type,

            HttpServletRequest request

    ) {
        if (!"education".equals(type)) {
            throw new RequestException("警告!繞過前端驗證");
        }

        Integer visitorId = SessionUtil.getVisitorIdFromSession();

        List<Education> educationList = new ArrayList<>();

        try {
            for (Integer num : education_count) {
                Education e = new Education();
                e.setDegree(checkBlank(request.getParameter("degree" + num)));
                e.setFromDate(checkBlank(request.getParameter("from_month" + num) + "-" + request.getParameter("from_year" + num)));
                e.setToDate(checkBlank(request.getParameter("to_month" + num) + "-" + request.getParameter("to_year" + num)));
                e.setOrganizationName(checkBlank(request.getParameter("organizationName" + num)));
                e.setMajor(checkBlank(request.getParameter("major" + num)));
                e.setApplicantInfoId(visitorId);


                e.setToDate4sort(DateUtils.parseDate(e.getToDate(), "M-yyyy"));

                educationList.add(e);
            }
        } catch (ParseException parseException) {
            throw new RequestException("日期格式不正確");
        }


        educationList = educationList.stream().sorted(Comparator.comparing(Education::getToDate4sort).reversed()).collect(Collectors.toList());


        List<Certification> certificationList = new ArrayList<>();
        for (Integer num : certification_count) {
            if (StringUtils.isBlank(request.getParameter("cert_name" + num))) {
                break;
            }
            Certification c = new Certification();
            c.setCertName(checkBlank(request.getParameter("cert_name" + num)));
            c.setIssueDate(checkBlank(request.getParameter("cert_month" + num) + "-" + request.getParameter("cert_year" + num)));
            c.setOrganizationName(checkBlank(request.getParameter("cert_organization_name" + num)));
            c.setApplicantInfoId(visitorId);
            certificationList.add(c);
        }

        if (visitorId != null) {
            applicantInfoService.updateTop3(visitorId, educationList, certificationList);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");

            }
            visitorRegistrationData.setEducationList(educationList);
            visitorRegistrationData.setCertificationList(certificationList);

            SessionUtil.setSessionAttribute("visitorRegistrationData", visitorRegistrationData);


            Integer incompletePageNo = getIncompletePageNo();
            if (incompletePageNo == null) {
                throw new SessionTimeOutInRegisterException("backend error: incompletePageNo == null");
            }
            if (incompletePageNo < 4) {
                SessionUtil.setSessionAttribute("incompletePageNo", 4);
            }
        }
        return new ResponseResult<>(SUCCESS);

    }


    @XsrfToken
    @PostMapping(value = "/top4", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> newApplicant4(
//            @RequestParam String have_experience,
            @RequestParam(value = "experience_count[]", required = false) Integer[] experience_count,
            @RequestParam String employed_before,
            @RequestParam String applied_baccount_before,

            String ebDept,
            String ebPosi,
//            String ebTime,
            Integer ebUserName,

            @RequestParam String terminated_before,
            String tbCause,
            @RequestParam String criminal_record,
            String crCause,

            @RequestParam String type,

            HttpServletRequest request

    ) {
        if (!"experience".equals(type)) {
            throw new RequestException("警告!繞過前端驗證");
        }


        Integer visitorId = SessionUtil.getVisitorIdFromSession();


        List<WorkingExperience> workingExperienceList = new ArrayList<>();


//        if ("yes".equals(checkBlank(have_experience))) {
        for (Integer num : experience_count) {
            WorkingExperience w = new WorkingExperience();
            w.setCompanyName(trimToEmpty(request.getParameter("companyName" + num)));
            String from_month = request.getParameter("from_month" + num);
            String from_year = request.getParameter("from_year" + num);
            w.setFromDate((from_month + "-" + from_year));
            w.setPosition(trimToEmpty(request.getParameter("position" + num)));
            w.setPayMethod((request.getParameter("payMethod" + num)));
            w.setCurrency((request.getParameter("currency" + num)));
            w.setSalary(trimToEmpty(request.getParameter("salary" + num)));
//            w.setAllowance(request.getParameter("allowance" + num));//津貼可以為空
            w.setAllowance("");
            w.setJobDuty(trimToEmpty(request.getParameter("jobDuty" + num)));
            w.setStillEmployed(trimToEmpty(request.getParameter("stillEmployed" + num)));


//                if ("Y".equals(w.getStillEmployed())) {


            String leaveReason = trimToEmpty(request.getParameter("leaveReason" + num));
            if ("其他".equals(leaveReason)) {
                w.setLeaveReason(trimToEmpty(request.getParameter("leaveOtherReason" + num)));
                System.err.println("OOO");
            } else {
                w.setLeaveReason(leaveReason);//離職原因可以為空
            }

            String to_month = request.getParameter("to_month" + num);
            String to_year = request.getParameter("to_year" + num);
            w.setToDate((to_month + "-" + to_year));
//                }else {
//
//                }

            w.setApplicantInfoId(visitorId);

            //全部為空則不要錄入list
            if (StringUtils.isBlank(w.getCompanyName()) &&
                    StringUtils.isBlank(from_month) &&
                    StringUtils.isBlank(from_year) &&
                    StringUtils.isBlank(w.getPosition()) &&
                    StringUtils.isBlank(w.getSalary()) &&
                    StringUtils.isBlank(w.getAllowance()) &&
                    StringUtils.isBlank(w.getJobDuty()) &&
                    StringUtils.isBlank(w.getStillEmployed()) &&
                    StringUtils.isBlank(w.getLeaveReason()) &&
                    StringUtils.isBlank(to_month) &&
                    StringUtils.isBlank(to_year)
            ) {
            } else {
                workingExperienceList.add(w);
            }
        }
//        }

        ApplicantInfo a = new ApplicantInfo();
        // employed_before=Y&
        // ebDept=2&
        // ebPosi=2&
        // ebTime=2&

        if ("Y".equals(checkBlank(employed_before))) {
            a.setEmployedBefore("Y");
            a.setEbDept(checkBlank(ebDept));
            a.setEbPosi(checkBlank(ebPosi));
//            a.setEbTime(checkBlank(ebTime));
            int tmp = ebUserName == null ? 0 : ebUserName;
            a.setEbUserName(tmp);

        } else {
            a.setEmployedBefore("N");
        }

        if ("Y".equals(checkBlank(applied_baccount_before))) {
            a.setAppliedBaccountBefore("Y");
        } else {
            a.setAppliedBaccountBefore("N");
        }


        // terminated_before=Y&
        // tbCause=3333&
        if ("Y".equals(checkBlank(terminated_before))) {
            a.setTerminatedBefore("Y");
            a.setTbCause(checkBlank(tbCause));
        } else {
            a.setTerminatedBefore("N");
        }

        // criminal_record=Y&
        // crCause=4444&
        if ("Y".equals(checkBlank(criminal_record))) {
            a.setCriminalRecord("Y");
            a.setCrCause(checkBlank(crCause));
        } else {
            a.setCriminalRecord("N");
        }

        // delete_id=&************
        // type=experience"


        if (visitorId != null) {
            applicantInfoService.updateTop4(visitorId, workingExperienceList, a);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");

            }
            visitorRegistrationData.setWorkingExperienceList(workingExperienceList);
            ApplicantInfo old_a = visitorRegistrationData.getApplicantInfo();
            BeanCopyUtil.beanCopyExceptNull(a, old_a);
            old_a.setIncompletePageNo(5);//前端要用
            visitorRegistrationData.setApplicantInfo(old_a);

            SessionUtil.setSessionAttribute("visitorRegistrationData", visitorRegistrationData);
            Integer incompletePageNo = getIncompletePageNo();
            if (incompletePageNo == null) {
                throw new SessionTimeOutInRegisterException("backend error: incompletePageNo == null");
            }
            if (incompletePageNo < 5) {
                SessionUtil.setSessionAttribute("incompletePageNo", 5);
            }
        }
        return new ResponseResult<>(SUCCESS);

    }

    @XsrfToken
    @NoRepeatToken(consumer = true, consumeMethodName = "hrTop5")
    @PostMapping(value = "/top5", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> newApplicant5(
            @RequestParam("language_count[]") Integer[] language_count,
//            @RequestParam("other_skill_count[]") Integer[] other_skill_count,
            @RequestParam String type,

            HttpServletRequest request

    ) {
        if (!"language".equals(type)) {
            throw new RequestException("警告!繞過前端驗證");
        }


        Integer visitorId = SessionUtil.getVisitorIdFromSession();


        List<Language> languageList = new ArrayList<>();


        for (Integer num : language_count) {
            Language l = new Language();
            l.setName(checkBlank(request.getParameter("lanauage_name" + num)));


            l.setWritten(checkBlank(request.getParameter("written" + num)));
            l.setSpoken(checkBlank(request.getParameter("conversation" + num)));
//            if (num == 3) {
//                l.setCertificate(checkBlank(request.getParameter("certificate" + num)));
//            }

            l.setApplicantInfoId(visitorId);
            languageList.add(l);
        }


        List<OtherSkill> otherSkillList = new ArrayList<>();

      /*  for (Integer num : other_skill_count) {
            OtherSkill o = new OtherSkill();
            o.setName(checkBlank(request.getParameter("other_skill_name" + num)));
            o.setDegree(checkBlank(request.getParameter("other_skill" + num)));
            o.setApplicantInfoId(visitorId);
            otherSkillList.add(o);
        }*/
        for (int i = 1; i <= 2; i++) {
            OtherSkill o = new OtherSkill();
            o.setName(trimToEmpty(request.getParameter("other_skill_name" + i)));
            o.setDegree(trimToEmpty(request.getParameter("other_skill" + i)));
            if (i == 1) {
                o.setSkillType("c");
            } else {
                o.setSkillType("o");
            }
            o.setApplicantInfoId(visitorId);
            otherSkillList.add(o);
        }

        if (visitorId != null) {
            applicantInfoService.updateTop5(visitorId, languageList, otherSkillList);
        } else {
            VisitorSessionDataVO visitorRegistrationData = (VisitorSessionDataVO) SessionUtil.getSessionAttribute("visitorRegistrationData");
            if (visitorRegistrationData == null) {
                throw new SessionTimeOutInRegisterException("填表超時,未完成註冊,資料會流失");

            }
            visitorRegistrationData.setLanguageList(languageList);
            visitorRegistrationData.setOtherSkillList(otherSkillList);

            ApplicantInfo dbdata = persistenceVistorSessionData(visitorRegistrationData);

            SessionUtil.setSessionAttribute("visitorRegistrationData", null);
            Integer incompletePageNo = getIncompletePageNo();
            if (incompletePageNo == null) {
                throw new SessionTimeOutInRegisterException("backend error: incompletePageNo == null");
            }
            if (incompletePageNo < 7) {
                SessionUtil.setSessionAttribute("incompletePageNo", 7);
            }
            SessionUtil.setSessionAttribute("visitorId", dbdata.getApplicantInfoId());


        }
        return new ResponseResult<>(SUCCESS);

    }

    /**
     * 棄用
     */
   /* @XsrfToken
    @PostMapping(value = "/top6", consumes = "application/x-www-form-urlencoded;charset=utf-8", produces = "application/json;charset=UTF-8")
    public ResponseResult<Void> newApplicant6(
            @RequestParam(value = "from_hour[]", required = false) Integer[] from_hour,
            @RequestParam(value = "from_min[]", required = false) Integer[] from_min,
            @RequestParam(value = "to_hour[]", required = false) Integer[] to_hour,
            @RequestParam(value = "to_min[]", required = false) Integer[] to_min
    ) {
//        System.err.println(Arrays.toString(from_hour));
        Integer visitorId = SessionUtil.getVisitorIdFromSession();
        List<OnShift> onShiftList = new ArrayList<>();
        if (
                from_hour == null ||
                        from_min == null ||
                        to_hour == null ||
                        to_min == null ||
                        from_hour.length == 0 ||
                        from_min.length == 0 ||
                        to_hour.length == 0 ||
                        to_min.length == 0
        ) {
            applicantInfoService.updateTop6(visitorId, onShiftList);
            return new ResponseResult<>(SUCCESS);
        }


//        for (int i = 0; i < 7; i++) {
        for (int i = 0; i < 1; i++) {
            OnShift o = new OnShift();
//            o.setDayofweek(String.valueOf(i + 1));
            o.setDayofweek("every day");

            o.setFromDate(from_hour[i] + ":" + from_min[i]);
            o.setToDate(to_hour[i] + ":" + to_min[i]);
            o.setApplicantInfoId(visitorId);
            onShiftList.add(o);
        }

        applicantInfoService.updateTop6(visitorId, onShiftList);

        return new ResponseResult<>(SUCCESS);


    }*/
    private ApplicantInfo persistenceVistorSessionData(VisitorSessionDataVO visitorSessionDataVO) {
        return applicantInfoService.insertVistorSessionData(visitorSessionDataVO);
    }


    private static String checkBlank(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RequestException("有值為空");
        }
        return str;
    }

    private static String replaceWithComma(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.replaceAll("\\|\\|", ",");
    }

    private static String trimToEmpty(String str) {
        return StringUtils.trimToEmpty(str);
    }


}
