package com.macauslot.recruitmentadmin.vo;


import com.macauslot.recruitmentadmin.annotation.Sign;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class CandidateCollectingStatisticsVO {

    private String title;
    private BigDecimal peopleCount;
    private BigDecimal percent;

    @Sign("candidateCollectingStatistics")
    public CandidateCollectingStatisticsVO(String title, BigDecimal peopleCount, BigDecimal percent) {
        this.title = title;
        this.peopleCount = peopleCount;
        this.percent = percent;
    }

    private Date applicant_postition_start_date;
    private Date applicant_postition_end_date;


}
