package com.macauslot.recruitmentadmin.entity;

import lombok.ToString;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@ToString
public class Message implements Serializable {
    private static final long serialVersionUID = 7292340818965587470L;
    private Integer id;
    private Integer messageGroupId;
    private String status;
    private String code;
    private String engDesc;
    private String chnDesc;

    public Message() {
    }

    public Message(Integer id, String code, String chnDesc) {
        this.id = id;
        this.code = code;
        this.chnDesc = chnDesc;
    }

//    private Integer orderNum;
    private String description;
//    private Integer astId;

 /*   public Message(Integer id,
                   Integer astId,
                   String code,
                   String engDesc,
                   String chnDesc,
                   String description,
                   Character status,
                   Integer orderNum) {
        this.id = id;
        this.astId = astId;
        this.status = String.valueOf(status);
        this.description = description;
        this.code = code;
        this.engDesc = engDesc;
        this.chnDesc = chnDesc;
        this.orderNum = orderNum;
    }*/

    public Message(String description,
                   String chnDesc,
                   Integer id) {
        this.id = id;
        this.description = description;
        this.chnDesc = chnDesc;
    }
    
    public Message(Integer id, 
    		String description,
            String chnDesc,
            String engDesc) {
    	this.id = id;
    	this.description = description;
    	this.chnDesc = chnDesc;
    	this.engDesc = engDesc;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*@Transient
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
*/
    @Transient
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  /*  @Transient
    public Integer getAstId() {
        return astId;
    }

    public void setAstId(Integer astId) {
        this.astId = astId;
    }*/

    @Basic
    @Column(name = "STATUS", columnDefinition = "char")
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

    @Column(name = "MESSAGE_GROUP_ID")
	public Integer getMessageGroupId() {
		return messageGroupId;
	}

	public void setMessageGroupId(Integer messageGroupId) {
		this.messageGroupId = messageGroupId;
	}


}
