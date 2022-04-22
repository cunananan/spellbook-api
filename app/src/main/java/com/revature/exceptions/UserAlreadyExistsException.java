package com.revature.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserAlreadyExistsException() {
		super();
	}

	public UserAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserAlreadyExistsException(String message, Throwable cause) {
		super(message, cause, false, false);
	}

	public UserAlreadyExistsException(String message) {
		super(message, null, false, false);
	}

	public UserAlreadyExistsException(Throwable cause) {
		super(null, cause, false, false);
	}
}
