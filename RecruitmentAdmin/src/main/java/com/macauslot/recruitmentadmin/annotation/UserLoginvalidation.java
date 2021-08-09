package com.macauslot.recruitmentadmin.annotation;

import com.macauslot.recruitmentadmin.util.ServerRoleTagEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 需要登录才能进行操作的注解UserLoginvalidation
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginvalidation {
    //	String platform() default "F";
    boolean needSetUserDTO() default false;

    ServerRoleTagEnum[] serverRoleTagEnum() default {ServerRoleTagEnum.HR};
}
