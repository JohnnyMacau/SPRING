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
public class ApplicantionRecordReportVO {

    private Integer applicantPosId;
    private String applicationOrg;
    private String jobCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicantCrDate;
    private String applicantDeptName;
    private String applicantDeptCode;
    private String jobTitle;
    private String recruitmentSource1;
    private String recruitmentSource2;
    private String recruitmentSource3;

    private Byte isBlacklisted;
    private String cnLastName;
    private String cnFirstName;
    private String enLastName;
    private String enFirstName;
    private String idCardTypeName;
    private String idCardNumber;
    private BigInteger age;
    private Character gender;
    private String martialStatus;
    private String address1;
    private String address2;
    private String address3;
    private String emailAddress;
    private String areaCode1;
    private String mobile1;
    private String relativeName;
    private String degree;
    private String schoolName;
    private String schoolMajor;
    private String allCertification;
    private String hiCompanyName;
    private String hiPosition;
    private String hiPayMethod;
    private String hiCurrency;
    private String hiSalary;
    private String expectedSalaryType;
    private String expectedSalary;
    private String allShiftDate;

    private String employedBefore;
    private String appliedBaccountBefore;
    private String terminatedBefore;
    private String criminalRecord;
    private String processStage;

    private String archivedResult;
    private String appointmentDeptCode;
    private String appointmentJobTitle;


//    private BigInteger countApplyTimes;
//    private String recruitmentForm;

//    private Date dateOfBirth;

//    private String areaCode2;
//    private String mobile2;

//    private String allCompany;
//    private String isOnshift;

  /*  private Date hrRefferralDate;
    private String interviewStatus;
    private String entryApproval;
    private String sendOffer;
    private String entryDate;
    private String appointmentForm;*/

    @Sign("page4ApplicantReport")
    public ApplicantionRecordReportVO(Integer applicantPosId, String applicationOrg, String jobCode, Date applicantCrDate, String applicantDeptName, String applicantDeptCode, String jobTitle, String recruitmentSource1, String recruitmentSource2, String recruitmentSource3, Byte isBlacklisted, String cnLastName, String cnFirstName, String enLastName, String enFirstName, String idCardTypeName, String idCardNumber, BigInteger age, Character gender, String martialStatus, String address1, String address2, String address3, String emailAddress, String areaCode1, String mobile1, String relativeName, String degree, String schoolName, String schoolMajor, String allCertification, String hiCompanyName, String hiPosition, String hiPayMethod, String hiCurrency, String hiSalary, String expectedSalaryType, String expectedSalary, String allShiftDate, String employedBefore, String appliedBaccountBefore, String terminatedBefore, String criminalRecord, String processStage, String archivedResult, String appointmentDeptCode, String appointmentJobTitle) {
        this.applicantPosId = applicantPosId;
        this.applicationOrg = applicationOrg;
        this.jobCode = jobCode;
        this.applicantCrDate = applicantCrDate;
        this.applicantDeptName = applicantDeptName;
        this.applicantDeptCode = applicantDeptCode;
        this.jobTitle = jobTitle;
        this.recruitmentSource1 = recruitmentSource1;
        this.recruitmentSource2 = recruitmentSource2;
        this.recruitmentSource3 = recruitmentSource3;

        this.isBlacklisted = isBlacklisted;
        this.cnLastName = cnLastName;
        this.cnFirstName = cnFirstName;
        this.enLastName = enLastName;
        this.enFirstName = enFirstName;
        this.idCardTypeName = idCardTypeName;
        this.idCardNumber = idCardNumber;
        this.age = age;
        this.gender = gender;
        this.martialStatus = martialStatus;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.emailAddress = emailAddress;
        this.areaCode1 = areaCode1;
        this.mobile1 = mobile1;
        this.relativeName = relativeName;
        this.degree = degree;
        this.schoolName = schoolName;
        this.schoolMajor = schoolMajor;
        this.allCertification = allCertification;
        this.hiCompanyName = hiCompanyName;
        this.hiPosition = hiPosition;
        this.hiPayMethod = hiPayMethod;
        this.hiCurrency = hiCurrency;
        this.hiSalary = hiSalary;
        this.expectedSalaryType = expectedSalaryType;
        this.expectedSalary = expectedSalary;
        this.allShiftDate = allShiftDate;
        this.employedBefore = employedBefore;
        this.appliedBaccountBefore = appliedBaccountBefore;
        this.terminatedBefore = terminatedBefore;
        this.criminalRecord = criminalRecord;
        this.processStage = processStage;
        this.archivedResult = archivedResult;
        this.appointmentDeptCode = appointmentDeptCode;
        this.appointmentJobTitle = appointmentJobTitle;
    }


    private Date applicant_postition_start_date;
    private Date applicant_postition_end_date;
    private String applicantDepartmentCode;
//    private String nameLike;


}
