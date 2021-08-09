package com.macauslot.recruitmentadmin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailVO {
	Integer job_id;
	Integer application_count;
	String job_title;
	String group_member;
}
