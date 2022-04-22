package com.revature.exceptions;

public class ItemNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ItemNotFoundException() {
		super();
	}

	public ItemNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ItemNotFoundException(String message, Throwable cause) {
		super(message, cause, false, false);
	}

	public ItemNotFoundException(String message) {
		super(message, null, false, false);
	}

	public ItemNotFoundException(Throwable cause) {
		super(null, cause, false, false);
	}
}
