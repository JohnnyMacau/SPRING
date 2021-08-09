package com.macauslot.recruitment_ms.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class EncryptUtil {
    private static final String SALT = "FLT";

    /**
     * 用MD5对密码进行加密
     *
     * @param srcPassword 用户的密码
     * @return 返回加密后的密码
     */
    public static String getMd5password(String srcPassword) {
        //返回加密后的密码
        return DigestUtils.md5DigestAsHex(srcPassword.getBytes(StandardCharsets.UTF_8));
    }

    public static String getMd5Token(String sessionId) {
        //盐值拼接
        String mix = SALT + sessionId + SALT + sessionId;
        //循环执行摘要运算
        for (int i = 0; i < 5; i++) {
            mix = DigestUtils.md5DigestAsHex(mix.getBytes(StandardCharsets.UTF_8));
        }
        //返回摘要/加密后的
        return mix;
    }
}
