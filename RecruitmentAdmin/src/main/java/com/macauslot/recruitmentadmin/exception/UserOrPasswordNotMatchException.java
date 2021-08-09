package com.macauslot.recruitmentadmin.exception;

/**
 * 用户或者密码错误异常
 * @author Administrator
 *
 */
public class UserOrPasswordNotMatchException extends ServiceException{

	private static final long serialVersionUID = 3027567043462815898L;

	public UserOrPasswordNotMatchException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserOrPasswordNotMatchException(String message, Throwable cause, boolean enableSuppression,
                                           boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserOrPasswordNotMatchException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserOrPasswordNotMatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserOrPasswordNotMatchException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
