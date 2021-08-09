package com.macauslot.recruitmentadmin.util.pdf;

import com.macauslot.recruitmentadmin.entity.*;
import com.macauslot.recruitmentadmin.util.TimeEnum;
import com.macauslot.recruitmentadmin.vo.ApplicantInfoVO;
import com.macauslot.recruitmentadmin.vo.ApplyDataVO;
import com.macauslot.recruitmentadmin.vo.ResumeVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.List;

public class PdfResumeHtmlStyleMaker {

    private static String filterReturnVal(String str) {
        return StringUtils.isBlank(str) ? "" : str;
    }

    private static String filterReturnValAndAddBrackets(String str) {
        return StringUtils.isBlank(str) ? "" : "(" + str + ")";
    }


    public static String setExportPageForResume(ResumeVO resume) {
        String Ep_html = "";

        try {
            List<ApplyDataVO> applyDataVOList = resume.getApplyDataVOList();
            ApplicantInfoVO applicantInfoVO = resume.getApplicantInfoVO();
            List<Message> messageList = resume.getMessageList();
            List<Education> educationList = resume.getEducationList();
            List<Certification> certificationList = resume.getCertificationList();
            List<WorkingExperience> experienceList = resume.getExperienceList();
            List<Language> languageList = resume.getLanguageList();
            List<OtherSkill> otherSkillList = resume.getOtherSkillList();


            String cnl_name = filterReturnVal(applicantInfoVO.getCnLName()); //中文姓
            String cnf_name = filterReturnVal(applicantInfoVO.getCnFName()); //中文名
            String enl_name = applicantInfoVO.getEnLName(); //英文姓
            String enf_name = applicantInfoVO.getEnFName(); //英文名
            String id_type = ""; //身份證類別
            for (Message message : messageList) {
                if (applicantInfoVO.getType_id().equals(message.getId())) {
                    id_type = message.getChnDesc();
                }
            }


            String id_no = applicantInfoVO.getIdCardNumber(); //編號
//String m_status=crsPersonalInfo.getString("MARTIAL_STATUS"); //婚姻狀況
            String m_status = applicantInfoVO.getMartialStatus(); //婚姻狀況

            String birthdayOrAgeValue; //出生日期or年齡
            String ageCalculateByDob = applicantInfoVO.getAgeCalculateByDob();
            if (StringUtils.isNotBlank(ageCalculateByDob)) {
                birthdayOrAgeValue = ageCalculateByDob;
            } else {
                birthdayOrAgeValue = dateToString(applicantInfoVO.getDob());
            }
            String area_code = filterReturnValAndAddBrackets(applicantInfoVO.getAreaCode()); //手提電話区号
            String mobile = filterReturnVal(applicantInfoVO.getMobile()); //手提電話
            String email = applicantInfoVO.getEmailAddress(); //電郵地址
//    String address = crsPersonalInfo.getString("NATIONALITY"); //住址
            String address = filterReturnVal(applicantInfoVO.getAddress_1()) + " " + filterReturnVal(applicantInfoVO.getAddress_2()) + " " + filterReturnVal(applicantInfoVO.getAddress_3());

            String country = applicantInfoVO.getCountry();//国家
            String district = applicantInfoVO.getDistrict();//地区
            String account = filterReturnVal(applicantInfoVO.getAccount());//户口性质
            String so_status = filterReturnVal(applicantInfoVO.getSocial());//社保状况
//紧急联系
            String contact_name = filterReturnVal(applicantInfoVO.getContactName());//姓名
//	contact_phone_area_code
            String contact_phone_area_code = filterReturnValAndAddBrackets(applicantInfoVO.getContactPhoneAreaCode());//电话区号

            String contact_phone = filterReturnVal(applicantInfoVO.getContactPhone());//电话
            String contact_relation = filterReturnVal(applicantInfoVO.getContactRelation());//关系

            String moneyType_application_org = applicantInfoVO.getApplicationOrg();//工资币种按公司区分
            if ("FLT".equalsIgnoreCase(moneyType_application_org)) {
                moneyType_application_org = "RMB";
            } else {
                moneyType_application_org = "MOP";
            }


            String gender = applicantInfoVO.getCnGender(); //性別


            String yes_tmp = "沒有";
            String relative_name = "";
            String department = "";
            String relationship = "";
            String rowContentPsi = "";
            String rel_in_service = "";


            if (applicantInfoVO.getRelationship() != null) //有親屬於本公司任職
            {
                yes_tmp = "有";
                relative_name = applicantInfoVO.getRelative_name(); //親屬姓名
                department = applicantInfoVO.getDepartment(); //親屬所屬部門
                relationship = applicantInfoVO.getRelationship(); //關係
                rel_in_service = applicantInfoVO.getCnIn_service(); //任职状态


                rowContentPsi = "<tr>"
                        + "<td>親屬姓名	</td><td class='tb_bt'>&#160;&#160;" + relative_name + "</td>"
                        + "<td>所屬部門</td><td class='tb_bt'>&#160;&#160;" + htmlEscape(department) + "</td>"
                        + "<td>關係</td><td class='tb_bt'>&#160;&#160;" + relationship + "</td>"
                        + "<td>任職狀態</td><td class='tb_bt'>&#160;&#160;" + rel_in_service + "</td>"

                        + "</tr>";

            }
            /* ---------- 個人資料 end ----------- */

            /* ---------- 教育程度 start ---------- */
            String rowContentEdu = "";

            int count_edu = 6; //如果填不夠6項就要補足

            for (Education education : educationList) { //教育程度
                //學歷，開始日期，結束日期，學校，專科
                rowContentEdu +=
                        "<tr>" +
                                "<td>" + education.getDegree() + "</td><td>" +
                                education.getOrganizationName() + "</td><td>" +
                                education.getFromDate() + "</td><td>" +
                                education.getToDate() + "</td><td>" +
                                education.getMajor() + "</td>" +
                                "</tr>";

                count_edu--;
            }

//如果填不夠6項就要補足
            for (int i = 1; i <= count_edu; i++) {
                rowContentEdu +=
                        "<tr>" +
                                "<td>&#160;</td><td>&#160;" +
                                "</td><td>&#160;" +
                                "</td><td>&#160;" +
                                "</td><td>&#160;" +
                                "</td>" +
                                "</tr>";
            }
            /* ---------- 教育程度 end ---------- */

            /* ---------- 專業資格/證書 start ------------ */
            String rowContentCert = "";

            int count_cert = 3; //如果填不夠3項就要補足

            for (Certification certification : certificationList) {
                //發證機構，頒發日期，證書/認可資格
                rowContentCert +=
                        "<tr>" +
                                "<td>" + certification.getCertName() + "</td><td>" +
                                certification.getIssueDate() + "</td><td>" +
                                certification.getOrganizationName() + "</td>" +
                                "</tr>";

                count_cert--;
            }

//如果填不夠3項就要補足
            for (int j = 1; j <= count_cert; j++) {
                rowContentCert +=
                        "<tr>" +
                                "<td>&#160;</td><td>&#160;" +
                                "</td><td>&#160;" +
                                "</td>" +
                                "</tr>";
            }


            /* ---------- 專業資格/證書 end ------------ */

            /* ---------- 工作經驗 start ------------ */
            String rowContentExp = "";

            int count_exp = 3; //如果填不夠3項就要補足


            for (WorkingExperience workingExperience : experienceList) {

                String pay_method = workingExperience.getPayMethod();


                rowContentExp +=
                        "<tr  valign='top' align='left'>"
                                + "<td style='height:20px;' class='td_l' valign='middle'>&#160;&#160;公司</td>" +
                                "<td class='td_lb'  align='center'>"
                                + workingExperience.getFromDate() + "</td><td class='td_lb' align='center'>"
                                + workingExperience.getToDate() + "</td><td class='td_lb' align='center'>(" + pay_method + ") "
                                + workingExperience.getCurrency() + "$ "
                                + workingExperience.getSalary()
                                + "</td>" +
                                //                        "<td class='td_lb' align='center'>" + crsExperience.getString("ALLOWANCE") + "</td>" +
                                "<td class='td_lb' align='center'>"
                                + workingExperience.getStillEmployed()
                                + "</td><td class='td_lbr' align='center'>"
                                + workingExperience.getLeaveReason()
                                + "</td>"
                                + "</tr>"
                                + "<tr style='height:40px;' valign='top' align='left'>"
                                + "<td class='td_lb'>&#160;&#160;"
                                + workingExperience.getCompanyName()
                                + "</td><td colspan='5'  class='td_lr' valign='middle'>&#160;&#160;職務</td>"
                                + "</tr>"
                                + "<tr style='height:60px;'  valign='top' align='left'>"
                                + "<td class='td_lb'>&#160;&#160;職位<br/>&#160;&#160;"
                                + workingExperience.getPosition()
                                + "</td>" +
                                "<td colspan='5' valign='top'  class='td_lbr'>&#160;&#160;"
                                + workingExperience.getJobDuty()
                                + "</td>"
                                + "</tr>";

                count_exp--;
            }

//如果填不夠3項就要補足
            for (int k = 1; k <= count_exp; k++) {
                rowContentExp +=
                        "<tr  valign='top' align='left'>"
                                + "<td style='height:20px;' class='td_l' valign='middle'>&#160;&#160;公司</td>" +
                                "<td class='td_lb'>&#160;</td><td class='td_lb'>&#160;</td>" +
                                "<td class='td_lb'><br/>&#160; "
                                + "&#160;</td>" +
                                "<td class='td_lb'>&#160;</td>" +
                                //                            "<td class='td_lb'>&#160;</td>" +
                                "<td class='td_lbr'>&#160;</td>"
                                + "</tr>"
                                + "<tr style='height:40px;' valign='top' align='left'>"
                                + "<td class='td_lb'>&#160;&#160;&#160;</td><td colspan='5'  class='td_lr' valign='middle'>&#160;&#160;職務</td>"
                                + "</tr>"
                                + "<tr style='height:60px;'  valign='top' align='left'>"
                                + "<td class='td_lb'>&#160;&#160;職位<br/>&#160;&#160;&#160;</td><td colspan='5' valign='top'  class='td_lbr'>&#160;&#160;&#160;</td>"
                                + "</tr>";
            }


            String workedBefore = "";
            String appliedBaccountBefore = "";

            String terminatedBefore = "";
            String criminalRecord = "";

//是否曾在本公司工作
            if ("是".equals(applicantInfoVO.getEmployedBefore())) {
                workedBefore = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>是否曾在本公司工作</td>" +
                        "<td width='10%' class='tb_bt' >是</td>" +
                        "<td width='5%'>部門:</td>" +
                        "<td class='tb_bt'>" + applicantInfoVO.getEbDept() + "</td>" +
                        "<td width='5%'>職位:</td>" +
                        "<td class='tb_bt'>" + applicantInfoVO.getEbPosi() + "</td>" +
                        "<td width='5%'>員工編號:</td>" +
                        "<td class='tb_bt'>" + applicantInfoVO.getEbUserName() + "</td>" +
                        "</tr></table>";
            } else {
                workedBefore = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>是否曾在本公司工作</td>" +
                        "<td width='10%' align='left'>否</td>" +
                        "<td width='45%'></td>" +
                        "</tr></table>";
                //+"<td>是否曾在本公司工作&#160;&#160;&#160;&#160;"+wb_tmp+"&#160;&#160;&#9674;&#8727;是（部門<u>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;</u>職位<u>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;</u>年資<u>&#160;&#160;&#160;&#160;&#160;&#160;&#160;</u>）</td>"
            }


//是否曾申請本公司之投注戶口
            if ("FLT".equalsIgnoreCase(applicantInfoVO.getApplicationOrg())) {

            } else if ("是".equals(applicantInfoVO.getAppliedBaccountBefore())) {
                appliedBaccountBefore = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>是否曾申請本公司之投注戶口</td>" +
                        "<td width='10%' align='left'>是</td>" +
                        "<td width='45%'></td>" +
                        "</tr></table>";
            } else {
                appliedBaccountBefore = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>是否曾申請本公司之投注戶口</td>" +
                        "<td width='10%' align='left'>否</td>" +
                        "<td width='45%'></td>" +
                        "</tr></table>";
            }


//是否曾被某個顧主終止/開除/暫停職務
            if ("是".equals(applicantInfoVO.getTerminatedBefore())) {
                terminatedBefore = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>曾被某個僱主終止/開除/暫停職務</td>" +
                        "<td width='10%' class='tb_bt' >是</td>" +
                        "<td width='5%'>(原因</td>" +
                        "<td width='40%' class='tb_bt'>&#160;&#160;"
                        + applicantInfoVO.getTbCause()
                        + "</td><td>)</td></tr></table>";
            } else {
                terminatedBefore = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>曾被某個僱主終止/開除/暫停職務</td>" +
                        "<td width='10%' align='left'>否</td>" +
                        "<td width='45%'></td>" +
                        "</tr></table>";
                //+"<td>是否曾被某個顧主終止/開除/暫停職務&#160;&#160;&#160;&#160;"+tb_tmp+"&#160;&#160;&#9674;&#160;&#8727;是（原因<u>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;</u>）</td>"
            }

//曾在澳門或其他國家/地區涉及任何刑事罪行
            if ("是".equals(applicantInfoVO.getCriminalRecord())) {
                criminalRecord = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>曾在澳門或其他國家/地區涉及任何刑事罪行</td>" +
                        "<td width='10%' class='tb_bt' >是</td>" +
                        "<td width='5%'>(原因</td>" +
                        "<td width='40%' class='tb_bt'>&#160;&#160;"
                        + applicantInfoVO.getCrCause()
                        + "</td><td>)</td></tr></table>";
            } else {
                criminalRecord = "<table width='100%' align='left' cellpadding='0' cellspacing='0'><tr>" +
                        "<td width='45%'>曾在澳門或其他國家/地區涉及任何刑事罪行</td>" +
                        "<td width='10%' align='left'>否</td>" +
                        "<td width='45%'></td>" +
                        "</tr></table>";
                //+"<td>是否曾在澳門或其它國家/地區涉及任何刑事罪行&#160;&#160;&#160;&#160;"+cr_tmp+"&#160;&#160;&#9674;&#8727;是（原因<u>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;</u>）</td>"
            }
            /* ---------- 工作經驗 end ------------ */




            /* --------- 其它技能 start ----------- */
            String rowContentSkillComputer_content = "";
            String rowContentSkillOT_tr = "";
        /*    System.err.println("TTTTT1:  " + (otherSkillList.size() == 2));
            System.err.println("TTTTT2:  " + ("電腦技能".equals(otherSkillList.get(0).getName())));
            System.err.println("TTTTT3:  " + (otherSkillList.get(1).getName()));*/


            if (otherSkillList.size()==2 &&
                    "電腦技能".equals(otherSkillList.get(0).getName()) &&
                    "其他技能".equals(otherSkillList.get(1).getName())
            ) {
                /**
                 * 新數據有且只有2條.
                 */

                rowContentSkillComputer_content =  otherSkillList.get(0).getDegree();
                OtherSkill otherSkill1 = otherSkillList.get(1);
                rowContentSkillOT_tr = "<tr>"
                        + "<td width='25.1%'>&#160;其它技能</td><td>&#160;&#160;" + otherSkill1.getDegree() + "</td>"
                        + "</tr>";

            }else {
                /**
                 * 兼容舊數據用
                 */
                String rowContentSkillOT = "";
                for (OtherSkill otherSkill : otherSkillList) {
                    rowContentSkillOT += otherSkill.getName() +
                            "[" + otherSkill.getDegree() + "]&#160;&#160;&#160;&#160;";
                }
                System.err.println("rowContentSkillOT = " + rowContentSkillOT);
                rowContentSkillOT_tr = "<tr>"
                        + "<td width='25.1%'>&#160;電腦技能 / 其它技能</td><td>&#160;&#160;" + rowContentSkillOT + "</td>"
                        + "</tr>";
            }

            /* --------- 其它技能 end ----------- */


            /* --------- 語言能力/ start --------- */
            String rowContentLang = "";
//String rowContentSkill = "";
            int count = 0;


            for (Language language : languageList) {
                rowContentLang +=
                        "<tr align='center'>";
                rowContentLang +=
                        "<td >" + language.getName() + "</td>" +
                                "<td >" + language.getWritten() + "</td>" +
                                "<td >" + language.getSpoken() + "</td>";

                if (count==0) {
                    rowContentLang +=
                            "<td rowspan='" + languageList.size() +
                                    "'>" + rowContentSkillComputer_content + "</td>";
                }

        /*    if (crsLanguage.getString("certificate") == null) {
                rowContentLang += "<td ></td>";
            } else {
                rowContentLang += "<td >" + crsLanguage.getString("certificate") + "</td>";
            }*/



        /* if(crsOtherSkill.next()){
            rowContentLang +=  "<td>"+crsOtherSkill.getString("NAME") +
            "</td><td>" + crsOtherSkill.getString("DEGREE") + "</td>";
        }else{
            rowContentLang +=
                "<td ></td>" +
                "<td ></td>";
        } */


                rowContentLang +=
                        "</tr>";
                count++;
            }
 /*   if (count < crsOtherSkill.size()) {
        for (int k = 0; k < crsOtherSkill.size() - count; k++) {
            rowContentLang += "<tr><td ></td><td ></td><td ></td><td ></td></tr>";
        }
    }*/

            /* --------- 語言能力 end ----------- */

            /*--------------電腦技能 start-------------------*/

      /*       String rowContentSkill = "";
            int count2 = 0;
           while (crsOtherSkill.next()) {
                rowContentSkill += "<tr align='center'>"
                        + "<td>" + crsOtherSkill.getString("NAME") + "</td>"
                        + "<td>" + crsOtherSkill.getString("DEGREE") + "</td>"
                        + "</tr>";

                count2++;
            }
            if (count2 < languageList.size()) {
                for (int k = 0; k < languageList.size() - count; k++) {
                    rowContentSkill += "<tr><td ></td><td ></td></tr>";
                }
            }*/
            /*--------------電腦技能 end-------------------*/




            String notice_day_type = applicantInfoVO.getNotice_day_type();
            if ("day".equalsIgnoreCase(notice_day_type)) {
                notice_day_type = "日";
            } else if ("month".equalsIgnoreCase(notice_day_type)) {
                notice_day_type = "月";
            } else {
                notice_day_type = "";
            }
            Ep_html += "<html><head><meta http-equiv='content-type' content='text/html; charset=UTF-8' />"
                    + "<style type='text/css' >"
                    + "body {font-family: SimSun;font-size:13px;font-weight:bold;}.pageNext {page-break-after: always;}"
                    + ".dataTable {border:1px solid;border-collapse:collapse;width:100%;table-layout:fixed;}"
                    + ".AutoNewline {word-break: break-all;width:100px;text-overflow:ellipsis;word-wrap : break-word ;overflow:auto;}"
                    + ".dataTable td,.dataTable tr {border:1px solid;border-collapse:collapse;height:25px;}"
                    + ".dataTable1 {border:1px solid;border-collapse:collapse;width:100%;}"
                    + ".fly {font-size:25px;font-weight:bold;}"
                    + ".out{border-top:40px #D6D3D6 solid;width:0px;height:0px;border-left:80px #BDBABD solid;position:relative;}"
                    + ".tb_bt{border-bottom:1px solid;}"
                    + ".td_bd{border-left:1px solid;border-top:1px solid;border-bottom:1px solid;}"
                    + ".td_lb{border-left:1px solid;border-bottom:1px solid;}"
                    + ".td_lr{border-left:1px solid;border-right:1px solid;}"
                    + ".td_lbr{border-left:1px solid;border-bottom:1px solid;border-right:1px solid;}"
                    + ".td_l{border-left:1px solid;}"
                    + ".td_all{border:1px solid;}"
                    + ".PageNext{page-break-after:always;}"
                    + "</style>"
                    + "</head>"
                    + "<body class='pageNext'>";

            String whetherYouNeedToWorkInShifts_tr = "";



            if (applyDataVOList != null && applyDataVOList.size() > 0) {
                ApplyDataVO applyDataVO = applyDataVOList.get(0);
                List<OnShift> onShiftList = applyDataVOList.get(0).getOnShiftList();
                String job_code = applyDataVO.getCode(); //招聘序號
                //String applicant_iid=crsCV1.getString("APPLICANT_INFO_ID"); //申請者編號
                String applicant_iid = ""; //申請者編號由人手填寫
                String cr_date = dateToString(applyDataVO.getCr_date()); //申請日期
                String name = applyDataVO.getDeptName(); //部門
                String job_title = applyDataVO.getJob_title(); //職位


                String source = applyDataVO.getSource(); //如何得知職位空缺

                /* ---------- 個人資料  start ----------- */
                String ep_salary = applyDataVO.getExpected_salary(); //要求待遇
                String expected_salary_type = applyDataVO.getExpected_salary_type();


                String notice_day = String.valueOf(applyDataVO.getNotice_day());  //預先通知期
                String work_date = dateToString(applyDataVO.getAvailable_work_date()); //預計可到任日期
                String source_ref = StringUtils.trimToEmpty(applyDataVO.getSource_ref()); //得知職位空缺的详细出处
                String applicant_source_detail_desc = StringUtils.trimToEmpty(applyDataVO.getApplicant_source_detail_desc());

                /* --------- 是否需要輪班工作 start ----------- */
                String rowContentShift = "";
                String Shift_tmp = "不需要";
                rowContentShift += "（註明時間&#160;&#160;";


                for (int i = 0; i < onShiftList.size(); i++) {
                    OnShift onShift = onShiftList.get(i);
                    if (onShift.getDayofweek() != null) {
                        Shift_tmp = "需要";
                        if (i > 0) {
                            rowContentShift += " | ";
                        }
                        rowContentShift += onShift.getDayofweek() + "   " +
                                "由   " + onShift.getFromDate() + "  " +
                                "至   " + onShift.getToDate();

                    }
                }
                rowContentShift += "&#160;&#160;）";
                whetherYouNeedToWorkInShifts_tr = "<tr>"
                        + "<td>&#160;是否需要輪班工作</td><td>&#160;&#160;" + Shift_tmp + "&#160;&#160;" + rowContentShift + "</td>"
                        + "</tr>";
                /* --------- 是否需要輪班工作 end ----------- */

                Ep_html += "<div align='center'>"
                        + "<font class='fly'><strong>職位申請表</strong></font>"
                        + "</div>"
                        + "<br/>"
                        + "<table border='1' align='center' class='dataTable' cellpadding='5' cellspacing='0'>"
                        + "<tr>"
                        + "<td width='100%' colspan='6'>人才資源部填寫</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td width='10%'>招聘序號</td><td width='25%'>" + job_code + "</td>"
                        + "<td width='10%'>申請編號</td><td width='25%'>" + applicant_iid + "</td>"
                        + "<td width='10%'>申請日期</td><td width='25%'>" + cr_date + "</td>"
                        + "</tr>"
                        + "</table><br/>"
                        + "<table width='100%'  border='1' align='center' class='dataTable1'>"
                        + "<tr>"
                        + "<td>"
                        + "<table width='100%' align='center' border='0' cellpadding='5' >"
                        + "<tr style='height:30px;'>"
                        + "<td width='8%' align='left'>部門</td>"
                        + "<td align='left' class='tb_bt'>&#160;&#160;" + htmlEscape(name) + "</td>"
                        + "<td width='8%' align='left'>如何得知此職位空缺</td>"
                        + "<td width='20%' class='tb_bt'>" + source + " " + source_ref + " " + applicant_source_detail_desc + "</td>"
                        //					+"<td width='8%' align='left'>出處</td>"
                        //					+"<td width='20%' class='tb_bt'>"+source_ref+"</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td width='8%' align='left'>職位</td>"
                        + "<td width='20%' align='left'  class='tb_bt'>&#160;&#160;" + job_title + "</td>"
                        + "<td width='8%' align='left'>類別</td>"
                        + "<td width='20%' class='tb_bt'>全職/時薪</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td colspan='4' align='left'>"
                        + "<table width='100%' align='center'>"
                        + "<tr style='height:30px;'>"
                        + "<td width='8%' align='left'>要求待遇</td>"
                        + "<td width='20%' class='tb_bt'>(" + moneyType_application_org +
                        ")&#160;&#160;" + expected_salary_type + " " + ep_salary + "</td>"
                        + "<td align='left'>預先通知期</td>"
                        + "<td width='20%' align='left'  class='tb_bt'>&#160;&#160;" + notice_day + " " + notice_day_type + "</td>"
                        + "<td align='left'>預計可到任日期</td>"
                        + "<td width='20%' align='left' class='tb_bt'>&#160;&#160;" + work_date + "</td>"
                        + "</tr>"

                        /*
                        + "<tr>"
                        + "<td>是否需要輪班工作</td><td colspan='4'>&#160;&#160;" + Shift_tmp + "&#160;&#160;" + rowContentShift + "</td>"
                        + "</tr>"
                        */

                        + "</table>"
                        + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "</td>"
                        + "</tr>"
                        + "</table><br/><br/><br/>";

            }

            Ep_html += "<div align='center'>"
                    + "<font style='font-size:20px'>個人資料</font>"
                    + "<hr width='100%' size='5' color='#00ffff' style='border:3 double black' />"
                    + "</div>"
                    + "<table width='100%' align='center' border='0' cellpadding='5' cellspacing='0'>"
                    + "<tr>"
                    + "<td>"
                    + "<table width='100%' align='left' border='0' cellpadding='5' cellspacing='0'>"
                    + "<tr  style='height:30px;'>"
                    + "<td width='10%' align='left'>姓名（英文）</td><td width='25%' align='left' class='tb_bt'>&#160;&#160;" + enl_name + "&#160;&#160;" + enf_name + "</td><td width='10%' align='left'>（中文）</td><td width='15%' align='left' class='tb_bt'>&#160;&#160;" + cnl_name + "&#160;&#160;" + cnf_name + "</td>"
                    + "</tr>"
                    + "<tr style='height:30px;'>"
                    + "<td  align='left'>身份證類別</td><td  align='left' class='tb_bt'>&#160;&#160;" + id_type + "</td><td  align='left'>編號</td><td align='left'  class='tb_bt'>&#160;&#160;" + id_no + "</td>"
                    + "</tr>"
                    + "<tr style='height:30px;'>"
                    + "<td  align='left'>戶口性質</td><td  align='left' class='tb_bt'>&#160;&#160;" + account + "</td><td  align='left'>社保狀況</td><td align='left'  class='tb_bt'>&#160;&#160;" + so_status + "</td>"
                    + "</tr>";
            if ("".equals(country) && "".equals(gender)) {
                Ep_html += "<tr style='height:30px;'>"
                        + "<td  align='left'>國家</td><td align='left' class='tb_bt'>&#160;&#160;" + country + "</td><td  align='left'>地區</td><td align='left' class='tb_bt'>&#160;&#160;" + district + "</td>"
                        + "</tr >";

            }

            String birthdayOrAgeKey;
            if (StringUtils.isNotBlank(ageCalculateByDob)) {
                birthdayOrAgeKey = "年齡";
            } else {
                birthdayOrAgeKey = "出生日期";
            }



            Ep_html += "<tr style='height:30px;'>"
                    + "<td  align='left'>" + birthdayOrAgeKey + "</td><td align='left' class='tb_bt'>&#160;&#160;" + birthdayOrAgeValue + "</td><td  align='left'>性別</td><td align='left' class='tb_bt'>&#160;&#160;" + gender + "</td>"
                    + "</tr >"
                    + "<tr style='height:30px;'>"
                    + "<td  align='left'>手提電話</td><td  align='left' class='tb_bt'>&#160;&#160;" + area_code + "&#160;" + mobile + "</td><td  align='left'>婚姻狀況</td><td align='left' class='tb_bt'>&#160;&#160;" + m_status + "</td>"
                    + "</tr>"
                    + "<tr  style='height:30px;'>"
                    + "<td  align='left'>電郵地址</td><td  align='left' colspan='3' class='tb_bt'>&#160;&#160;" + email + "</td>"
                    + "</tr>"
                    + "<tr style='height:30px;' >"
                    + "<td align='left'>住址</td><td align='left' colspan='3' class='tb_bt'>&#160;&#160;" + address + "</td>"
                    + "</tr>"
                    + "<tr style='height:30px;' >"
                    + "<td align='left'>緊急聯繫人</td><td align='left' class='tb_bt'>&#160;&#160;" + contact_name + "</td><td align='left'>緊急聯電話</td><td align='left'  class='tb_bt'>&#160;&#160;" + contact_phone_area_code + contact_phone + "</td>"
                    + "</tr>"
                    + "<tr style='height:30px;' >"
                    + "<td align='left'>聯繫人關係</td><td align='left' colspan='3' class='tb_bt'>&#160;&#160;" + contact_relation + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "<td>"
                    + "<table width='100%' align='center' border='0' cellpadding='5' cellspacing='0'>"
                    + "<tr>"
                    + "<td>"
                    + "<img src='./men.png' style='border:1px solid; height:120px; width:100px;'></img>"//height:150px; width:120px;
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>"
                    + "<table width='100%' align='left' border='0' cellpadding='5' cellspacing='0'>"
                    + "<tr style='height:30px;'>"
                    + "<td colspan='6' align='left'>"
                    + "有親屬於本公司任職（現任/曾任職）&#160;&#160;&#160;&#160;&#160;" + yes_tmp + "&#160;&#160;&#160;&#160;"
                    + "</td>"
                    + "</tr>"
                    + rowContentPsi
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "<br/><br/><br/>"
                    + "<div align='center'>"
                    + "<font style='font-size:20px'>教育程度</font>"
                    + "<hr width='100%' size='5' color='#00ffff' style='border:3 double black' />"
                    + "</div>"
                    + "<br/><br/>"
                    + "學歷（由最高學歷順序列出）"
                    + "<table width='100%' align='center' class='dataTable'  cellpadding='5' cellspacing='0'>"
                    + "<tr align='center'>"
                    + "<td width='8%'>學歷</td><td width='30%'>學校</td><td  width='10%'>由（年/月）</td><td width='10%'>至（年/月）</td><td width='10%'>所獲證書/專科</td>"
                    + "</tr>"
                    + rowContentEdu
                    + "</table><br/><br/>"


                    + "專業資格/證書"
                    + "<table width='100%' align='center' class='dataTable'  cellpadding='5' cellspacing='0'>"
                    + "<tr align='center'>"
                    + "<td width='40%'>頒發機構</td><td  width='20%'>頒發日期（月/年）</td><td width='40%'>證書/認可資格</td>"
                    + "</tr>"
                    + rowContentCert
                    + "</table>"
                    + "<div class='PageNext'></div>"
                    + "<div align='center'>"
                    + "<font style='font-size:20px;'>工作經驗（先填寫現任或最近任職之工作）</font>"
                    + "<hr width='100%' size='5' color='#00ffff' style='border:3 double black' />"
                    + "</div>"
                    + "<br/>"
                    + "<table width='100%' align='center' cellpadding='0' cellspacing='0'>"
                    + "<tr align='center'>"
                    + "<td width='35%' class='td_bd'>公司/職位</td>"
                    + "<td width='10%' class='td_bd'>由<br/>(月/年)</td>"
                    + "<td width='10%' class='td_bd'>至<br/>(月/年)</td>"
                    + "<td width='12%' class='td_bd'>基本薪酬<br/>（" +
                    moneyType_application_org + "）<br/>月薪/日薪/時薪</td>" +
                    //            "<td width='8%' class='td_bd'>津貼<br/>(MOP)</td>" +
                    "<td width='8%' class='td_bd'>仍在職<br/>(是/否)</td>"
                    + "<td  width='25%' class='td_all'>離職原因</td>"
                    + "</tr>"
                    + rowContentExp
                    + "</table>"
                    + "<br/>"
                    + "<table width='100%' border='0' cellpadding='5' cellspacing='0'>"
                    + "<tr><td>"
                    + workedBefore
                    + "</td></tr>"
                    + "<tr><td>"
                    + appliedBaccountBefore
                    + "</td></tr>"
                    + "<tr><td>"
                    + terminatedBefore
                    + "</td></tr>"
                    + "<tr><td>"
                    + criminalRecord
                    + "</td></tr>"
                    + "</table>"
                    + "<br/><br/><br/>"
                    + "<div align='center'>"
                    + "<font style='font-size:20px;'>其它資料</font>"
                    + "<hr width='100%' size='5' color='#00ffff' style='border:3 double black' />"
                    + "</div>"
                    + "<br/>"

                    + "個人技能"
//                                + "（請填寫代號 &#160;E-Excellent優/&#160;G-Good良好/&#160;A-Average一般/&#160;F-Fair差/&#160;P-Poor劣）"
                    + "（請填寫 &#160;流利/&#160;一般/&#160;略懂/&#160;不適用）"

                    + "<table width='100%' align='center' cellpadding='0' cellspacing='0'>"
                    + "<tr>"
                    + "<td width='100%'>"
                    + "<table width='100%' height='100%'  class='dataTable'  cellpadding='0' cellspacing='0'>"
                    + "<tr align='center'>"
                    + "<td width='30%'>語言技能</td><td width='20%'>書寫</td><td width='20%'>會話</td><td width='50%'>電腦技能</td>"
                    //            + "<td width='20%'>證書</td>"
                    + "</tr>"
                    + rowContentLang
                    + "</table>"
                    + "</td>"
                    + "<td width='40%'>"
                    + "<table width='100%' height='100%'  class='dataTable'  cellpadding='0' cellspacing='0'>"













             /*   + "<tr align='center'>"
                + "<td width='70%'>電腦技能</td><td width='30%'>運用</td>"
                + "</tr>"
                + rowContentSkill*/
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td colspan ='2'>"
                    + "<table width='100%' height='100%' class='dataTable'  cellpadding='0' cellspacing='0'>"
                    + rowContentSkillOT_tr
                    //            + "<tr>"
                    //            + "<td>愛好/興趣/體育活動</td><td></td>"
                    //            + "</tr>"


                    + whetherYouNeedToWorkInShifts_tr



                  /*  + "<tr>"
                    + "<td>是否需要輪班工作</td><td>&#160;&#160;" + Shift_tmp + "&#160;&#160;" + rowContentShift + "</td>"
                    + "</tr>"*/

              /*  + "<tr>"
                + "<td></td><td>&#160;&#160;"  + "&#160;&#160;" + "</td>"
                + "</tr>"*/
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</body>"
                    + "</html>";
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("導出失敗 PdfResumeHtmlStyleMaker error");

        }
        if (StringUtils.isBlank(Ep_html)) {
            throw new IllegalArgumentException("導出失敗 Ep_html is empty");
        }

        return Ep_html;
    }

    //vic
    public static String htmlEscape(String input) {
        if (input == null) {
            return null;
        }
        input = input.replaceAll("&", "&amp;");
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");
        input = input.replaceAll("'", "&#39;");   //IE暂不支持单引号的实体名称,而支持单引号的实体编号,故单引号转义成实体编号,其它字符转义成实体名称
        input = input.replaceAll("\"", "&quot;"); //双引号也需要转义，所以加一个斜线对其进行转义
        input = input.replaceAll("\n", "<br/>");  //不能把\n的过滤放在前面，因为还要对<和>过滤，这样就会导致<br/>失效了
        return input;
    }


    public static String dateToString(Date date) {
        if (date == null) return "";
        else return DateFormatUtils.format(date, TimeEnum.YYYY_MM_DD.getName());
    }
}
