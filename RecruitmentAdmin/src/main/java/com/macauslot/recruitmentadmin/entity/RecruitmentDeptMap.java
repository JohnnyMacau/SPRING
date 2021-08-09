package com.macauslot.recruitmentadmin.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "recruitment_dept_map", schema = "hrportal")
public class RecruitmentDeptMap extends BaseEntity<RecruitmentDeptMap> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6472938447430480472L;
	private Integer id;
    private String hrPortalDept;
    private String recruitmentDept;
    private Integer orgId;

    @Id
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "HR_PORTAL_DEPT", nullable = false, length = 20)
    public String getHrPortalDept() {
        return hrPortalDept;
    }

    public void setHrPortalDept(String hrPortalDept) {
        this.hrPortalDept = hrPortalDept;
    }

    @Basic
    @Column(name = "RECRUITMENT_DEPT", nullable = false, length = 20)
    public String getRecruitmentDept() {
        return recruitmentDept;
    }

    public void setRecruitmentDept(String recruitmentDept) {
        this.recruitmentDept = recruitmentDept;
    }
    
    @Basic
    @Column(name = "org_Id", nullable = false)
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecruitmentDeptMap that = (RecruitmentDeptMap) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(hrPortalDept, that.hrPortalDept) &&
                Objects.equals(recruitmentDept, that.recruitmentDept) &&
                Objects.equals(orgId, that.orgId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hrPortalDept, recruitmentDept, orgId);
    }
}
