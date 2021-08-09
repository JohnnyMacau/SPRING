package com.macauslot.recruitmentadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * SELECT
 * CASE
 * WHEN PASSWORD_MODIFY_DATE IS NULL
 * OR UNIX_TIMESTAMP(PASSWORD_MODIFY_DATE) + 90 * 60 * 60 * 24 < UNIX_TIMESTAMP(CURRENT_TIMESTAMP) THEN
 * 1
 * ELSE
 * 0
 * END EXPIRED,
 * IFNULL(email, '') email,
 * STATUS,
 * ec. NAME,
 * ec.phone,
 * ec.relation_ship
 * FROM
 * USER
 * LEFT JOIN EMERGENCY_CONTACT ec ON USER .id = ec.user_id
 * WHERE
 * USER_NAME = 80653
 * AND PASSWORD = 'e99a18c428cb38d5f260853678922e03'
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPO implements Converter<UserPO> {
    private Object expired;
    private String email;
    private Character status;
    private String employeeId;
    private String deptCode;
    private String name;
    private String relativeName;
    private String phone;
    private String relationShip;
    private String remark;


    @Override
    public UserPO convertToVO() {
        this.setRelativeName(null);
        this.setRelationShip(null);
        this.setPhone(null);
        return this;
    }
}
