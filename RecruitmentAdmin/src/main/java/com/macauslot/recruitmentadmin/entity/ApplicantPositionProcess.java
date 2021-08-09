package com.macauslot.recruitmentadmin.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.ToString;

/**
 * @author jim.deng
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "applicant_position_process")
public class ApplicantPositionProcess extends BaseEntity<ApplicantPositionProcess> implements Serializable {
    private static final long serialVersionUID = 168367446585608324L;
    private Integer applicantPositionProcessId;
    private Integer applicantPositionId;
    private Integer deptPosDetailId;
    private float stage;
    private float newStage;
    private Integer stageAction;
    private String stageActionRemark;
    private Timestamp lastModifyDate;
    private String lastModifyBy;


    public ApplicantPositionProcess() {
    }

    public ApplicantPositionProcess(Integer applicantPositionProcessId, Integer applicantPositionId, Integer stage, Integer stageAction, String stageActionRemark, Timestamp lastModifyDate, String lastModifyBy) {
        this.applicantPositionProcessId = applicantPositionProcessId;
        this.applicantPositionId = applicantPositionId;
        this.stage = stage;
        this.stageAction = stageAction;
        this.stageActionRemark = stageActionRemark;
        this.lastModifyDate = lastModifyDate;
        this.lastModifyBy = lastModifyBy;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "applicant_position_process_id", nullable = false)
	public Integer getApplicantPositionProcessId() {
		return applicantPositionProcessId;
	}

	public void setApplicantPositionProcessId(Integer applicantPositionProcessId) {
		this.applicantPositionProcessId = applicantPositionProcessId;
	}
	
	@Basic
    @Column(name = "applicant_position_id", nullable = true)
	public Integer getApplicantPositionId() {
		return applicantPositionId;
	}

	public void setApplicantPositionId(Integer applicantPositionId) {
		this.applicantPositionId = applicantPositionId;
	}
	
	@Basic
    @Column(name = "dept_pos_detail_id", nullable = true)
	public Integer getDeptPosDetailId() {
		return deptPosDetailId;
	}

	public void setDeptPosDetailId(Integer deptPosDetailId) {
		this.deptPosDetailId = deptPosDetailId;
	}

	@Basic
    @Column(name = "stage", nullable = true, columnDefinition = "float")
	public float getStage() {
		return stage;
	}

	public void setStage(float stage) {
		this.stage = stage;
	}
	
	@Basic
    @Column(name = "new_stage", nullable = true, columnDefinition = "float")
	public float getNewStage() {
		return newStage;
	}

	public void setNewStage(float newStage) {
		this.newStage = newStage;
	}

	@Basic
    @Column(name = "stage_action", nullable = true, columnDefinition = "tinyint")
	public Integer getStageAction() {
		return stageAction;
	}

	public void setStageAction(Integer stageAction) {
		this.stageAction = stageAction;
	}

	@Basic
    @Column(name = "stage_action_remark", nullable = true, length = 255)
	public String getStageActionRemark() {
		return stageActionRemark;
	}

	public void setStageActionRemark(String stageActionRemark) {
		this.stageActionRemark = stageActionRemark;
	}

	@Basic
    @Column(name = "last_modify_date", nullable = true)
	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	@Basic
    @Column(name = "last_modify_by", nullable = true, length = 20)
	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}


}
