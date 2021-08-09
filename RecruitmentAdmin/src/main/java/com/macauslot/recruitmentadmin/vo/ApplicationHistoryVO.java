package com.macauslot.recruitmentadmin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationHistoryVO {
	Integer application_pos_id;
	String cr_date;
	String job_code;
	String result;
	String remark;
}
