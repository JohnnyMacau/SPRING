package com.macauslot.recruitmentadmin.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "organization")

public class Organization {
    private Integer orgId;
    private String code;
    private String desc;

    @Id
    @Column(name = "org_Id", nullable = false)
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
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
