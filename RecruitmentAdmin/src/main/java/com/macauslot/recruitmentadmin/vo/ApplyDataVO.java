package com.macauslot.recruitmentadmin.vo;

import com.macauslot.recruitmentadmin.annotation.Sign;
import com.macauslot.recruitmentadmin.entity.OnShift;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
//@AllArgsConstructor
public class ApplyDataVO {
    //    Integer job_priority;
    Integer applicant_info_id;


    String process_stage;
    String code;
    String job_title;
    String expected_salary;
    String expected_salary_type;
    Date available_work_date;
    Integer notice_day;
    String notice_day_type;
    String source;
    String source_ref;
    Date cr_date;
    String deptName;
    String applicant_source_detail_desc;
    String need_shift;
    Integer applicant_position_id;
    String applicantResult;
    @Sign("getApplyDataListByApplicantInfoId")
    public ApplyDataVO(Float process_stage, String code, String job_title, String expected_salary, String expected_salary_type, Date available_work_date, Integer notice_day, String notice_day_type, String source, String source_ref, Date cr_date, String deptName, String applicant_source_detail_desc, Byte need_shift, Integer applicant_position_id,String applicantResult) {
        this.process_stage =  String.valueOf(process_stage);
        this.code = code;
        this.job_title = job_title;
        this.expected_salary = expected_salary;
        this.expected_salary_type = expected_salary_type;
        this.available_work_date = available_work_date;
        this.notice_day = notice_day;
        this.notice_day_type = notice_day_type;
        this.source = source;
        this.source_ref = source_ref;
        this.cr_date = cr_date;
        this.deptName = deptName;
        this.applicant_source_detail_desc = applicant_source_detail_desc;
        this.need_shift = String.valueOf(need_shift);
        this.applicant_position_id = applicant_position_id;
        this.applicantResult = applicantResult;
    }
    @Sign("getApplyDataListByApplicantPosId")
    public ApplyDataVO(Integer applicant_info_id, Float process_stage, String code, String job_title, String expected_salary, String expected_salary_type, Date available_work_date, Integer notice_day, String notice_day_type, String source, String source_ref, Date cr_date, String deptName, String applicant_source_detail_desc, Byte need_shift, Integer applicant_position_id,String applicantResult) {
        this.applicant_info_id = applicant_info_id;
        this.process_stage =  String.valueOf(process_stage);
        this.code = code;
        this.job_title = job_title;
        this.expected_salary = expected_salary;
        this.expected_salary_type = expected_salary_type;
        this.available_work_date = available_work_date;
        this.notice_day = notice_day;
        this.notice_day_type = notice_day_type;
        this.source = source;
        this.source_ref = source_ref;
        this.cr_date = cr_date;
        this.deptName = deptName;
        this.applicant_source_detail_desc = applicant_source_detail_desc;
        this.need_shift = String.valueOf(need_shift);
        this.applicant_position_id = applicant_position_id;
        this.applicantResult = applicantResult;
    }

    List<OnShift> onShiftList;


}
