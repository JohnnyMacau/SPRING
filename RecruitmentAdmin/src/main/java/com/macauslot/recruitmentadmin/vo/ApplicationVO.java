package com.macauslot.recruitmentadmin.vo;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@ToString
public class ApplicationVO {
	Integer applicant_pos_id;
    Integer applicant_info_id;
	Integer dept_pos_detail_id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date apply_date;
    String job_Code;
    String job_title;
    String belong_job_code;
    String belong_job_title;
    String bookmarked_job_code;
    String bookmarked_job_title;
    String ad_source;
    String applicant_Name;
    String id_type;
    String id_card_number;
    Character gender;
    String age;
    String mobile;
    String area;
    //    private String education;//學曆改分成以下三列 學歷、學校、專科
     String degree;//學歷
     String organizationName;//學校
     String major;//專科
    String blackListed;
    String duplicated;
    String process_stage;
    String process_stage_code; //1-12
    String dept_reply_date;
    String prefer_interview_time1;
    String prefer_interview_time2;
    String prefer_interview_time3;
    String prefer_interview_time4;
    String prefer_interview_time5;
    String interview_time;
    Integer meeting_room_id;
    String meeting_room_name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date employ_date;
    String job_dept;
    String result; // 應聘結果
    String operation;
    String operation_desc;


    //filter
    Date start_date;
    Date end_date;
    String department_code;
    String status; //job status
	Integer belong_job_id;
	Integer bookmarked_job_id;
	String user_dept; //登入用戶部門
	String process_status; //A:未結束 S:已結束 11/12
	String employ_status; //A:已入职 S:未入职
	String process_stage_code1; //查看狀態
	String org;
	Integer interview_arranged;

//    public ApplicationVO(Integer applicant_pos_id, Integer applicant_info_id, Date apply_date, String job_Code, String ad_source, String applicant_Name, String id_card_number, Character gender,
//    		String age, String mobile, String area, String education, Integer blacklisted, Integer duplicated, String process_stage) {
//    	this.applicant_pos_id = applicant_pos_id;
//    	this.applicant_info_id = applicant_info_id;
//        this.apply_date = apply_date;
//        this.job_Code = job_Code;
//        this.ad_source = ad_source;
//        this.applicant_Name = applicant_Name;
//        this.id_card_number = id_card_number;
//        this.gender = gender;
//        this.age = age;
//        this.mobile = mobile;
//        this.area = area;
//        this.education = education;
//        this.blacklisted = blacklisted;
//        this.duplicated = duplicated;
//        this.process_stage = process_stage;
//    }

    public ApplicationVO(Integer applicant_pos_id, Integer applicant_info_id, Integer dept_pos_detail_id, Date apply_date, String job_Code, String job_title, Integer belong_job_id, String belong_job_code, String belong_job_title, Integer bookmarked_job_id, String bookmarked_job_code, String bookmarked_job_title, String ad_source, String applicant_Name, String id_type, String id_card_number, Character gender,
    		String age, String area, String mobile, String degree,
                         String organizationName,
                         String major, String process_stage, String process_stage_code, String dept_reply_date, String prefer_interview_time1, String prefer_interview_time2, String prefer_interview_time3, String prefer_interview_time4, String prefer_interview_time5,
    		String interview_time, Integer meeting_room_id, Date employ_date, String job_dept, String result, String blackListed, String duplicated, String operation, String operation_desc, String meeting_room_name
    		) {
    	this.applicant_pos_id = applicant_pos_id;
    	this.applicant_info_id = applicant_info_id;
    	this.dept_pos_detail_id = dept_pos_detail_id;
        this.apply_date = apply_date;
        this.job_Code = job_Code;
        this.job_title = job_title;
        this.belong_job_id = belong_job_id;
        this.belong_job_code = belong_job_code;
        this.belong_job_title = belong_job_title;
        this.bookmarked_job_id = bookmarked_job_id;
        this.bookmarked_job_code = bookmarked_job_code;
        this.bookmarked_job_title = bookmarked_job_title;
        this.ad_source = ad_source;
        this.applicant_Name = applicant_Name;
        this.id_type = id_type;
        this.id_card_number = id_card_number;
        this.gender = gender;
        this.age = age;
        this.area = area;
        this.mobile = mobile;
//        this.education = education;
        this.degree = degree;
        this.organizationName = organizationName;
        this.major = major;
        this.process_stage = process_stage;
        this.process_stage_code = process_stage_code;
        this.dept_reply_date = dept_reply_date;
        this.prefer_interview_time1 = prefer_interview_time1;
        this.prefer_interview_time2 = prefer_interview_time2;
        this.prefer_interview_time3 = prefer_interview_time3;
        this.prefer_interview_time4 = prefer_interview_time4;
        this.prefer_interview_time5 = prefer_interview_time5;
        this.interview_time = interview_time;
        this.meeting_room_id = meeting_room_id;
        this.employ_date = employ_date;
        this.job_dept = job_dept;
        this.result = result;
        this.blackListed = blackListed;
        this.duplicated = duplicated;
        this.operation = operation;
        this.operation_desc = operation_desc;
        this.meeting_room_name = meeting_room_name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationVO that = (ApplicationVO) o;
        return Objects.equals(applicant_info_id, that.applicant_info_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicant_info_id);
    }
}
