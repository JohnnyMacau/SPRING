package com.macauslot.recruitmentadmin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jim.deng
 */
@Data
@AllArgsConstructor
public class DeptPositionDescVO {
    private String description;
    private String jobDesc;
    private String jobCode;
    private String jobTitle;
    private Byte needShift;

}
