package com.revature.exceptions;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause, false, false);
	}

	public UserNotFoundException(String message) {
		super(message, null, false, false);
	}

	public UserNotFoundException(Throwable cause) {
		super(null, cause, false, false);
	}
}
