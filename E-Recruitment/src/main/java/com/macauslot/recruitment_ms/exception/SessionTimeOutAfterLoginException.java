package com.macauslot.recruitment_ms.exception;


public class SessionTimeOutAfterLoginException extends ServiceException{


	private static final long serialVersionUID = -3912909332914015122L;

	public SessionTimeOutAfterLoginException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutAfterLoginException(String message, Throwable cause, boolean enableSuppression,
                                             boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutAfterLoginException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutAfterLoginException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SessionTimeOutAfterLoginException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
