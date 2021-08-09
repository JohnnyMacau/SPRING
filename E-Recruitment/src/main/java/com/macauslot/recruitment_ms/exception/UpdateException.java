package com.macauslot.recruitment_ms.exception;

/**
 *更改数据异常
 * @author Administrator
 *
 */
public class UpdateException extends ServiceException{

	private static final long serialVersionUID = 4080686327652491754L;

	public UpdateException() {
		super();
	}

	public UpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateException(String message) {
		super(message);
	}

	public UpdateException(Throwable cause) {
		super(cause);
	}


}
