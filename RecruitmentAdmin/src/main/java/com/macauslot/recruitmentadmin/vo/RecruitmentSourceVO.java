package com.macauslot.recruitmentadmin.vo;

import com.macauslot.recruitmentadmin.annotation.Sign;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jim.deng
 */
@Data
//@AllArgsConstructor
public class RecruitmentSourceVO {
    private  Character messageStatus;
    private  String messageGroupDesc;
    private  String messageDesc;
    private Integer messageId;

    @Sign("findMsgDescList")
    public RecruitmentSourceVO(Character messageStatus, String messageGroupDesc, String messageDesc, Integer messageId) {
        this.messageStatus = messageStatus;
        this.messageGroupDesc = messageGroupDesc;
        this.messageDesc = messageDesc;
        this.messageId = messageId;
    }
}
