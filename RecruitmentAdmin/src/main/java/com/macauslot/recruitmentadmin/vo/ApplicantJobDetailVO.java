package com.macauslot.recruitmentadmin.vo;
//之前使用之VO，現在不用
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;


@Data
@NoArgsConstructor
@ToString
public class ApplicantJobDetailVO {
    Integer applicant_info_id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date export_date;
    String applicant_Name;
    Character gender;
    Date dob;
    String age;
    String id_card_number;
    String mobile;
    String education;
    String work_experience;
    String application_org;
    Character email_checking;
    Integer dept_pos_detail_id;
    String mail_Status;
    String apply_Position_1;
    String job_Code_1;


    public ApplicantJobDetailVO(Integer applicant_info_id, Date export_date, String applicant_Name, Character gender, Date dob, Object age, String id_card_number, String mobile, String education, String work_experience, String application_org, Character email_checking, Integer dept_pos_detail_id, String mail_Status, String apply_Position_1, String job_Code_1,String process_stage, Integer applicant_position_id) {
        this.applicant_info_id = applicant_info_id;
        this.export_date = export_date;
        this.applicant_Name = applicant_Name;
        this.gender = gender;
        this.dob = dob;
        this.age = String.valueOf(age);
        this.id_card_number = id_card_number;
        this.mobile = mobile;
        this.education = education;
        this.work_experience = work_experience;
        this.application_org = application_org;
        this.email_checking = email_checking;
        this.dept_pos_detail_id = dept_pos_detail_id;
        this.mail_Status = mail_Status;
        this.apply_Position_1 = apply_Position_1;
        this.job_Code_1 = job_Code_1;
        this.process_stage = process_stage;
        this.applicant_position_id = applicant_position_id;

    }

    Date applicant_postition_start_date;
    Date applicant_postition_end_date;
    String departmentCode;
    String export_date_status;
    String process_stage;

    Integer applicant_position_id;
    Integer rowSpan;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicantJobDetailVO that = (ApplicantJobDetailVO) o;
        return Objects.equals(applicant_info_id, that.applicant_info_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicant_info_id);
    }
}
