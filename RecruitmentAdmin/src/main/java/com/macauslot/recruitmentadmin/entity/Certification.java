package com.macauslot.recruitmentadmin.entity;

import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicUpdate
@DynamicInsert
public class Certification extends BaseEntity<Certification> implements Serializable {
    private static final long serialVersionUID = 6557355280027711200L;
    private Integer certificationId;
    private String organizationName;
    private String certName;
    private String issueDate;
    private Integer applicantInfoId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id", nullable = false)
    public Integer getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(Integer certificationId) {
        this.certificationId = certificationId;
    }

    @Basic
    @Column(name = "organization_name", nullable = false, length = 50)
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Basic
    @Column(name = "cert_name", nullable = false, length = 50)
    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    @Basic
    @Column(name = "issue_date", nullable = false, length = 10)
    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
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
        return "Certification{" +
                "certificationId=" + certificationId +
                ", organizationName='" + organizationName + '\'' +
                ", certName='" + certName + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", applicantInfoId=" + applicantInfoId +
                '}';
    }


    @Override
    public void update(Certification certification) {
//        System.err.println("调用了证书");
        BeanCopyUtil.beanCopyExceptNullWithIngore(certification, this, "certificationId", "applicantInfoId");

    }
}
