package com.macauslot.recruitmentadmin.vo;


import com.macauslot.recruitmentadmin.annotation.Sign;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class ApplicantInfoVO {
    private Integer applicantInfoId;
    private String enLName;
    private String enFName;
    private String cnLName;
    private String cnFName;
    private String id_type;
    private Integer type_id;
    private String idCardNumber;
    private Character gender;
    private Date dob;
    private String martialStatus;
    private String mobile;
    private String areaCode;
    private String emailAddress;
    //    private Character employedBefore;
    private String sourceRef;
    private String social;
    private String applicationOrg;
    private String country;
    private String district;
    private String account;

    private String contactName;
    private String contactPhone;
    private String contactPhoneAreaCode;
    private String contactRelation;
    private String relationship;
    private Byte in_service;
    private String relative_name;

    private String department;

    private Integer notice_day;
    private String notice_day_type;
    private Date availableWorkDate;

    private String expectedSalary;
    private String expectedSalaryType;

    private String nationality;
    private String address_1;
    private String address_2;
    private String address_3;


    private String ebDept;
    private String ebPosi;
    private Integer ebUserName;
    private String crCause;
    private String tbCause;

    private String employedBefore;
    private String appliedBaccountBefore;
    private String terminatedBefore;
    private String criminalRecord;

    private String applicantName;
    //    private String education;//學曆改分成以下三列 學歷、學校、專科
    private String degree;//學歷
    private String organizationName;//學校
    private String major;//專科

    private String status;
    private String area;
    private String blackListed;

    @Sign("searchApplicant")
    public ApplicantInfoVO(Integer applicantInfoId,
                           String applicantName,
                           String id_type,
                           String idCardNumber,
                           Character gender,
                           String area,
                           String degree,
                           String organizationName,
                           String major,
                           Byte blackListed,
                           Character status,
                           String applicationOrg) {
        this.applicantInfoId = applicantInfoId;
        this.applicantName = applicantName;
        this.id_type = id_type;
        this.idCardNumber = idCardNumber;
        this.gender = gender;
        this.area = area;
        this.degree = degree;
        this.organizationName = organizationName;
        this.major = major;
        this.blackListed = String.valueOf(blackListed);
        this.status = String.valueOf(status);
        this.applicationOrg = applicationOrg;
    }


    @Sign("findApplicantPersonalInfoByApplicantId")
    public ApplicantInfoVO(Integer applicantInfoId, Character status, String enLName, String enFName, String cnLName, String cnFName, String id_type, Integer type_id, String idCardNumber,
                           String area, Character gender, Date dob, String martialStatus, String mobile, String areaCode, String emailAddress, String sourceRef, String social, String applicationOrg, String country, String district, String account, String contactName, String contactPhone, String contactPhoneAreaCode, String contactRelation, String relationship, Byte in_service, String relative_name, String department, Integer notice_day, String notice_day_type, Date availableWorkDate, String expectedSalary, String expectedSalaryType, String nationality, String address_1, String address_2, String address_3, String ebDept, String ebPosi, Integer ebUserName, String crCause, String tbCause, String employedBefore, String appliedBaccountBefore, String terminatedBefore, String criminalRecord) {
        this.applicantInfoId = applicantInfoId;
        this.status = String.valueOf(status);
        this.enLName = enLName;
        this.enFName = enFName;
        this.cnLName = cnLName;
        this.cnFName = cnFName;
        this.id_type = id_type;
        this.type_id = type_id;
        this.idCardNumber = idCardNumber;
        this.area = area;
        this.gender = gender;
        this.dob = dob;
        this.martialStatus = martialStatus;
        this.mobile = mobile;
        this.areaCode = areaCode;
        this.emailAddress = emailAddress;
        this.sourceRef = sourceRef;
        this.social = social;
        this.applicationOrg = applicationOrg;
        this.country = country;
        this.district = district;
        this.account = account;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactPhoneAreaCode = contactPhoneAreaCode;
        this.contactRelation = contactRelation;
        this.relationship = relationship;
        this.in_service = in_service;
        this.relative_name = relative_name;
        this.department = department;
        this.notice_day = notice_day;
        this.notice_day_type = notice_day_type;
        this.availableWorkDate = availableWorkDate;
        this.expectedSalary = expectedSalary;
        this.expectedSalaryType = expectedSalaryType;
        this.nationality = nationality;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.address_3 = address_3;
        this.ebDept = ebDept;
        this.ebPosi = ebPosi;
        this.ebUserName = ebUserName;
        this.crCause = crCause;
        this.tbCause = tbCause;
        this.employedBefore = employedBefore;
        this.appliedBaccountBefore = appliedBaccountBefore;

        this.terminatedBefore = terminatedBefore;
        this.criminalRecord = criminalRecord;
    }

    private String cnGender;
    private String cnIn_service;

    private String ageCalculateByDob;
}
