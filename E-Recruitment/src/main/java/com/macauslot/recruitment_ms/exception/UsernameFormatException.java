package com.macauslot.recruitment_ms.exception;

public class UsernameFormatException extends ServiceException {


    private static final long serialVersionUID = 2689874564492347608L;

    public UsernameFormatException() {
    }

    public UsernameFormatException(String message) {
        super(message);
    }

    public UsernameFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameFormatException(Throwable cause) {
        super(cause);
    }

    public UsernameFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
