package com.macauslot.recruitmentadmin.exception;

public class UserDataMissingException extends ServiceException {

    private static final long serialVersionUID = -1738505270727596489L;

    public UserDataMissingException() {
    }

    public UserDataMissingException(String message) {
        super(message);
    }

    public UserDataMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDataMissingException(Throwable cause) {
        super(cause);
    }

    public UserDataMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
