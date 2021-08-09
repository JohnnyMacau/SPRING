package com.macauslot.recruitmentadmin.exception;

public class UserLoginTimeoutException extends ServiceException {

    private static final long serialVersionUID = -6263418531881813294L;

    public UserLoginTimeoutException() {
    }

    public UserLoginTimeoutException(String message) {
        super(message);
    }

    public UserLoginTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLoginTimeoutException(Throwable cause) {
        super(cause);
    }

    public UserLoginTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
