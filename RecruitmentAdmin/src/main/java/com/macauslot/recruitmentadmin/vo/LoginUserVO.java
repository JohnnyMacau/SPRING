package com.macauslot.recruitmentadmin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginUserVO {

	private Integer id;
    private String userName;
    private String chName;
    private String deptCode;
    private String recruimentAdminUser;
    
	public LoginUserVO(Integer id, String deptCode, String userName, String chName, String recruimentAdminUser) {
		this.id = id;
		this.deptCode = deptCode;
		this.userName = userName;
		this.chName = chName;
		this.recruimentAdminUser = recruimentAdminUser;
	}
	
    
}
