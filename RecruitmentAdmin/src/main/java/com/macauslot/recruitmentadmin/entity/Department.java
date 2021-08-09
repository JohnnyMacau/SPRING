package com.macauslot.recruitmentadmin.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "department")

public class Department extends BaseEntity<Department> {
    private Integer departmentId;
    private String code;
    private String name;
    private String description;
    private Integer isParent;
    private Integer parentId;
    private String descCode;
    private String position;
    private Integer orgId;

    @Id
    @Column(name = "department_Id", nullable = false)
    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = false, length = 50)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "isParent", nullable = true)
    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    @Basic
    @Column(name = "parentID", nullable = true)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "desc_Code", nullable = true, length = 255)
    public String getDescCode() {
        return descCode;
    }

    public void setDescCode(String descCode) {
        this.descCode = descCode;
    }

    @Basic
    @Column(name = "position", nullable = true, length = 10)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Column(name = "org_Id", nullable = false)
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }



}
