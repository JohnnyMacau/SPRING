package com.macauslot.recruitment_ms.annotation;

import java.lang.annotation.*;

/**
 * Token注解类--noRepeat
 *
 * @author jim.deng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface NoRepeatToken {
    boolean producer() default false;

    boolean consumer() default false;

    /**
     * 本回合同时需要几个token(默认一个,多的话用来同页面多个提交防重)
     * @return 新旧总token数
     */
    int needProduceTokenNum() default 1;

    /**
     * noRepeatToken的前缀,消耗掉生成token(producer)的那个方法名
     * @return 注解(producer)的那个方法名
     */
    String consumeMethodName() default "";
}
