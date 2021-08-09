package com.macauslot.recruitmentadmin.exception;

public class EntityCastException extends ServiceException {


    private static final long serialVersionUID = 7287436379255689425L;

    public EntityCastException() {
        super();
    }

    public EntityCastException(String message) {
        super(message);
    }

    public EntityCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityCastException(Throwable cause) {
        super(cause);
    }

    public EntityCastException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
