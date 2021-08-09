package com.macauslot.recruitmentadmin.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.macauslot.recruitmentadmin.annotation.Sign;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class CandidateApplicationReport2VO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicant_cr_date;
    private String applicant_dept_code;
    private String job_title;
    private String process_stage;
    private String archived_result;
    private String flow_detail;

    @Sign("find4CandidateApplicationReport2")
    public CandidateApplicationReport2VO(Date applicant_cr_date, String applicant_dept_code, String job_title, String process_stage, String archived_result, String flow_detail) {
        this.applicant_cr_date = applicant_cr_date;
        this.applicant_dept_code = applicant_dept_code;
        this.job_title = job_title;
        this.process_stage = process_stage;
        this.archived_result = archived_result;
        this.flow_detail = flow_detail;
    }
}
