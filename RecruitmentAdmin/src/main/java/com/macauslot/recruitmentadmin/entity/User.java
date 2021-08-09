package com.macauslot.recruitmentadmin.entity;

import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name="user")
public class User {
    private Integer id;
    private String userName;
    private String enFname;
    private String enLname;
    private String nickname;
    private String chName;
    private Integer levelId;
    private String deptCode;
    private String password;
    private String email;
    private String ext;
    private Date passwordModifyDate;
    private String status;
    private String oneTimeApprove;
    private Integer isLocal;
    private String remark;
    private String signFlag;
    private String recruimentAdminUser;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "USER_NAME", nullable = false, length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "EN_FNAME", nullable = true, length = 50)
    public String getEnFname() {
        return enFname;
    }

    public void setEnFname(String enFname) {
        this.enFname = enFname;
    }

    @Basic
    @Column(name = "EN_LNAME", nullable = true, length = 50)
    public String getEnLname() {
        return enLname;
    }

    public void setEnLname(String enLname) {
        this.enLname = enLname;
    }

    @Basic
    @Column(name = "NICKNAME", nullable = true, length = 50)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "CH_NAME", nullable = true, length = 50)
    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    @Basic
    @Column(name = "LEVEL_ID", nullable = false)
    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    @Basic
    @Column(name = "DEPT_CODE", nullable = true, length = 10)
    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "EMAIL", nullable = true, length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "EXT", nullable = true, length = 30)
    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Basic
    @Column(name = "PASSWORD_MODIFY_DATE", nullable = true)
    public Date getPasswordModifyDate() {
        return passwordModifyDate;
    }

    public void setPasswordModifyDate(Date passwordModifyDate) {
        this.passwordModifyDate = passwordModifyDate;
    }

    @Basic
    @Column(name = "STATUS", nullable = false, length = 1, columnDefinition = "char")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "ONE_TIME_APPROVE", nullable = true, length = 1, columnDefinition = "char")
    public String getOneTimeApprove() {
        return oneTimeApprove;
    }

    public void setOneTimeApprove(String oneTimeApprove) {
        this.oneTimeApprove = oneTimeApprove;
    }

//    @Basic
//    @Column(name = "isLocal", nullable = false)
//    public Integer getIsLocal() {
//        return isLocal;
//    }
//
//    public void setIsLocal(Integer isLocal) {
//        this.isLocal = isLocal;
//    }

    @Basic
    @Column(name = "Remark", nullable = false, length = 20)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Basic
    @Column(name = "RECRUIMENT_ADMIN_USER", nullable = false, length = 1, columnDefinition = "char")
	public String getRecruimentAdminUser() {
		return recruimentAdminUser;
	}

	public void setRecruimentAdminUser(String recruimentAdminUser) {
		this.recruimentAdminUser = recruimentAdminUser;
	}
//    @Basic
//    @Column(name = "signFlag", nullable = true, length = 50)
//    public String getSignFlag() {
//        return signFlag;
//    }
//
//    public void setSignFlag(String signFlag) {
//        this.signFlag = signFlag;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.macauslot.recruitmentadmin.entity.User user = (com.macauslot.recruitmentadmin.entity.User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(enFname, user.enFname) &&
                Objects.equals(enLname, user.enLname) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(chName, user.chName) &&
                Objects.equals(levelId, user.levelId) &&
                Objects.equals(deptCode, user.deptCode) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(ext, user.ext) &&
                Objects.equals(passwordModifyDate, user.passwordModifyDate) &&
                Objects.equals(status, user.status) &&
                Objects.equals(oneTimeApprove, user.oneTimeApprove) &&
                Objects.equals(isLocal, user.isLocal) &&
                Objects.equals(remark, user.remark) &&
                Objects.equals(signFlag, user.signFlag)&&
                Objects.equals(recruimentAdminUser, user.recruimentAdminUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, enFname, enLname, nickname, chName, levelId, deptCode, password, email, ext, passwordModifyDate, status, oneTimeApprove, isLocal, remark, signFlag,recruimentAdminUser);
    }
}
