package com.macauslot.recruitmentadmin.vo;

import com.macauslot.recruitmentadmin.annotation.Sign;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class DepartmentPositionDetailVO {

    private Byte bugget_type;
    private String org_name;
    private Integer org_id;
    private String dept_code;
    private String dept_name;
    private String job_title;
    private Integer dept_pos_id;
    private Integer dept_pos_detail_id;
    private String job_code;
    private Character status;
    private String job_desc;
    private Integer headcount;
//    private String start_date;
    private String end_date;
    private Date cr_date;
    private String created_by;
    private Byte need_shift;
    private Integer recruitment_group_id;
    private String recruitmentForm;


    @Sign("findDepartmentPositionDetail")
    public DepartmentPositionDetailVO(Byte bugget_type, String org_name, Integer org_id, String dept_code, String dept_name, String job_title, Integer dept_pos_id, Integer dept_pos_detail_id, String job_code, Character status, String job_desc, Integer headcount, String start_date, String end_date, Date cr_date, String created_by, Byte need_shift,Integer recruitment_group_id,String recruitmentForm) {
    	this.bugget_type = bugget_type;
    	this.org_name = org_name;
    	this.org_id = org_id;
    	this.dept_code = dept_code;
    	this.dept_name = dept_name;
        this.job_title = job_title;
        this.dept_pos_id = dept_pos_id;
        this.dept_pos_detail_id = dept_pos_detail_id;
        this.job_code = job_code;
        this.status = status;
        this.job_desc = job_desc;
        this.headcount = headcount;
//        this.start_date = start_date;
        this.end_date = end_date;
        this.cr_date = cr_date;
        this.created_by = created_by;
        this.need_shift = need_shift;
        this.recruitment_group_id = recruitment_group_id;
        this.recruitmentForm = recruitmentForm;

    }
}
