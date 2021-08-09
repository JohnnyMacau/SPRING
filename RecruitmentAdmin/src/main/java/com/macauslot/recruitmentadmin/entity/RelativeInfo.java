package com.macauslot.recruitmentadmin.entity;


import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@ToString
@DynamicUpdate
@DynamicInsert
@Table(name = "relative_info")
public class RelativeInfo extends BaseEntity<RelativeInfo> implements Serializable {
    private static final long serialVersionUID = 9042450467708027793L;
    private Integer relativeId;
    private Integer applicantInfoId;
    private String relationship;
    private String name;
    private Integer departmentId;
    private String departmentName;
    private Integer inService;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relative_id", nullable = false)
    public Integer getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(Integer relativeId) {
        this.relativeId = relativeId;
    }

    @Basic
    @Column(name = "applicant_info_id", nullable = false)
    public Integer getApplicantInfoId() {
        return applicantInfoId;
    }

    public void setApplicantInfoId(Integer applicantInfoId) {
        this.applicantInfoId = applicantInfoId;
    }

    @Basic
    @Column(name = "relationship", nullable = false, length = 300)
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 300)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "department_id", nullable = false)
    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Basic
    @Column(name = "department_name", nullable = false)
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Basic
    @Column(name = "in_service",columnDefinition = "TINYINT", length = 2, nullable = false)
    public Integer getInService() {
        return inService;
    }

    public void setInService(Integer inService) {
        this.inService = inService;
    }








    @Override
    public void update(RelativeInfo relativeInfo) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(relativeInfo, this, "relativeId", "applicantInfoId");

    }
}
