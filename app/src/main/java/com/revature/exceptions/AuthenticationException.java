package com.revature.exceptions;

public class AuthenticationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause, false, false);
	}

	public AuthenticationException(String message) {
		super(message, null, false, false);
	}

	public AuthenticationException(Throwable cause) {
		super(null, cause, false, false);
	}
}
