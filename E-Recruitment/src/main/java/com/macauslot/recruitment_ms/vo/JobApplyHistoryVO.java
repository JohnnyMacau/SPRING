package com.macauslot.recruitment_ms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class JobApplyHistoryVO {

    Date cr_date;
    String process_stage;
    String job_title;
    String job_code;
}
