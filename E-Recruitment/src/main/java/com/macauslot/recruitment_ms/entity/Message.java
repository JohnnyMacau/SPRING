package com.macauslot.recruitment_ms.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Message implements Serializable {
    private static final long serialVersionUID = 7292340818965587470L;
    private Integer id;
    private String status;
    private String code;
    private String engDesc;
    private String chnDesc;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "STATUS",columnDefinition = "char")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "ENG_DESC")
    public String getEngDesc() {
        return engDesc;
    }

    public void setEngDesc(String engDesc) {
        this.engDesc = engDesc;
    }

    @Basic
    @Column(name = "CHN_DESC")
    public String getChnDesc() {
        return chnDesc;
    }

    public void setChnDesc(String chnDesc) {
        this.chnDesc = chnDesc;
    }


}
