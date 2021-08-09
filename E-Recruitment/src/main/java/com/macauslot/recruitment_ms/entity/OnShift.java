package com.macauslot.recruitment_ms.entity;

import com.macauslot.recruitment_ms.util.BeanCopyUtil;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "on_shift")
public class OnShift implements Serializable, BaseEntity4Update<OnShift> {
    private static final long serialVersionUID = -2296187878529884779L;
    private Integer onShiftId;
    private String dayofweek;
    private String fromDate;
    private String toDate;
    private Integer applicantInfoId;
    private Integer applicantPositionId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "on_shift_id", nullable = false)
    public Integer getOnShiftId() {
        return onShiftId;
    }

    public void setOnShiftId(Integer onShiftId) {
        this.onShiftId = onShiftId;
    }

    @Basic
    @Column(name = "dayofweek", nullable = false, length = 10)
    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    @Basic
    @Column(name = "from_date", nullable = false, length = 10)
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Basic
    @Column(name = "to_date", nullable = false, length = 10)
    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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
    public void update(OnShift onShift) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(onShift, this, "onShiftId", "applicantInfoId");
    }

    @Basic
    @Column(name = "applicant_position_id", nullable = true)
    public Integer getApplicantPositionId() {
        return applicantPositionId;
    }

    public void setApplicantPositionId(Integer applicantPositionId) {
        this.applicantPositionId = applicantPositionId;
    }


}
