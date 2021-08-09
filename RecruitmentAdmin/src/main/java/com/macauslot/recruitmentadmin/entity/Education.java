package com.macauslot.recruitmentadmin.entity;

import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicUpdate
@DynamicInsert
public class Education extends BaseEntity<Education> implements Serializable {
    private static final long serialVersionUID = 122729645351767226L;
    private Integer educationId;
    private String organizationName;
    private String degree;
    private String fromDate;
    private String toDate;
    private String major;
    private Integer applicantInfoId;

    private Date toDate4sort;

    @Transient
    public Date getToDate4sort() {
        return toDate4sort;
    }

    public void setToDate4sort(Date toDate4sort) {
        this.toDate4sort = toDate4sort;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id", nullable = false)
    public Integer getEducationId() {
        return educationId;
    }

    public void setEducationId(Integer educationId) {
        this.educationId = educationId;
    }

    @Basic
    @Column(name = "organization_name", nullable = true, length = 100)
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Basic
    @Column(name = "degree", nullable = true, length = 50)
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Basic
    @Column(name = "from_date", nullable = true, length = 10)
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Basic
    @Column(name = "to_date", nullable = true, length = 10)
    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Basic
    @Column(name = "major", nullable = true, length = 50)
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Basic
    @Column(name = "applicant_info_id", nullable = false)
    public Integer getApplicantInfoId() {
        return applicantInfoId;
    }


    public void setApplicantInfoId(Integer applicantInfoId) {
        this.applicantInfoId = applicantInfoId;
    }


//      public void update(Education education) {
//        BeanCopyUtil.beanCopyExceptNullWithIngore(education, this, "educationId","applicantInfoId");
//    }


    @Override
    public String toString() {
        return "Education{" +
                "educationId=" + educationId +
                ", organizationName='" + organizationName + '\'' +
                ", degree='" + degree + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", major='" + major + '\'' +
                ", applicantInfoId=" + applicantInfoId +
                ", toDate4sort=" + toDate4sort +
                '}';
    }

    @Override
    public void update(Education education) {
//        System.err.println("调用了教育");
        BeanCopyUtil.beanCopyExceptNullWithIngore(education, this, "educationId", "applicantInfoId");

    }
}
