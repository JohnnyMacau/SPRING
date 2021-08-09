package com.macauslot.recruitmentadmin.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "applicant_position_process_result")

public class ApplicantPositionProcessResult extends BaseEntity<ApplicantInfo> implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2438715363982642611L;
	
	private Integer applicantPositionProcessResultId;
    private String index;
    private String code;
    private String desc;

    @Id
    @Column(name = "applicant_position_process_result_id", nullable = false)
    public Integer getApplicantPositionProcessResultId() {
        return applicantPositionProcessResultId;
    }

    public void setApplicantPositionProcessResultId(Integer applicantPositionProcessResultId) {
        this.applicantPositionProcessResultId = applicantPositionProcessResultId;
    }

    
    @Basic
    @Column(name = "index", nullable = false, length = 10)
    public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Basic
    @Column(name = "code", nullable = false, length = 10)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "desc", nullable = false, length = 50)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
