package com.macauslot.recruitmentadmin.vo;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListVO {
	private Integer applicantBlackListId;
    private String name;
    private String nameEn;
    private String nameCn;
    private String tel;
    private String idNumber;
    private String leaveDate;
    private String remark;
    private String status;
    private String lastModifyBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp lastModifyDate;  
    
    public BlackListVO(Integer applicantBlackListId, String nameEn,String nameCn,String tel,String idNumber, String leaveDate,String remark, String status,String lastModifyBy,Timestamp lastModifyDate) {
    	this.applicantBlackListId = applicantBlackListId;
    	this.nameEn = nameEn;
    	this.nameCn = nameCn;
    	this.tel = tel;
    	this.idNumber = idNumber;
    	this.leaveDate = leaveDate;
    	this.remark = remark;
    	this.status = status;
    	this.lastModifyBy = lastModifyBy;
    	this.lastModifyDate = lastModifyDate;
    }
}
