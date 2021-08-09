package com.macauslot.recruitment_ms.exception;
/**
 * 违反了Unique约束的异常 （用户名被占用）
 * @author Administrator
 *
 */
public class DuplicateUserNameException extends ServiceException{


	private static final long serialVersionUID = 5209316078583604788L;

	public DuplicateUserNameException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DuplicateUserNameException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DuplicateUserNameException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DuplicateUserNameException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DuplicateUserNameException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
