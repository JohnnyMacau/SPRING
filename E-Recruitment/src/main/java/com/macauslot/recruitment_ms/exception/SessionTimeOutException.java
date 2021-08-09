package com.macauslot.recruitment_ms.exception;


public class SessionTimeOutException extends ServiceException{


	private static final long serialVersionUID = -4262816278582945063L;

	public SessionTimeOutException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
