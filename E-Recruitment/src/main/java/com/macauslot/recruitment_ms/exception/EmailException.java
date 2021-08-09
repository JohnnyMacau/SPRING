package com.macauslot.recruitment_ms.exception;

/**
 * 发送邮件异常
 * @author jim.deng
 */
public class EmailException extends ServiceException {


    private static final long serialVersionUID = 8192972732181416484L;

    public EmailException() {
    }

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(Throwable cause) {
        super(cause);
    }

    public EmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
