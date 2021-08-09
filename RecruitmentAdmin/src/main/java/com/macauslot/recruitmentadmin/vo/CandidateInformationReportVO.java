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
public class CandidateInformationReportVO {

    private String cn_last_name;
    private String cn_first_name;
    private String en_last_name;
    private String en_first_name;
    private String id_card_type_name;
    private String id_card_number;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date_of_birth;
    private BigInteger age;
    private Character gender;
    private String martial_status;
    private String address_1;
    private String address_2;
    private String address_3;
    private String area_code_1;
    private String mobile_1;
    private String degree;
    private String school_name;
    private String school_major;
    private String all_certification;
    private String all_company;
    private String employed_before;
    private String applied_baccount_before;
    private String terminated_before;
    private String criminal_record;
    private Byte is_blacklisted;
    private BigInteger count_apply_times;
    private String application_history;

    @Sign("page4CandidateInformationReport")
    public CandidateInformationReportVO(String cn_last_name, String cn_first_name, String en_last_name, String en_first_name, String id_card_type_name, String id_card_number, Date date_of_birth, BigInteger age, Character gender, String martial_status, String address_1, String address_2, String address_3, String area_code_1, String mobile_1, String degree, String school_name, String school_major, String all_certification, String all_company, String employed_before, String applied_baccount_before, String terminated_before, String criminal_record, Byte is_blacklisted, BigInteger count_apply_times, String application_history) {
        this.cn_last_name = cn_last_name;
        this.cn_first_name = cn_first_name;
        this.en_last_name = en_last_name;
        this.en_first_name = en_first_name;
        this.id_card_type_name = id_card_type_name;
        this.id_card_number = id_card_number;
        this.date_of_birth = date_of_birth;
        this.age = age;
        this.gender = gender;
        this.martial_status = martial_status;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.address_3 = address_3;
        this.area_code_1 = area_code_1;
        this.mobile_1 = mobile_1;
        this.degree = degree;
        this.school_name = school_name;
        this.school_major = school_major;
        this.all_certification = all_certification;
        this.all_company = all_company;
        this.employed_before = employed_before;
        this.applied_baccount_before = applied_baccount_before;
        this.terminated_before = terminated_before;
        this.criminal_record = criminal_record;
        this.is_blacklisted = is_blacklisted;
        this.count_apply_times = count_apply_times;
        this.application_history = application_history;
    }


    private String idCardTypeName;
    private String is_blacklisted_str;

}
