package com.macauslot.recruitmentadmin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.macauslot.recruitmentadmin.entity.group.Create;
import com.macauslot.recruitmentadmin.entity.group.Update;
import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "department_position_detail")
public class DepartmentPositionDetail extends BaseEntity<DepartmentPositionDetail> {

	@NotNull(groups = {Update.class},message = "buggetType is null")
	@NotNull(groups = {Create.class},message = "buggetType is null")
    private Byte buggetType;
    private Integer deptPosDetailId;
    @NotNull(groups = {Create.class},message = "deptPosId is null")
    private Integer deptPosId;
    @NotBlank(groups = {Update.class}, message = "status is null")
    @NotBlank(groups = {Create.class}, message = "status is null")
    //D:草稿  A:上線 O:離線  S:關閉  T:模版
    private String status;
    private Boolean isSealed;
//    @NotBlank(groups = {Update.class},message = "jobCode is null")
//    @NotBlank(groups = {Create.class},message = "jobCode is null")
    private String jobCode;
//    @NotBlank(groups = {Update.class},message = "jobDesc is null")
//    @NotBlank(groups = {Create.class},message = "jobDesc is null")
    private String jobDesc;
    @NotNull(groups = {Update.class},message = "headcount is null")
    @NotNull(groups = {Create.class},message = "headcount is null")
    private Integer headcount;
    private Date startDate;
//    @NotNull(groups = {Update.class},message = "endDate is null")
//    @NotNull(groups = {Create.class},message = "endDate is null")
    private Date endDate;
    @NotNull(groups = {Update.class},message = "needShift is null")
    @NotNull(groups = {Create.class},message = "needShift is null")
    private Byte needShift;
    private Date crDate;
    private String createdBy;
    private Date closeDate;
    private String closedBy;
    private Date lastModifyDate;
    private String lastModifyBy;

//    @NotNull(groups = {Update.class},message = "recruitmentGroupId is null")
//    @NotNull(groups = {Create.class},message = "recruitmentGroupId is null")
    private Integer recruitmentGroupId;
    @NotBlank(groups = {Update.class},message = "recruitmentForm is null")
    @NotBlank(groups = {Create.class},message = "recruitmentForm is null")
    private String recruitmentForm;
    @Basic
    @Column(name = "recruitment_form")
    public String getRecruitmentForm() {
        return recruitmentForm;
    }

    public void setRecruitmentForm(String recruitmentForm) {
        this.recruitmentForm = recruitmentForm;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_pos_detail_id", nullable = false)
    public Integer getDeptPosDetailId() {
        return deptPosDetailId;
    }

    public void setDeptPosDetailId(Integer deptPosDetailId) {
        this.deptPosDetailId = deptPosDetailId;
    }

    @Basic
    @Column(name = "dept_pos_id", nullable = false)
    public Integer getDeptPosId() {
        return deptPosId;
    }

    public void setDeptPosId(Integer deptPosId) {
        this.deptPosId = deptPosId;
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
    @Column(name = "is_sealed", nullable = false, columnDefinition = "tinyint")
	public Boolean getIsSealed() {
		return isSealed;
	}

	public void setIsSealed(Boolean isSealed) {
		this.isSealed = isSealed;
	}

    @Basic
    @Column(name = "job_desc", nullable = true, length = 5000)
    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    @Basic
    @Column(name = "start_date", nullable = false)
//    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @JsonFormat(pattern = "MM/dd/yyyy", timezone = "GMT+8")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "need_shift", nullable = false, columnDefinition = "tinyint")
    public Byte getNeedShift() {
        return needShift;
    }

    public void setNeedShift(Byte needShift) {
        this.needShift = needShift;
    }

    @Basic
    @Column(name = "bugget_type", nullable = false, columnDefinition = "tinyint")
    public Byte getBuggetType() {
        return buggetType;
    }

    public void setBuggetType(Byte buggetType) {
        this.buggetType = buggetType;
    }

    @Basic
    @Column(name = "headcount", nullable = false)
    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    @Basic
    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern = "MM/dd/yyyy", timezone = "GMT+8")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "job_code", nullable = false, length = 20)
    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    @Basic
    @CreatedDate
    @Column(name = "cr_date", nullable = false)
    public Date getCrDate() {
        return crDate;
    }

    public void setCrDate(Date crDate) {
        this.crDate = crDate;
    }

    @Basic
    @CreatedBy
    @LastModifiedBy
    @Column(name = "created_by", nullable = false, length = 20)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "close_date", nullable = false)
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

    @Basic
    @Column(name = "closed_by", nullable = false, length = 20)
	public String getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}
    
    @Basic
    @LastModifiedDate
    @Column(name = "last_modify_date", nullable = false)
    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    @Override
    public void update(DepartmentPositionDetail departmentPositionDetail) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(departmentPositionDetail,
                this, "deptPosDetailId",
                "deptPosId", "crDate");
    }

    @Basic
    @Column(name = "last_modify_by", nullable = true, length = 5000)
	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}



    @Basic
    @Column(name = "recruitment_group_id", nullable = true)
    public Integer getRecruitmentGroupId() {
        return recruitmentGroupId;
    }

    public void setRecruitmentGroupId(Integer recruitmentGroupId) {
        this.recruitmentGroupId = recruitmentGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentPositionDetail that = (DepartmentPositionDetail) o;
        return Objects.equals(recruitmentGroupId, that.recruitmentGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recruitmentGroupId);
    }

}
