package com.macauslot.recruitment_ms.exception;

/**
 *  token未匹配异常
 * @author Administrator
 *
 */
public class TokenNotMatchException extends ServiceException{


	private static final long serialVersionUID = -5598036510344095011L;

	public TokenNotMatchException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenNotMatchException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public TokenNotMatchException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TokenNotMatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TokenNotMatchException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
