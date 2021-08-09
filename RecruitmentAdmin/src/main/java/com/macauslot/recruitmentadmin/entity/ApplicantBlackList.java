package com.macauslot.recruitmentadmin.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

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

@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "applicant_black_list")
public class ApplicantBlackList extends BaseEntity<ApplicantBlackList> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -802052393680936861L;
	
	private Integer applicantBlackListId;
    private String nameEn;
    private String nameCn;
    private String tel;
    private String idNumber;
    private String leaveDate;
    private String remark;
    private String status;
    private String lastModifyBy;
    private Timestamp lastModifyDate;

    public ApplicantBlackList() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_black_list_id", nullable = false)
	public Integer getApplicantBlackListId() {
		return applicantBlackListId;
	}

	public void setApplicantBlackListId(Integer applicantBlackListId) {
		this.applicantBlackListId = applicantBlackListId;
	}

    @Basic
    @Column(name = "name_en")
	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

    @Basic
    @Column(name = "name_cn")
	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

    @Basic
    @Column(name = "tel")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

    @Basic
    @Column(name = "id_number")
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

    @Basic
    @Column(name = "leave_date")
	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

    @Basic
    @Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
    @Column(name = "last_modify_by")
	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

    @Basic
    @Column(name = "last_modify_date")
	public Timestamp getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Timestamp lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}



}
