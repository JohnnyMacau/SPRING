package com.macauslot.recruitmentadmin.entity;

import com.macauslot.recruitmentadmin.entity.group.Create;
import com.macauslot.recruitmentadmin.entity.group.Delete;
import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "department_position")
public class DepartmentPosition extends BaseEntity<DepartmentPosition> {
    @NotNull(groups = {Delete.class}, message = "請選擇刪除項目")
    private Integer deptPosId;
    @NotNull(groups = {Create.class}, message = "departmentCode is null")
    private String departmentCode;
    @NotBlank(groups = {Create.class}, message = "jobTitle is null")
    private String jobTitle;
    private String status;
    private Integer orgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_pos_id", nullable = false)
    public Integer getDeptPosId() {
        return deptPosId;
    }

    public void setDeptPosId(Integer deptPosId) {
        this.deptPosId = deptPosId;
    }

    @Basic
    @Column(name = "department_code", nullable = false)
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Basic
    @Column(name = "job_title", nullable = false, length = 50)
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 1, columnDefinition = "char")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Basic
    @Column(name = "org_id", nullable = false)
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
    
    @Override
    public void update(DepartmentPosition departmentPosition) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(departmentPosition,
                this, "dept_pos_id",
                "department_code", "job_title");
    }
}
