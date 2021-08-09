package com.macauslot.recruitment_ms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author jim.deng
 */
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class DeptPositionDetailVO {
    private Date startDate;
    private Date endDate;
    private Integer deptPosDetailId;
    private String departmentCode;
    private String description;
    private String jobTitle;
    private String jobCode;
    private Byte needShift;
}
