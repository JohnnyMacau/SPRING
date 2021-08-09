package com.macauslot.recruitmentadmin.entity;

import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "working_experience")
public class WorkingExperience extends BaseEntity<WorkingExperience> implements Serializable {
    private static final long serialVersionUID = -3376220940598459542L;
    private Integer experienceId;
    private String companyName;
    private String fromDate;
    private String toDate;
    private String position;
    private String jobDuty;
    private String payMethod;
    private String currency;
    private String salary;
    private String allowance;
    private String stillEmployed;
    private String leaveReason;
    private Integer applicantInfoId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id", nullable = false)
    public Integer getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Integer experienceId) {
        this.experienceId = experienceId;
    }

    @Basic
    @Column(name = "company_name", nullable = false, length = 100)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Basic
    @Column(name = "from_date", nullable = false, length = 11)
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Basic
    @Column(name = "to_date", nullable = false, length = 11)
    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Basic
    @Column(name = "position", nullable = false, length = 50)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Basic
    @Column(name = "job_duty", nullable = false, length = 500)
    public String getJobDuty() {
        return jobDuty;
    }

    public void setJobDuty(String jobDuty) {
        this.jobDuty = jobDuty;
    }

    @Basic
    @Column(name = "pay_method", nullable = false, length = 20)
    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    @Basic
    @Column(name = "currency", nullable = false, length = 20)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Basic
    @Column(name = "salary", nullable = false, length = 20)
    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Basic
    @Column(name = "allowance", nullable = false, length = 20)
    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    @Basic
    @Column(name = "still_employed", columnDefinition = "char", nullable = false, length = 1)
    public String getStillEmployed() {
        return stillEmployed;
    }

    public void setStillEmployed(String stillEmployed) {
        this.stillEmployed = stillEmployed;
    }

    @Basic
    @Column(name = "leave_reason", nullable = false, length = 50)
    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    @Basic
    @Column(name = "applicant_info_id", nullable = false)
    public Integer getApplicantInfoId() {
        return applicantInfoId;
    }

    public void setApplicantInfoId(Integer applicantInfoId) {
        this.applicantInfoId = applicantInfoId;
    }

    @Override
    public String toString() {
        return "WorkingExperience{" +
                "experienceId=" + experienceId +
                ", companyName='" + companyName + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", position='" + position + '\'' +
                ", jobDuty='" + jobDuty + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", currency='" + currency + '\'' +
                ", salary='" + salary + '\'' +
                ", allowance='" + allowance + '\'' +
                ", stillEmployed='" + stillEmployed + '\'' +
                ", leaveReason='" + leaveReason + '\'' +
                ", applicantInfoId=" + applicantInfoId +
                '}';
    }


    @Override
    public void update(WorkingExperience workingExperience) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(workingExperience, this, "experienceId", "applicantInfoId");
    }
}
