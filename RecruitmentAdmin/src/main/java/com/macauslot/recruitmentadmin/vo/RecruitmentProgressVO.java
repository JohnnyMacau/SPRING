package com.macauslot.recruitmentadmin.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class RecruitmentProgressVO {
	private Integer dept_pos_detail_id;
	private String job_code;
	private String job_title;
	private Integer headcount;
	private Integer recruited;
	private String job_status;
	private String dept;
	
	
	
}
