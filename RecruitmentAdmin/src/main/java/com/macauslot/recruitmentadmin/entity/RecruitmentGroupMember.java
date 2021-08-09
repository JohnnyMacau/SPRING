package com.macauslot.recruitmentadmin.entity;

import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @Author: jim.deng
 * @Date: 2021/2/9 12:16
 */
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert

@Entity
@Table(name = "recruitment_group_member")
@NoArgsConstructor
@ToString
public class RecruitmentGroupMember extends BaseEntity<RecruitmentGroupMember> implements Serializable {
    private static final long serialVersionUID = -1187900990636652484L;
    private Integer id;
    private Integer recruitmentGroupId;
    private String role;
    private String employeeId;
    private String lastModBy;
    private Date lastModDate;

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
    @Column(name = "recruitment_group_id", nullable = false)
    public Integer getRecruitmentGroupId() {
        return recruitmentGroupId;
    }

    public void setRecruitmentGroupId(Integer recruitmentGroupId) {
        this.recruitmentGroupId = recruitmentGroupId;
    }

    @Basic
    @Column(name = "role", nullable = false, length = 100)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "employee_id", nullable = false, length = 6)
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Basic
    @Column(name = "last_mod_by", nullable = true, length = 6)
    @LastModifiedBy
    public String getLastModBy() {
        return lastModBy;
    }

    public void setLastModBy(String lastModBy) {
        this.lastModBy = lastModBy;
    }

    @Basic
    @Column(name = "last_mod_date", nullable = true)
    @LastModifiedDate
    public Date getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(Date lastModDate) {
        this.lastModDate = lastModDate;
    }

    @Override
    public void update(RecruitmentGroupMember recruitmentGroupMember) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(recruitmentGroupMember,
                this,
                "id",
                "recruitmentGroupId"
        );
    }

}
