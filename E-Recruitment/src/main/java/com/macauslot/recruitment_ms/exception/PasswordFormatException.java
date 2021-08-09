package com.macauslot.recruitment_ms.exception;

public class PasswordFormatException extends ServiceException {


    private static final long serialVersionUID = -3547694323704132100L;

    public PasswordFormatException() {
    }

    public PasswordFormatException(String message) {
        super(message);
    }

    public PasswordFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordFormatException(Throwable cause) {
        super(cause);
    }

    public PasswordFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
