package com.macauslot.recruitment_ms.exception;

public class EntityCastException extends RuntimeException {

    private static final long serialVersionUID = -6579993714313281294L;

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
