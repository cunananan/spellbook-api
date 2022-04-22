package com.revature.exceptions;

public class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ValidationException() {
		super();
	}

	public ValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause, false, false);
	}

	public ValidationException(String message) {
		super(message, null, false, false);
	}

	public ValidationException(Throwable cause) {
		super(null, cause, false, false);
	}
}
