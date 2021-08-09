package com.macauslot.recruitment_ms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jim.deng
 */
@Data
@AllArgsConstructor
public class ApplicantPositionVO {
    private Integer deptPosDetailId;
    private Integer departmentCode;
    private String description;
    private String jobTitle;
    private Integer jobPriority;
    private String jobCode;
    private Byte needShift;

}
