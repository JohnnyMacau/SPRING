package com.macauslot.recruitmentadmin.vo;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessHistoryVO {
	private Integer applicant_position_id;
	private String job_code;
    private String begin_stage;
    private String operation;
    private String end_stage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp operation_time;  
    private String remark;
    

}
