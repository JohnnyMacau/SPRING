package com.macauslot.recruitment_ms.entity;

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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.macauslot.recruitment_ms.util.BeanCopyUtil;

import lombok.ToString;

/**
 * @author jim.deng
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "applicant_position")
public class ApplicantPosition implements BaseEntity4Update<ApplicantPosition>, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5387713118670832648L;
	private Integer applicantInfoId;
    private Integer deptPosDetailId;
    private Integer jobPriority;
    private Timestamp exportDate;
    private Timestamp crDate;
    private Integer applicantPosId;
    private Integer noticeDay;
    private String noticeDayType;
    private Date availableWorkDate;


    private String expectedSalary;
    private String expectedSalaryType;
    private Integer sourceId;
    private String sourceRef;
    private float processStage;
    private Integer applicantSourceDetailId;
    private Integer bookmarkedJobId;
    private Timestamp deptReplyDate;
    private String preferInterviewTime1;
    private String preferInterviewTime2;
    private String preferInterviewTime3;
    private String preferInterviewTime4;
    private String preferInterviewTime5;
    
    private Timestamp lastModifyDate;
    private String lastModifyBy;

    public ApplicantPosition() {
    }

    public ApplicantPosition(Integer applicantInfoId, Integer deptPosDetailId, Integer jobPriority, Timestamp exportDate, Timestamp crDate) {
        this.applicantInfoId = applicantInfoId;
        this.deptPosDetailId = deptPosDetailId;
        this.jobPriority = jobPriority;
        this.exportDate = exportDate;
        this.crDate = crDate;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_pos_id", nullable = false)
    public Integer getApplicantPosId() {
        return applicantPosId;
    }

    public void setApplicantPosId(Integer applicantPosId) {
        this.applicantPosId = applicantPosId;
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
    @Column(name = "dept_pos_detail_id", nullable = false)
    public Integer getDeptPosDetailId() {
        return deptPosDetailId;
    }

    public void setDeptPosDetailId(Integer deptPosDetailId) {
        this.deptPosDetailId = deptPosDetailId;
    }

    @Basic
    @Column(name = "job_priority", nullable = false)
    public Integer getJobPriority() {
        return jobPriority;
    }

    public void setJobPriority(Integer jobPriority) {
        this.jobPriority = jobPriority;
    }

    @Basic
    @Column(name = "export_date", nullable = true)
    public Timestamp getExportDate() {
        return exportDate;
    }

    public void setExportDate(Timestamp exportDate) {
        this.exportDate = exportDate;
    }

    @Basic
    @CreatedDate
    @Column(name = "cr_date", nullable = false)
    public Timestamp getCrDate() {
        return crDate;
    }

    public void setCrDate(Timestamp crDate) {
        this.crDate = crDate;
    }


    @Basic
    @Column(name = "notice_day", nullable = true)
    public Integer getNoticeDay() {
        return noticeDay;
    }

    public void setNoticeDay(Integer noticeDay) {
        this.noticeDay = noticeDay;
    }

    @Basic
    @Column(name = "notice_day_type", nullable = true, length = 10)
    public String getNoticeDayType() {
        return noticeDayType;
    }

    public void setNoticeDayType(String noticeDayType) {
        this.noticeDayType = noticeDayType;
    }

    @Basic
    @Column(name = "available_work_date", nullable = true)
    public Date getAvailableWorkDate() {
        return availableWorkDate;
    }

    public void setAvailableWorkDate(Date availableWorkDate) {
        this.availableWorkDate = availableWorkDate;
    }

    @Basic
    @Column(name = "expected_salary", nullable = true, length = 20)
    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    @Basic
    @Column(name = "expected_salary_type", nullable = true, length = 10)
    public String getExpectedSalaryType() {
        return expectedSalaryType;
    }

    public void setExpectedSalaryType(String expectedSalaryType) {
        this.expectedSalaryType = expectedSalaryType;
    }

    @Basic
    @Column(name = "source_id", nullable = true)
    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Basic
    @Column(name = "source_ref", nullable = true, length = 100)
    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    @Basic
    @Column(name = "process_stage", nullable = true, columnDefinition = "float")
    public float getProcessStage() {
        return processStage;
    }

    public void setProcessStage(float processStage) {
        this.processStage = processStage;
    }


    @Basic
    @Column(name = "applicant_source_detail_id", nullable = true)
    public Integer getApplicantSourceDetailId() {
        return applicantSourceDetailId;
    }

    public void setApplicantSourceDetailId(Integer applicantSourceDetailId) {
        this.applicantSourceDetailId = applicantSourceDetailId;
    }


	@Basic
    @Column(name = "bookmarked_job_id")
	public Integer getBookmarkedJobId() {
		return bookmarkedJobId;
	}

	public void setBookmarkedJobId(Integer bookmarkedJobId) {
		this.bookmarkedJobId = bookmarkedJobId;
	}

	@Basic
    @Column(name = "dept_reply_date", nullable = true)
	public Timestamp getDeptReplyDate() {
		return deptReplyDate;
	}

	public void setDeptReplyDate(Timestamp deptReplyDate) {
		this.deptReplyDate = deptReplyDate;
	}

    @Basic
    @Column(name = "prefer_interview_time1", nullable = true, length = 20)
	public String getPreferInterviewTime1() {
		return preferInterviewTime1;
	}

	public void setPreferInterviewTime1(String preferInterviewTime1) {
		this.preferInterviewTime1 = preferInterviewTime1;
	}

    @Basic
    @Column(name = "prefer_interview_time2", nullable = true, length = 20)
	public String getPreferInterviewTime2() {
		return preferInterviewTime2;
	}

	public void setPreferInterviewTime2(String preferInterviewTime2) {
		this.preferInterviewTime2 = preferInterviewTime2;
	}

    @Basic
    @Column(name = "prefer_interview_time3", nullable = true, length = 20)
	public String getPreferInterviewTime3() {
		return preferInterviewTime3;
	}

	public void setPreferInterviewTime3(String preferInterviewTime3) {
		this.preferInterviewTime3 = preferInterviewTime3;
	}

    @Basic
    @Column(name = "prefer_interview_time4", nullable = true, length = 20)
	public String getPreferInterviewTime4() {
		return preferInterviewTime4;
	}

	public void setPreferInterviewTime4(String preferInterviewTime4) {
		this.preferInterviewTime4 = preferInterviewTime4;
	}

    @Basic
    @Column(name = "prefer_interview_time5", nullable = true, length = 20)
	public String getPreferInterviewTime5() {
		return preferInterviewTime5;
	}

	public void setPreferInterviewTime5(String preferInterviewTime5) {
		this.preferInterviewTime5 = preferInterviewTime5;
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


    @Override
    public void update(ApplicantPosition applicantPosition) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(applicantPosition, this, "id","applicantInfoId");
    }

}
