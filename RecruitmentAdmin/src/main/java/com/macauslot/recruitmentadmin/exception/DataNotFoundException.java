package com.macauslot.recruitmentadmin.exception;

/**
 *  token未找到异常
 * @author Administrator
 *
 */
public class DataNotFoundException extends ServiceException{


	private static final long serialVersionUID = 1729019735816941092L;

	public DataNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DataNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DataNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DataNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
