package com.macauslot.recruitmentadmin.vo;


import com.macauslot.recruitmentadmin.annotation.Sign;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class CandidateApplicationReport1VO {

    private String cn_last_name;
    private String cn_first_name;
    private String en_last_name;
    private String en_first_name;
    private String id_card_type_name;
    private String id_card_number;
    private Byte is_blacklisted;
    private BigInteger count_apply_times;

    @Sign("find4CandidateApplicationReport1")
    public CandidateApplicationReport1VO(String cn_last_name, String cn_first_name, String en_last_name, String en_first_name, String id_card_type_name, String id_card_number, Byte is_blacklisted, BigInteger count_apply_times) {
        this.cn_last_name = cn_last_name;
        this.cn_first_name = cn_first_name;
        this.en_last_name = en_last_name;
        this.en_first_name = en_first_name;
        this.id_card_type_name = id_card_type_name;
        this.id_card_number = id_card_number;
        this.is_blacklisted = is_blacklisted;
        this.count_apply_times = count_apply_times;
    }

    List<CandidateApplicationReport2VO> candidateApplicationReport2VOList;



    String applicationOrg;
}
