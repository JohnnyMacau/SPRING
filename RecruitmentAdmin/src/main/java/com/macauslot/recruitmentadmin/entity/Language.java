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
public class Language extends BaseEntity<Language> implements Serializable {
    private static final long serialVersionUID = -7191710078221302518L;
    private Integer languageId;
    private String name;
    private String written;
    private String spoken;
    private String certificate;
    private Integer applicantInfoId;

    public Language(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id", nullable = false)
    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
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
    @Column(name = "written", nullable = false, length = 20)
    public String getWritten() {
        return written;
    }

    public void setWritten(String written) {
        this.written = written;
    }

    @Basic
    @Column(name = "spoken", nullable = false, length = 20)
    public String getSpoken() {
        return spoken;
    }

    public void setSpoken(String spoken) {
        this.spoken = spoken;
    }

    @Basic
    @Column(name = "certificate", nullable = true, length = 20)
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
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
    public String toString() {
        return "Language{" +
                "languageId=" + languageId +
                ", name='" + name + '\'' +
                ", written='" + written + '\'' +
                ", spoken='" + spoken + '\'' +
                ", certificate='" + certificate + '\'' +
                ", applicantInfoId=" + applicantInfoId +
                '}';
    }


    @Override
    public void update(Language language) {
        BeanCopyUtil.beanCopyExceptNullWithIngore(language, this, "languageId", "applicantInfoId");
    }
}
