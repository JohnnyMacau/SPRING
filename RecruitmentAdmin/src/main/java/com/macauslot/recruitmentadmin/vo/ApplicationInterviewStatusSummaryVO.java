package com.macauslot.recruitmentadmin.vo;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class ApplicationInterviewStatusSummaryVO {
    String applicationOrg;
    Date startDate;
    Date endDate;
    String stage;
}
