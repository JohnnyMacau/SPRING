package com.macauslot.recruitment_ms.exception;
/**
 * 违反了Unique约束的异常 （身份证被占用）
 * @author Administrator
 *
 */
public class DuplicateIdCardNumException extends ServiceException{

	private static final long serialVersionUID = 6056335784066377909L;



	public DuplicateIdCardNumException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DuplicateIdCardNumException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DuplicateIdCardNumException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DuplicateIdCardNumException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DuplicateIdCardNumException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
