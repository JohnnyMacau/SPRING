package com.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserVO {
	String username;
	String createDate;
	
	public UserVO(String username) {
		this.username = username;
	}
}
