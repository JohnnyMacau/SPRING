package com.macauslot.recruitmentadmin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class JobVO {
    private Integer detail_id;
    private String dept_name;
    private String job_title;
    private String job_code;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date start_date;
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date end_date;
    private String post_status; //生效文字
    private String dept_code;
    private Date last_modify_date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cr_date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date close_date;
    private String org;
    private Integer headcount;
    private Byte is_sealed;
    private BigInteger bookmark_count;
    private BigInteger recruitment_count;
    private Character status;  //A\D\O\S

    private Boolean thisOneLock;

    public JobVO(Integer detail_id, String job_code, String job_title) {
        this.detail_id = detail_id;
        this.job_title = job_title;
        this.job_code = job_code;
    }


    public JobVO(Integer detail_id, String dept_name, String job_title, String job_code, Date start_date, Date end_date, String post_status,Date last_modify_date) {
        this.detail_id = detail_id;
        this.dept_name = dept_name;
        this.job_title = job_title;
        this.job_code = job_code;
        this.end_date = end_date;
        this.post_status = post_status;
        this.last_modify_date = last_modify_date;
    }

    public JobVO(Integer detail_id, String dept_name, String job_title, String job_code, Date start_date, Date end_date, String post_status,Date last_modify_date,Date cr_date, String org, Integer headcount) {
        this.detail_id = detail_id;
        this.dept_name = dept_name;
        this.job_title = job_title;
        this.job_code = job_code;
//        this.start_date = start_date;
        this.end_date = end_date;
        this.post_status = post_status;
        this.last_modify_date = last_modify_date;
        this.cr_date = cr_date;
        this.org = org;
        this.headcount = headcount;
    }

    public JobVO(Integer detail_id, String dept_name, String job_title, String job_code, Date start_date, Date end_date, String post_status,Date last_modify_date,Date cr_date,Date close_date, String org, Integer headcount, Byte is_sealed, BigInteger recruitment_count) {
        this.detail_id = detail_id;
        this.dept_name = dept_name;
        this.job_title = job_title;
        this.job_code = job_code;
//        this.start_date = start_date;
        this.end_date = end_date;
        this.post_status = post_status;
        this.last_modify_date = last_modify_date;
        this.cr_date = cr_date;
        this.close_date = close_date;
        this.org = org;
        this.headcount = headcount;
        this.is_sealed = is_sealed;
        this.recruitment_count = recruitment_count;
    }

    public JobVO(Integer detail_id, String job_title, String job_code, Integer headcount, Character status, BigInteger recruitment_count, BigInteger bookmark_count) {
        this.detail_id = detail_id;
        this.job_title = job_title;
        this.job_code = job_code;
//        this.start_date = start_date;
        this.headcount = headcount;
        this.status = status;
        this.recruitment_count = recruitment_count;
        this.bookmark_count = bookmark_count;
    }


}
