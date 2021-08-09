package com.macauslot.recruitment_ms.entity;


import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@ToString
@Table(name = "applicant_source_detail")
public class ApplicantSourceDetail implements Serializable {

    private static final long serialVersionUID = -4519656353607072142L;
    private Integer id;
    private Integer messageId;
    private Integer orderNum;
    private String chnDesc;
    private String status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "message_id", nullable = false)
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "order_num", nullable = false)
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Basic
    @Column(name = "chn_desc", nullable = false, length = 50)
    public String getChnDesc() {
        return chnDesc;
    }

    public void setChnDesc(String chnDesc) {
        this.chnDesc = chnDesc;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 1, columnDefinition = "char(1)")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicantSourceDetail that = (ApplicantSourceDetail) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(messageId, that.messageId) &&
                Objects.equals(orderNum, that.orderNum) &&
                Objects.equals(chnDesc, that.chnDesc) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageId, orderNum, chnDesc, status);
    }


}
