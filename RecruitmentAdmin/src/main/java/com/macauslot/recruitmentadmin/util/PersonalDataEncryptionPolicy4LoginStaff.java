package com.macauslot.recruitmentadmin.util;

import com.macauslot.recruitmentadmin.dto.UserDTO;
import com.macauslot.recruitmentadmin.vo.ApplicantInfoVO;
import com.macauslot.recruitmentadmin.vo.ResumeVO;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: jim.deng
 * @Date: 2021/4/26 17:38
 */
public class PersonalDataEncryptionPolicy4LoginStaff {
    private static final String MASK = "***";

    public static void setProfileMask(UserDTO loginStaff, ResumeVO profile) {
        boolean isServerRoleDept = ServerRoleTagEnum.DEPT == loginStaff.getRoleTag();
        Boolean isFLTAdmin = loginStaff.getIsFLTAdmin();
        boolean isNotFltAdmin = isFLTAdmin == null || !isFLTAdmin;
        setProfileMask(isServerRoleDept, isNotFltAdmin, profile);
    }

    public static void setProfileMask(boolean isServerRoleDept, boolean isNotFltAdmin, ResumeVO profile) {
        if (isServerRoleDept) {
            ApplicantInfoVO a = profile.getApplicantInfoVO();
            a.setIdCardNumber(mask(a.getIdCardNumber()));
            a.setAccount(mask(a.getAccount()));
//            a.setAddress_1(mask(a.getAddress_1()));
            a.setAddress_2(mask(a.getAddress_2()));
            a.setAddress_3(mask(a.getAddress_3()));
            a.setSocial(mask(a.getSocial()));
            Date dob = a.getDob();
            a.setAgeCalculateByDob(getAge(dob));
            a.setDob(null);
            if (isNotFltAdmin) {
                a.setAddress_1(mask(a.getAddress_1()));
                a.setAreaCode(mask(a.getAreaCode()));
                a.setMobile(mask(a.getMobile()));
                a.setEmailAddress(mask(a.getEmailAddress()));
                a.setContactName(mask(a.getContactName()));
                a.setContactPhoneAreaCode(mask(a.getContactPhoneAreaCode()));
                a.setContactPhone(mask(a.getContactPhone()));
                a.setContactRelation(mask(a.getContactRelation()));
            }
        }
    }

    private static String mask(String str) {
        return StringUtils.isBlank(str) ? "" : MASK;
    }

    //根据时间获取年龄
    private static String getAge(Date birthdaynow) {
        if (birthdaynow == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();//当前时间
        if (cal.before(birthdaynow)) {
            throw new IllegalArgumentException("出生日期小于当前时间，无效的日期!");
        }
        //当前的年月日
        int yearNow = cal.get(Calendar.YEAR);  //年
        int monthNow = cal.get(Calendar.MONTH);  //月
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //日
        cal.setTime(birthdaynow);//出生时间Date
        //出生的年月日
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        String s2 = String.valueOf(age);//int转为String类型
        return s2;

    }
}
