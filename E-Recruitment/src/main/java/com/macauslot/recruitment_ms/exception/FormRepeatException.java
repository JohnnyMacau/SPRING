package com.macauslot.recruitment_ms.exception;

/**
 * 表单重复提交异常
 */
public class FormRepeatException extends RuntimeException {

    public FormRepeatException(String message) {
        super(message);
    }

    public FormRepeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
