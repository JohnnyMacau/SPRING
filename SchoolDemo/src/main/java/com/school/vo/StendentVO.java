package com.school.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class StendentVO {
	String id;
	String name;
	String classid;
	String classname;
	
	public StendentVO(String name) {
		this.name = name;
	}
	
}
