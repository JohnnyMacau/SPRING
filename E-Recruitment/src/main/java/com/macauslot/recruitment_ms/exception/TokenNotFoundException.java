package com.macauslot.recruitment_ms.exception;

/**
 *  token未找到异常
 * @author Administrator
 *
 */
public class TokenNotFoundException extends ServiceException{


	private static final long serialVersionUID = -7983991919536293423L;

	public TokenNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public TokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TokenNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TokenNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
