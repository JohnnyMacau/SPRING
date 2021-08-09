package com.macauslot.recruitmentadmin.exception;

/**
 * 繞過前端驗證異常
 * @author jim.deng
 */
public class UserSkipValidation extends ServiceException {

    private static final long serialVersionUID = 2567537946188391739L;

    public UserSkipValidation() {
    }

    public UserSkipValidation(String message) {
        super(message);
    }

    public UserSkipValidation(String message, Throwable cause) {
        super(message, cause);
    }

    public UserSkipValidation(Throwable cause) {
        super(cause);
    }

    public UserSkipValidation(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
