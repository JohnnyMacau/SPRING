package com.macauslot.recruitmentadmin.exception;

/**
 * 发送邮件异常
 * @author jim.deng
 */
public class SendEmailException extends ServiceException {


    private static final long serialVersionUID = 8192972732181416484L;

    public SendEmailException() {
    }

    public SendEmailException(String message) {
        super(message);
    }

    public SendEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendEmailException(Throwable cause) {
        super(cause);
    }

    public SendEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
