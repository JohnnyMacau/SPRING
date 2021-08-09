package com.macauslot.recruitmentadmin.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.poi.excel.style.StyleUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.macauslot.recruitmentadmin.annotation.UserLoginvalidation;
import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.service.RecruitmentService;
import com.macauslot.recruitmentadmin.util.DateTool;
import com.macauslot.recruitmentadmin.util.ExcelUtil;
import com.macauslot.recruitmentadmin.util.ServerRoleTagEnum;
import com.macauslot.recruitmentadmin.util.TimeEnum;
import com.macauslot.recruitmentadmin.util.pdf.PdfResumeHtmlStyleMaker;
import com.macauslot.recruitmentadmin.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/export")
@Slf4j
public class ExportController {

    /**
     * 單sheet最大行數限制: 一百万[只因Excel有限制]
     */
    private static final int MAXIMUM_ROW_LIMIT = 100_0000;

    private final RecruitmentService recruitmentService;

    private static File TTC_FILE;
    private static String TTC_PATH;

    static {
        try (InputStream stream1 = ExportController.class.getClassLoader().getResourceAsStream("static/assets/fonts/simsun.ttc")) {
            TTC_FILE = new File("files/simsun.ttc");
            FileUtils.copyInputStreamToFile(stream1, TTC_FILE);
            TTC_PATH = TTC_FILE.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ExportController getAbsolutePath error!");
        }
    }


    @Autowired
    public ExportController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }





    //1|申請記錄總表
    @UserLoginvalidation
    @RequestMapping("/application_report")
    public void exportApplicationRpt(
            ApplicantionRecordReportVO condition,
            @RequestParam("titles[]") String[] titles,
            @RequestParam("keys[]") String[] keys,

            String applicationOrgCn,
            String applicantDeptCn,
            String idCardTypeNameCn,

            HttpServletResponse response
    ) {
//        System.err.println("condition---" +  condition);
//        System.err.println("titles---" + Arrays.toString(titles));
//        System.err.println("keys---" + Arrays.toString(keys));
        List<String> newTableNameList = new ArrayList<>();
        List<String[]> newTitleList = new ArrayList<>();
        List<List<String[]>> resourceList = new ArrayList<>();
        String fileName = "申請記錄總表";
        newTableNameList.add(fileName);
        newTitleList.add(titles);
        /*    String[] keys = new String[]{
                    "department",
                    "groupName",
                    "managerName",
                    "reviewerName",
                    "appraiserName",
                    "employee",
            };*/
        List<ApplicantionRecordReportVO> reportVOList = recruitmentService.getPage4ApplicantReport(0, 0, 99999999, condition, null, null).getData();
        resourceList.add(castInstanceToStringArr4applicantReport(reportVOList, keys));
        List<ArrayList<String>> conditionList = new ArrayList<>();
        ArrayList<String> condition1 = new ArrayList<>();
        printDateToDoc("申請日期: ", condition.getApplicant_postition_start_date(),
                condition.getApplicant_postition_end_date(), condition1);

        condition1.add("所屬公司: " + applicationOrgCn);
        condition1.add("應徵部門: " + applicantDeptCn);
        condition1.add("身份證類別: " + idCardTypeNameCn);
        String archivedResult = condition.getArchivedResult();
        if (StringUtils.isBlank(archivedResult)) {
            archivedResult = "全部";
        }
        condition1.add("歸檔結果: " + archivedResult);


        conditionList.add(condition1);
        export(fileName, "excel", response, newTableNameList, newTitleList, resourceList, conditionList);
    }

    //2|應徵者資料總表
    @UserLoginvalidation
    @RequestMapping("/candidate_information_report")
    public void exportCandidateInformationReport(
            CandidateInformationReportVO condition,
            @RequestParam("titles[]") String[] titles,
            @RequestParam("keys[]") String[] keys,
            String idCardTypeNameCn,
            String degreeCn,
            String isblacklistedCn,
            HttpServletResponse response
    ) {
        System.err.println("condition---" + condition);
        System.err.println("titles---" + Arrays.toString(titles));
        System.err.println("keys---" + Arrays.toString(keys));
        List<String> newTableNameList = new ArrayList<>();
        List<String[]> newTitleList = new ArrayList<>();
        List<List<String[]>> resourceList = new ArrayList<>();
        String fileName = "應徵者資料總表";
        newTableNameList.add(fileName);
        newTitleList.add(titles);
        /*    String[] keys = new String[]{
                    "department",
                    "groupName",
                    "managerName",
                    "reviewerName",
                    "appraiserName",
                    "employee",
            };*/
        List<CandidateInformationReportVO> reportVOList = recruitmentService.getPage4CandidateInformationReport(0, 0, 99999999, condition, null, null).getData();
        resourceList.add(castInstanceToStringArr4CandidateInformationReport(reportVOList, keys));
        List<ArrayList<String>> conditionList = new ArrayList<>();
        ArrayList<String> condition1 = new ArrayList<>();
        condition1.add("身份證類別: " + idCardTypeNameCn);
        condition1.add("教育程度: " + degreeCn);
        condition1.add("是否黑名單: " + isblacklistedCn);

        conditionList.add(condition1);
        export(fileName, "excel", response, newTableNameList, newTitleList, resourceList, conditionList);
    }

    //3|應徵者申請總表
    @UserLoginvalidation
    @RequestMapping("/candidate_application_report")
    public void exportCandidateApplicationReport(
            CandidateApplicationReport1VO condition,
            HttpServletResponse response
    ) {
        String fileName = "應徵者申請總表";
        List<String> tableNameList = new ArrayList<>();
        List<List<List<String>>> resourceList = new ArrayList<>();
        //CandidateApplicationReport1
        CandidateApplicationReport1VO report1VO = recruitmentService.getCandidateApplicationReport1(condition);

        tableNameList.add(fileName);
        List<String> row1 = CollUtil.newArrayList("姓名",
                filterReturnVal(report1VO.getCn_last_name())
                        + filterReturnVal(report1VO.getCn_first_name())
                        + (report1VO.getEn_last_name())
                        + (report1VO.getEn_first_name())
        );
        List<String> row2 = CollUtil.newArrayList("身份證資料",
                filterReturnVal(report1VO.getId_card_type_name()) + " "
                        + filterReturnVal(report1VO.getId_card_number())
        );
        Byte is_blacklisted = report1VO.getIs_blacklisted();
        String is_blacklisted_str;
        if (is_blacklisted == null || is_blacklisted == (byte) 0) {
            is_blacklisted_str = "否";
        } else {
            is_blacklisted_str = "是";
        }
        List<String> row3 = CollUtil.newArrayList("黑名單", is_blacklisted_str);
        BigInteger count_apply_times = report1VO.getCount_apply_times();
        String count_apply_times_str;
        if (count_apply_times == null) {
            count_apply_times_str = "0";
        } else {
            count_apply_times_str = String.valueOf(count_apply_times);
        }
        List<String> row4 = CollUtil.newArrayList("總應徵次數", count_apply_times_str);
        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4);
        resourceList.add(rows);

        //CandidateApplicationReport2 List
        List<CandidateApplicationReport2VO> report2List = recruitmentService.getCandidateApplicationReport2List(condition);
        for (int i = 0; i < report2List.size(); i++) {
            CandidateApplicationReport2VO report2VO = report2List.get(i);
            tableNameList.add("申請 " + (i + 1));
            List<String> row1_tmp = CollUtil.newArrayList("申請日期",
                    DateFormatUtils.format(report2VO.getApplicant_cr_date(), TimeEnum.YYYY_MM_DD_HH_MM_SS.getName())
            );
            List<String> row2_tmp = CollUtil.newArrayList("申請職位", filterReturnVal(report2VO.getJob_title()));
            List<String> row3_tmp = CollUtil.newArrayList("最後結果",
                    filterReturnVal(report2VO.getProcess_stage()) + " " +
                            filterReturnVal(report2VO.getArchived_result())
            );
            List<String> row4_tmp = CollUtil.newArrayList("流程歷史", renderFun(filterReturnVal(report2VO.getFlow_detail())));
            List<List<String>> rows_tmp = CollUtil.newArrayList(row1_tmp, row2_tmp, row3_tmp, row4_tmp);
            resourceList.add(rows_tmp);
        }


        exportByHuTool(fileName, "excel", response, tableNameList, resourceList);
    }

    //4|申請及面試情況總表
    @UserLoginvalidation
    @RequestMapping("/application_interview_status_summary")
    public void exportApplicationInterviewStatusSummary(
            ApplicationInterviewStatusSummaryVO condition,
            @RequestParam("titles[]") String[] titles,
//            @RequestParam("keys[]") String[] keys,
            HttpServletResponse response
    ) {
        System.err.println("condition---" + condition);
        System.err.println("titles---" + Arrays.toString(titles));
//        System.err.println("keys---" + Arrays.toString(keys));
        List<String> newTableNameList = new ArrayList<>();
        List<String[]> newTitleList = new ArrayList<>();
        List<List<String[]>> resourceList = new ArrayList<>();
        String fileName = "申請及面試情況總表";

        newTableNameList.add("職位申請情況");
        newTitleList.add(titles);
        List<String[]> list1 = getStringsList(recruitmentService.getJobApplicationStatisticsList(condition));
        resourceList.add(list1);

        newTableNameList.add("HR面試情況");
        newTitleList.add(titles);
        List<String[]> list2 = getStringsList(recruitmentService.getInterviewsStatisticsList(condition));
        resourceList.add(list2);

        newTableNameList.add("部門面試情況");
        newTitleList.add(titles);
        List<String[]> list3 = getStringsList(recruitmentService.getInterviewsStatisticsList(condition));
        resourceList.add(list3);
        List<ArrayList<String>> conditionList = new ArrayList<>();
        ArrayList<String> condition1 = new ArrayList<>();
        condition1.add("所屬公司: " + condition.getApplicationOrg());
        printDateToDoc("日期範圍由: ", condition.getStartDate(), condition.getEndDate(), condition1);
        conditionList.add(condition1);
        export(fileName, "excel", response, newTableNameList, newTitleList, resourceList, conditionList);
    }

    //5|應徵者收集統計
    @UserLoginvalidation
    @RequestMapping("/candidate_collecting_statistics")
    public void exportCandidateCollectingStatistics(
            CandidateCollectingStatisticsVO condition,
            HttpServletResponse response
    ) {
        System.err.println("condition---" + condition);

        String fileName = "應徵者收集統計";
        List<String> newTableNameList = CollUtil.newArrayList(
                "應徵者申請來源", "應徵者學歷背景", "全職應徵者年齡比例", "兼職應徵者年齡比例");
        List<String[]> newTitleList = CollUtil.newArrayList(
                new String[]{
                        "招聘渠道",
                        "人數",
                        "百分比",
                },
                new String[]{
                        "應徵者學歷",
                        "人數",
                        "百分比",
                },
                new String[]{
                        "年齡階段",
                        "全職人數",
                        "全職百分比",
                },
                new String[]{
                        "年齡階段",
                        "兼職人數",
                        "兼職百分比",
                });

        String[] keys = new String[]{
                "title",
                "peopleCount",
                "percent",
        };
        List<List<String[]>> resourceList = new ArrayList<>();


        List<CandidateCollectingStatisticsVO> list1 = recruitmentService.getCandidateApplicationSourcesList(condition);
        resourceList.add(castInstanceToStringArr4CandidateCollectingStatistics(list1, keys));
        List<CandidateCollectingStatisticsVO> list2 = recruitmentService.getCandidateEducationalBackgroundList(condition);
        resourceList.add(castInstanceToStringArr4CandidateCollectingStatistics(list2, keys));
        List<CandidateCollectingStatisticsVO> list3 = recruitmentService.getCandidateAgeRatioList(condition, "full");
        resourceList.add(castInstanceToStringArr4CandidateCollectingStatistics(list3, keys));
        List<CandidateCollectingStatisticsVO> list4 = recruitmentService.getCandidateAgeRatioList(condition, "part");
        resourceList.add(castInstanceToStringArr4CandidateCollectingStatistics(list4, keys));
        List<ArrayList<String>> conditionList = new ArrayList<>();
        ArrayList<String> condition1 = new ArrayList<>();
        printDateToDoc("收表日期由: ", condition.getApplicant_postition_start_date(),
                condition.getApplicant_postition_end_date(),
                condition1);
        conditionList.add(condition1);
        export(fileName, "excel", response, newTableNameList, newTitleList, resourceList, conditionList);
    }


    @UserLoginvalidation
    @RequestMapping("/downloadBlackList")
    public void downloadBlackList(
            BlackListVO condition,
            HttpServletResponse response
    ) {
//        System.err.println(condition);
        List<BlackListVO> data = recruitmentService.getPage4BlackList(0, 0, 9999999, "default", "default", condition).getData();
//        System.err.println(data);

        for (BlackListVO vo : data) {
            String status = vo.getStatus();
            if ("A".equalsIgnoreCase(status)) {
                vo.setStatus("生效");
            } else if ("S".equalsIgnoreCase(status)) {
                vo.setStatus("失效");
            }
        }


        String type = "excel";
        Calendar now = Calendar.getInstance();

        try (OutputStream out = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(out);
             // 通过工具类创建writer
             ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(true)
        ) {
            int width = 20;

            //自定义标题别名
            writer.addHeaderAlias("nameEn", "譯名");
            writer.setColumnWidth(0, width);
            writer.addHeaderAlias("nameCn", "姓名");
            writer.setColumnWidth(1, width);
            writer.addHeaderAlias("tel", "聯絡電話");
            writer.setColumnWidth(2, width);
            writer.addHeaderAlias("idNumber", "證件號碼");
            writer.setColumnWidth(3, width);
            writer.addHeaderAlias("leaveDate", "離職日期");
            writer.setColumnWidth(4, width);
            writer.addHeaderAlias("remark", "黑名單原因");
            writer.setColumnWidth(5, width);
            writer.addHeaderAlias("lastModifyDate", "添加時間");
            writer.setColumnWidth(6, width);
            writer.addHeaderAlias("status", "狀態");
            writer.setColumnWidth(7, width);

            writer.setOnlyAlias(true);

            writer.write(data, true);


            writer.flush(baos, true);


            //合并导出
            String fileName1 = "黑名單" + "-";
            String suffix = "excel".equals(type) ? ".xlsx" : ".pdf";
            response.setContentType("APPLICATION/OCTET-STREAM");
            StringBuilder headStr = new StringBuilder()
                    .append("attachment; filename=\"")
                    .append(new String(fileName1.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                    .append(getDateTimeString(now))
                    .append(suffix)
                    .append("\"");
            response.setHeader("Content-Disposition", headStr.toString());
            byte[] bytes = baos.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            bos.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }


    }


    public static String dateToString(Date date) {
        return DateFormatUtils.format(date, TimeEnum.YYYY_MM_DD.getName());
    }

    private void printDateToDoc(String dateName, Date dateFromD, Date dateToD, ArrayList<String> condition) {
        String dateFrom = dateFromD == null ? "" : dateToString(dateFromD);
        String dateTo = dateToD == null ? "" : dateToString(dateToD);
        printDateToDoc(dateName, dateFrom, dateTo, condition);
    }

    private void printDateToDoc(String dateName, String dateFrom, String dateTo, ArrayList<String> condition) {
        if (StringUtils.isNotBlank(dateFrom) && StringUtils.isBlank(dateTo)) {
            condition.add(dateName + dateFrom + " 至 - ");
        } else if (StringUtils.isBlank(dateFrom) && StringUtils.isNotBlank(dateTo)) {
            condition.add(dateName + " - 至 " + dateTo);
        } else if (StringUtils.isNotBlank(dateFrom) && StringUtils.isNotBlank(dateTo)) {
            condition.add(dateName + dateFrom + " 至 " + dateTo);
        } else {
            condition.add(dateName + " - 至 - ");
        }
    }

    private List<String[]> getStringsList(JSONArray jsonArray) {
        List<String[]> returnList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray subArr = jsonArray.getJSONArray(i);
            String[] tmpStrings = new String[subArr.size()];
            for (int j = 0; j < subArr.size(); j++) {
                tmpStrings[j] = subArr.getString(j);
            }
            returnList.add(tmpStrings);
        }
        return returnList;
    }


    private String renderFun(String str) {
        return StringUtils.replace(str, "||", "\n");
    }

    @UserLoginvalidation(needSetUserDTO = true, serverRoleTagEnum = {ServerRoleTagEnum.HR, ServerRoleTagEnum.DEPT})
    @PostMapping("/resume")
    public void exportResume(UserDTO userDTO,
                             @RequestParam("applicant_pos_ids[]") Integer[] applicant_pos_ids,
                             HttpServletResponse response) {
        System.err.println(Arrays.toString(applicant_pos_ids));

        if (applicant_pos_ids.length == 0) {
            return;
        }


        Function<String, InputStream> changeEpHtmlToInputStream = str_tmp -> {
            InputStream inputStream;
            try {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ITextRenderer renderer = new ITextRenderer();
                    renderer.setDocumentFromString(str_tmp);
                    ITextFontResolver fontResolver = renderer.getFontResolver();
//                    System.err.println("TTC_PATH = " + TTC_PATH);
//                    log.warn("TTC_PATH = " + TTC_PATH);
                    fontResolver.addFont(TTC_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    renderer.layout();
                    renderer.createPDF(baos);
                    inputStream = new ByteArrayInputStream(baos.toByteArray());
                }
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("導出失敗 changeEpHtmlToInputStream() error");

            }
            return inputStream;

        };

        boolean isServerRoleDept = ServerRoleTagEnum.DEPT == userDTO.getRoleTag();
        Boolean isFLTAdmin = userDTO.getIsFLTAdmin();
        boolean isNotFltAdmin = isFLTAdmin == null || !isFLTAdmin;
        List<InputStream> inputStreamList = Arrays.stream(applicant_pos_ids)
                .map(x -> recruitmentService.getTheResumeWithMaskInspection(isServerRoleDept, isNotFltAdmin, x))
                .map(PdfResumeHtmlStyleMaker::setExportPageForResume)
                .map(changeEpHtmlToInputStream)
                .collect(Collectors.toList());

        exportMultiplePagePdfForResume("批量導出", response, inputStreamList);

        recruitmentService.batchChangeApplicantPositionExportDate(Arrays.asList(applicant_pos_ids));
    }


    @UserLoginvalidation
    @PostMapping("/profile")
    public void exportProfile(
            @RequestParam("applicant_info_ids[]") Integer[] applicant_info_ids,
            HttpServletResponse response) {
        System.err.println(Arrays.toString(applicant_info_ids));

        if (applicant_info_ids.length == 0) {
            return;
        }


        Function<String, InputStream> changeEpHtmlToInputStream = str_tmp -> {
            InputStream inputStream;
            try {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ITextRenderer renderer = new ITextRenderer();
                    renderer.setDocumentFromString(str_tmp);
                    ITextFontResolver fontResolver = renderer.getFontResolver();
                    fontResolver.addFont(TTC_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    renderer.layout();
                    renderer.createPDF(baos);
                    inputStream = new ByteArrayInputStream(baos.toByteArray());
                }
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("導出失敗 changeEpHtmlToInputStream() error");

            }
            return inputStream;

        };


        List<InputStream> inputStreamList = Arrays.stream(applicant_info_ids)
                .map(recruitmentService::getProfile)
                .map(PdfResumeHtmlStyleMaker::setExportPageForResume)
                .map(changeEpHtmlToInputStream)
                .collect(Collectors.toList());

        exportMultiplePagePdfForResume("批量導出", response, inputStreamList);

    }


    private void exportMultiplePagePdfForResume(String fileName,
                                                HttpServletResponse response, List<InputStream> inputStreamList) {
        Document document = null;
        PdfWriter writer = null;
        try (OutputStream out = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(out)) {

            document = new Document();

            List<PdfReader> readers = new ArrayList<>();
            int totalPages = 0;

            // Create Readers for the pdfs.
            for (InputStream pdf : inputStreamList) {
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            writer = PdfWriter.getInstance(document, baos);

            document.open();
//            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
//                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
//            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;

            // Loop through the PDF files and add to the output.
            for (PdfReader pdfReader : readers) {
                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
//                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                    // Code for pagination.
//                        if (paginate) {
//                    cb.beginText();
//                    cb.setFontAndSize(bf, 9);
//                    cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
//                                    + currentPageNumber + " of " + totalPages, 520,
//                            5, 0);
//                    cb.endText();
//                        }
                }
                pageOfCurrentReaderPDF = 0;
            }
            document.close();
            writer.close();
            //合并导出
            String fileName1 = fileName + "-" + readers.size() + "人" + totalPages + "頁-";
            response.setContentType("APPLICATION/OCTET-STREAM");
//            response.setContentType("application/pdf");
            String finalFileName = "attachment; filename=\"" +
//            String finalFileName = "inline; filename=\"" +
                    new String(fileName1.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) +
                    DateTool.getDateTimeString() +
                    ".pdf" +
                    "\"";
            response.setHeader("Content-Disposition", finalFileName);
            byte[] bytes = baos.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            bos.write(bytes);

        } catch (Exception e1) {
            e1.printStackTrace();
            throw new IllegalArgumentException("導出失敗 exportMultiplePagePdfForResume() error");
        } finally {
            if (document != null) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }


    private void exportSinglePagePdfForResume(ResumeVO resume, String str_tmp, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            //String inputFile = "src/com/a.html";
            //String url = new File(inputFile).toURI().toURL().toString();
            //String outputFile = "c:/Projects/HRPortal/WebContent/documents/company_policy/my.pdf";
            //OutputStream os =new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();

            //renderer.setDocument(url);
            renderer.setDocumentFromString(str_tmp);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            //fontResolver.addFont("C:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            //fontResolver.addFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            fontResolver.addFont(TTC_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            renderer.layout();
            renderer.createPDF(baos);


            String fileName = "(" + resume.getApplyDataVOList().get(0).getCode() + ")"
                    + resume.getApplicantInfoVO().getEnLName()
                    + resume.getApplicantInfoVO().getEnFName();


            //合并导出
            String fileName1 = fileName + "-";
            response.setContentType("APPLICATION/OCTET-STREAM");
//            response.setContentType("application/pdf");
            String finalFileName = "attachment; filename=\"" +
//            String finalFileName = "inline; filename=\"" +
                    new String(fileName1.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) +
                    DateTool.getDateTimeString() +
                    ".pdf" +
                    "\"";
            response.setHeader("Content-Disposition", finalFileName);
            byte[] bytes = baos.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            bos.write(bytes);
//            System.err.println("Success!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("導出失敗 exportSinglePagePdfForResume() error");

        }
    }


    private <T> List<String[]> castInstanceToStringArr4applicantReport(List<T> list, String[] keys) {
        Function<JSONObject, String[]> changeToStringArr = rowsObject -> {
            String[] values = new String[keys.length];
            for (int i = 0; i < keys.length; i++) {
                String cellvalue = rowsObject.getString(keys[i]);


                if ("applicantCrDate".equals(keys[i])) {
                    values[i] = DateFormatUtils.format(Long.parseLong(cellvalue), TimeEnum.YYYY_MM_DD_HH_MM_SS.getName());
                } else if ("applicationOrg".equals(keys[i])) {
                    if (StringUtils.isBlank(cellvalue) || "null".equalsIgnoreCase(cellvalue)) {
                        values[i] = "SLT";
                    } else {
                        values[i] = cellvalue;
                    }
                } else if ("applicantDeptName".equals(keys[i])) {
                    values[i] = rowsObject.getString("applicantDeptCode") + " " + cellvalue;
                } else if ("recruitmentSource1".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + "  " + filterReturnVal(rowsObject.getString("recruitmentSource3")) + filterReturnVal(rowsObject.getString("recruitmentSource2"));
                } else if ("isBlacklisted".equals(keys[i])) {
                    if ("1".equalsIgnoreCase(cellvalue)) {
                        values[i] = "✔";
                    }
                } else if ("cnLastName".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) +
                            filterReturnVal(rowsObject.getString("cnFirstName")) + "  " +
                            filterReturnVal(rowsObject.getString("enLastName")) + " " +
                            filterReturnVal(rowsObject.getString("enFirstName"));
                } else if ("idCardTypeName".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + "  " + filterReturnVal(rowsObject.getString("idCardNumber"));
                } else if ("martialStatus".equals(keys[i])) {
                    switch (cellvalue) {
                        case "Single":
                            values[i] = "未婚";
                            break;
                        case "Married":
                            values[i] = "已婚";
                            break;
                        case "Divorced":
                            values[i] = "離婚";
                            break;
                        case "Widowed":
                            values[i] = "鳏寡";
                            break;
                        case "Deprecated":
                            values[i] = "";
                            break;

                        default:
                            values[i] = cellvalue;
                            break;
                    }
                } else if ("address1".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + " "
                            + filterReturnVal(rowsObject.getString("address2")) + " "
                            + filterReturnVal(rowsObject.getString("address3"));
                } else if ("mobile1".equals(keys[i])) {
                    values[i] = filterReturnVal(rowsObject.getString("areaCode1")) + "-" + filterReturnVal(cellvalue);
                } else if ("relativeName".equals(keys[i])) {
                    values[i] = cellvalue == null ? "否" : ("是 (" + cellvalue + ")");
                } else if ("degree".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + " "
                            + filterReturnVal(rowsObject.getString("schoolName")) + " "
                            + filterReturnVal(rowsObject.getString("schoolMajor"));
                } else if ("hiCompanyName".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + " "
                            + filterReturnVal(rowsObject.getString("hiPosition")) + " "
                            + filterReturnVal(rowsObject.getString("hiPayMethod")) + " "
                            + filterReturnVal(rowsObject.getString("hiCurrency")) + " "
                            + filterReturnVal(rowsObject.getString("hiSalary"));
                } else if ("expectedSalary".equals(keys[i])) {
                    values[i] = filterReturnVal(rowsObject.getString("expectedSalaryType")) + " " + filterReturnVal(cellvalue);
                } else {
                    values[i] = cellvalue;
                }
            }
            return values;
        };
        return list.stream().map(x -> JSONObject.parseObject(JSON.toJSONString(x)))
                .map(changeToStringArr)
                .collect(Collectors.toList());
    }


    private <T> List<String[]> castInstanceToStringArr4CandidateCollectingStatistics(List<T> list, String[] keys) {
        Function<JSONObject, String[]> changeToStringArr = rowsObject -> {
            String[] values = new String[keys.length];
            for (int i = 0; i < keys.length; i++) {
                String cellvalue = rowsObject.getString(keys[i]);
                if (cellvalue != null && "percent".equals(keys[i])) {
                    BigDecimal n = new BigDecimal(cellvalue).setScale(2, BigDecimal.ROUND_HALF_UP);
                    values[i] = n.toPlainString() + "%";
                } else {
                    values[i] = cellvalue;
                }


            }
            return values;
        };
        return list.stream().map(x -> JSONObject.parseObject(JSON.toJSONString(x)))
                .map(changeToStringArr)
                .collect(Collectors.toList());
    }


    private <T> List<String[]> castInstanceToStringArr4CandidateInformationReport(List<T> list, String[] keys) {
        Function<JSONObject, String[]> changeToStringArr = rowsObject -> {
            String[] values = new String[keys.length];
            for (int i = 0; i < keys.length; i++) {
                String cellvalue = rowsObject.getString(keys[i]);


                if ("is_blacklisted".equals(keys[i])) {
                    if ("1".equalsIgnoreCase(cellvalue)) {
                        values[i] = "✔";
                    }
                } else if ("cn_last_name".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) +
                            filterReturnVal(rowsObject.getString("cn_first_name")) + "  " +
                            filterReturnVal(rowsObject.getString("en_last_name")) + " " +
                            filterReturnVal(rowsObject.getString("en_first_name"));
                } else if ("id_card_type_name".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + "  " + filterReturnVal(rowsObject.getString("id_card_number"));
                } else if ("martial_status".equals(keys[i])) {
                    switch (cellvalue) {
                        case "Single":
                            values[i] = "未婚";
                            break;
                        case "Married":
                            values[i] = "已婚";
                            break;
                        case "Divorced":
                            values[i] = "離婚";
                            break;
                        case "Widowed":
                            values[i] = "鳏寡";
                            break;
                        case "Deprecated":
                            values[i] = "";
                            break;

                        default:
                            values[i] = cellvalue;
                            break;
                    }
                } else if ("address_1".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + " "
                            + filterReturnVal(rowsObject.getString("address_2")) + " "
                            + filterReturnVal(rowsObject.getString("address_3"));
                } else if ("mobile_1".equals(keys[i])) {
                    values[i] = filterReturnVal(rowsObject.getString("area_code_1")) + "-" + filterReturnVal(cellvalue);
                } else if ("degree".equals(keys[i])) {
                    values[i] = filterReturnVal(cellvalue) + " "
                            + filterReturnVal(rowsObject.getString("school_name")) + " "
                            + filterReturnVal(rowsObject.getString("school_major"));
                } else if ("all_company".equals(keys[i]) || "application_history".equals(keys[i])) {
                    values[i] = StringUtils.replace(cellvalue, "||", "\n");
                } else {
                    values[i] = cellvalue;
                }
            }
            return values;
        };
        return list.stream().map(x -> JSONObject.parseObject(JSON.toJSONString(x)))
                .map(changeToStringArr)
                .collect(Collectors.toList());
    }

    private static String filterReturnVal(String str) {
        return StringUtils.isBlank(str) ? "" : str;
    }

    private void exportByHuTool(String fileName,
                                String type,
                                HttpServletResponse response,
                                List<String> tableNameList,
                                List<List<List<String>>> resourceList

    ) {


        Calendar now = Calendar.getInstance();

        try (OutputStream out = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(out);
             // 通过工具类创建writer
             ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(true)
        ) {
// 定义单元格
            StyleSet style = writer.getStyleSet();
            style.setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
            style.setWrapText();
            CellStyle headCellStyle = style.getHeadCellStyle();
            StyleUtil.setColor(headCellStyle, IndexedColors.GOLD, FillPatternType.SOLID_FOREGROUND);
            Font headCell_font = writer.createFont();
            headCell_font.setFontHeightInPoints((short) 11);
            headCell_font.setBold(true);
            headCell_font.setFontName("宋体");

            headCellStyle.setFont(headCell_font);


            writer.setColumnWidth(0, 15); //第1列15px宽
            writer.setColumnWidth(1, 55); //第2列55px 宽


            for (int i = 0; i < tableNameList.size(); i++) {
                List<List<String>> rows = resourceList.get(i);
                // 合并单元格后的标题行，使用默认标题样式
                writer.merge(rows.get(0).size() - 1, tableNameList.get(i), true);
                // 一次性写出内容，使用默认样式，强制输出标题
                writer.write(rows, true);
                //跳过当前行
                writer.passCurrentRow();
            }

            writer.flush(baos, true);

//// 关闭writer，释放内存
//            writer.close();

            //合并导出
            String fileName1 = fileName + "-";
            String suffix = "excel".equals(type) ? ".xlsx" : ".pdf";
            response.setContentType("APPLICATION/OCTET-STREAM");
            StringBuilder headStr = new StringBuilder()
                    .append("attachment; filename=\"")
                    .append(new String(fileName1.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                    .append(getDateTimeString(now))
                    .append(suffix)
                    .append("\"");
            response.setHeader("Content-Disposition", headStr.toString());
            byte[] bytes = baos.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            bos.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }


    }

    private void export(String fileName,
                        String type,
                        HttpServletResponse response,
                        List<String> newTableNameList,
                        List<String[]> newTitleList,
                        List<List<String[]>> resourceList,
                        List<ArrayList<String>> conditionList
    ) {
        Calendar now = Calendar.getInstance();

        try (OutputStream out = response.getOutputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(out)) {


            if ("excel".equals(type)) {
                int sizeSum = resourceList.stream().mapToInt(List::size).sum();
                if (returnWarningMsg(response, bos, sizeSum)) {
                    return;
                }
                ExcelUtil.exportExcel(newTableNameList, newTitleList, resourceList, baos, conditionList);
            }


            //合并导出
            String fileName1 = fileName + "-";
            String suffix = "excel".equals(type) ? ".xlsx" : ".pdf";

//        response.setContentType("application/pdf");
//        String finalFileName = "inline; filename=\"" +

            response.setContentType("APPLICATION/OCTET-STREAM");

            StringBuilder headStr = new StringBuilder()
                    .append("attachment; filename=\"")
                    .append(new String(fileName1.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                    .append(getDateTimeString(now))
                    .append(suffix)
                    .append("\"");
            response.setHeader("Content-Disposition", headStr.toString());
            byte[] bytes = baos.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            bos.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private boolean returnWarningMsg(HttpServletResponse response, BufferedOutputStream bos, int size) throws IOException {
        if (size > MAXIMUM_ROW_LIMIT) {
            response.setContentType("text/html; charset=utf-8");
            bos.write("<script>alert('excel最大行數超過一百萬!');history.go(-1);</script>".getBytes(StandardCharsets.UTF_8));
            return true;
        }
        return false;
    }

    private String getDateTimeString(Calendar now) {
        return new String(
                DateFormatUtils.format(now, "yyyy年MM月dd日(EEEE)-HH-mm-ss").getBytes(StandardCharsets.UTF_8)
                , StandardCharsets.ISO_8859_1);
    }
}
