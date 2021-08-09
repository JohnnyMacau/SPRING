package com.macauslot.recruitmentadmin.entity;

import com.macauslot.recruitmentadmin.util.BeanCopyUtil;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@Table(name = "other_skill")
public class OtherSkill extends BaseEntity<OtherSkill> implements Serializable {
    private static final long serialVersionUID = -1423744667211240695L;
    private Integer skillId;
    private String name;
    private String degree;
    private Integer applicantInfoId;
    private String skillType;

    public OtherSkill(String name, String skillType) {
        this.name = name;
        this.skillType = skillType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id", nullable = false)
    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
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
    @Column(name = "degree", nullable = false, length = 20)
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
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
    @Column(name = "skill_type", nullable = true)
    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }


    @Override
    public String toString() {
        return "OtherSkill{" +
                "skillId=" + skillId +
                ", name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", applicantInfoId=" + applicantInfoId +
                ", skillType='" + skillType + '\'' +
                '}';
    }

    @Override
    public void update(OtherSkill otherSkill) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(otherSkill, this, "skillId", "applicantInfoId");

    }
}
