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
@Table(name = "recruitment_group")
@NoArgsConstructor
@ToString
public class RecruitmentGroup extends BaseEntity<RecruitmentGroup> implements Serializable {
    private static final long serialVersionUID = -6525358210157565230L;
    private Integer id;
    private String departmentCode;
    private String name;
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
    @Column(name = "department_code", nullable = false)
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public void update(RecruitmentGroup recruitmentGroup) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(recruitmentGroup,
                this,
                "id"
        );
    }
}
