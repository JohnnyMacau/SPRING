package com.macauslot.recruitment_ms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.macauslot.recruitment_ms.util.BeanCopyUtil;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "applicant_info")
public class ApplicantInfo implements Serializable, BaseEntity4Update<ApplicantInfo> {
    private static final long serialVersionUID = 333461995330586711L;
    private Integer applicantInfoId;
    private String userName;
    private String password;
    private Date crDate;
    private Date lastModifyDate;
    private String enFName;
    private String enLName;
    private String cnFName;
    private String cnLName;
    private Integer idTypeId;
    private Integer sourceId;

    private String sourceRef;

    private String idCardNumber;
    private Date dob;
    private String gender;
    /**
     * 婚姻状况过时,但是不能为空
     */
//    private String martialStatus = "Deprecated";
    private String martialStatus;

    private String nationality;
    private String address_1;
    private String address_2;
    private String address_3;
    private String area;


    private String areaCode;
//    private String areaCode_2;

    private String mobile;
//    private String mobile_2;

    private String emailAddress;
    private String employedBefore;
    private String appliedBaccountBefore;

    private String ebDept;
    private String ebPosi;
    private String ebTime;
    private Integer  ebUserName;

    private String criminalRecord;
    private String crCause;
    private Integer noticeDay;
    private String noticeDayType;

    private Date availableWorkDate;
    private String expectedSalary;
    private String expectedSalaryType;


    /**
     * 本程序包注册的,默认添加MSLOT
     */
    private String applicationOrg = "SLT";
    private String emailStatus;
    private String status;
    private Integer incompletePageNo;
    private String emailChecking;
    private String lock;
    private Date lockTime;
    private Date tryTime;
    private Integer tryCount;
    private String terminatedBefore;
    private String tbCause;
    private String sendmailOwl;
    private String contactName;
    private String contactPhone;
    private String contactPhoneAreaCode;

    private String contactRelation;
    private String country;
    private String district;
    private String account;
    private String social;


    private transient String token;
    private List<Integer> positionIdInApplicationList;

    @Transient
    public List<Integer> getPositionIdInApplicationList() {
        return positionIdInApplicationList;
    }

    public void setPositionIdInApplicationList(List<Integer> positionIdInApplicationList) {
        this.positionIdInApplicationList = positionIdInApplicationList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_info_id")
    public Integer getApplicantInfoId() {
        return applicantInfoId;
    }

    public void setApplicantInfoId(Integer applicantInfoId) {
        this.applicantInfoId = applicantInfoId;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "cr_date")
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getCrDate() {
        return crDate;
    }

    public void setCrDate(Date crDate) {
        this.crDate = crDate;
    }

    @Basic
    @Column(name = "last_modify_date", insertable = false)
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    @Basic
    @Column(name = "en_f_name")
    public String getEnFName() {
        return enFName;
    }

    public void setEnFName(String enFName) {
        this.enFName = enFName;
    }

    @Basic
    @Column(name = "en_l_name")
    public String getEnLName() {
        return enLName;
    }

    public void setEnLName(String enLName) {
        this.enLName = enLName;
    }

    @Basic
    @Column(name = "cn_f_name")
    public String getCnFName() {
        return cnFName;
    }

    public void setCnFName(String cnFName) {
        this.cnFName = cnFName;
    }

    @Basic
    @Column(name = "cn_l_name")
    public String getCnLName() {
        return cnLName;
    }

    public void setCnLName(String cnLName) {
        this.cnLName = cnLName;
    }

    @Basic
    @Column(name = "id_type_id")
    public Integer getIdTypeId() {
        return idTypeId;
    }

    public void setIdTypeId(Integer idTypeId) {
        this.idTypeId = idTypeId;
    }

    @Basic
    @Column(name = "source_id")
    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Basic
    @Column(name = "source_ref")
    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    @Basic
    @Column(name = "id_card_number")
    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    @Basic
    @Column(name = "dob")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Basic
    @Column(name = "gender", columnDefinition = "char")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "martial_status")
    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    @Basic
    @Column(name = "nationality")
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Basic
    @Column(name = "address_1")
    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    @Basic
    @Column(name = "address_2")
    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    @Basic
    @Column(name = "address_3")
    public String getAddress_3() {
        return address_3;
    }

    public void setAddress_3(String address_3) {
        this.address_3 = address_3;
    }

    @Basic
    @Column(name = "area")
    public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

    @Basic
    @Column(name = "area_code")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

   /* @Basic
    @Column(name = "area_code_2")
    public String getAreaCode_2() {
        return areaCode_2;
    }

    public void setAreaCode_2(String areaCode_2) {
        this.areaCode_2 = areaCode_2;
    }
*/
    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

   /* @Basic
    @Column(name = "mobile_2")
    public String getMobile_2() {
        return mobile_2;
    }

    public void setMobile_2(String mobile_2) {
        this.mobile_2 = mobile_2;
    }
*/
    @Basic
    @Column(name = "email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Basic
    @Column(name = "employed_before", columnDefinition = "char")
    public String getEmployedBefore() {
        return employedBefore;
    }

    public void setEmployedBefore(String employedBefore) {
        this.employedBefore = employedBefore;
    }
    @Basic
    @Column(name = "applied_baccount_before", columnDefinition = "char")
    public String getAppliedBaccountBefore() {
        return appliedBaccountBefore;
    }

    public void setAppliedBaccountBefore(String appliedBaccountBefore) {
        this.appliedBaccountBefore = appliedBaccountBefore;
    }

    @Basic
    @Column(name = "eb_dept")
    public String getEbDept() {
        return ebDept;
    }

    public void setEbDept(String ebDept) {
        this.ebDept = ebDept;
    }

    @Basic
    @Column(name = "eb_posi")
    public String getEbPosi() {
        return ebPosi;
    }

    public void setEbPosi(String ebPosi) {
        this.ebPosi = ebPosi;
    }

    @Basic
    @Column(name = "eb_time")
    public String getEbTime() {
        return ebTime;
    }

    public void setEbTime(String ebTime) {
        this.ebTime = ebTime;
    }

    @Basic
    @Column(name = "eb_user_name")
    public Integer getEbUserName() {
        return ebUserName;
    }

    public void setEbUserName(Integer ebUserName) {
        this.ebUserName = ebUserName;
    }

    @Basic
    @Column(name = "criminal_record", columnDefinition = "char")
    public String getCriminalRecord() {
        return criminalRecord;
    }

    public void setCriminalRecord(String criminalRecord) {
        this.criminalRecord = criminalRecord;
    }

    @Basic
    @Column(name = "cr_cause", columnDefinition = "text")
    public String getCrCause() {
        return crCause;
    }

    public void setCrCause(String crCause) {
        this.crCause = crCause;
    }

    @Basic
    @Column(name = "notice_day")
    public Integer getNoticeDay() {
        return noticeDay;
    }

    public void setNoticeDay(Integer noticeDay) {
        this.noticeDay = noticeDay;
    }

    @Basic
    @Column(name = "notice_day_type")
    public String getNoticeDayType() {
        return noticeDayType;
    }

    public void setNoticeDayType(String noticeDayType) {
        this.noticeDayType = noticeDayType;
    }

    @Basic
    @Column(name = "available_work_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date getAvailableWorkDate() {
        return availableWorkDate;
    }

    public void setAvailableWorkDate(Date availableWorkDate) {
        this.availableWorkDate = availableWorkDate;
    }

    @Basic
    @Column(name = "expected_salary")
    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }


    @Basic
    @Column(name = "expected_salary_type")
    public String getExpectedSalaryType() {
        return expectedSalaryType;
    }

    public void setExpectedSalaryType(String expectedSalaryType) {
        this.expectedSalaryType = expectedSalaryType;
    }


    @Basic
    @Column(name = "application_org")
    public String getApplicationOrg() {
        return applicationOrg;
    }

    public void setApplicationOrg(String applicationOrg) {
        this.applicationOrg = applicationOrg;
    }

    @Basic
    @Column(name = "email_status", columnDefinition = "char")
    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    @Basic
    @Column(name = "status", columnDefinition = "char")
    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "incomplete_page_no")
    public Integer getIncompletePageNo() {
        return incompletePageNo;
    }

    public void setIncompletePageNo(Integer incompletePageNo) {
        this.incompletePageNo = incompletePageNo;
    }

    @Basic
    @Column(name = "email_checking", columnDefinition = "char")
    public String getEmailChecking() {
        return emailChecking;
    }

    public void setEmailChecking(String emailChecking) {
        this.emailChecking = emailChecking;
    }

    @Basic
    @Column(name = "\"lock\"")
    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    @Basic
    @Column(name = "lock_time")
    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    @Basic
    @Column(name = "try_time")
    public Date getTryTime() {
        return tryTime;
    }

    public void setTryTime(Date tryTime) {
        this.tryTime = tryTime;
    }

    @Basic
    @Column(name = "try_count")
    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    @Basic
    @Column(name = "terminated_before", columnDefinition = "char")
    public String getTerminatedBefore() {
        return terminatedBefore;
    }

    public void setTerminatedBefore(String terminatedBefore) {
        this.terminatedBefore = terminatedBefore;
    }

    @Basic
    @Column(name = "tb_cause", columnDefinition = "text")
    public String getTbCause() {
        return tbCause;
    }

    public void setTbCause(String tbCause) {
        this.tbCause = tbCause;
    }

    @Basic
    @Column(name = "sendmail_OWL", columnDefinition = "char")
    public String getSendmailOwl() {
        return sendmailOwl;
    }

    public void setSendmailOwl(String sendmailOwl) {
        this.sendmailOwl = sendmailOwl;
    }

    @Basic
    @Column(name = "contact_name")
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Basic
    @Column(name = "contact_phone")
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    @Basic
    @Column(name = "contact_phone_area_code")
    public String getContactPhoneAreaCode() {
        return contactPhoneAreaCode;
    }

    public void setContactPhoneAreaCode(String contactPhoneAreaCode) {
        this.contactPhoneAreaCode = contactPhoneAreaCode;
    }
    @Basic
    @Column(name = "contact_relation")
    public String getContactRelation() {
        return contactRelation;
    }

    public void setContactRelation(String contactRelation) {
        this.contactRelation = contactRelation;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "district")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Basic
    @Column(name = "account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "social")
    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }


    @Transient
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }





    public void copy(ApplicantInfo applicantInfo) {
        BeanCopyUtil.beanCopyExceptNull(applicantInfo, this);
    }

    @Override
    public void update(ApplicantInfo applicantInfo) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(applicantInfo, this, "applicantInfoId", "enFName", "enLName", "cnFName", "cnLName", "userName", "idTypeId", "idCardNumber", "emailAddress");
    }
}
