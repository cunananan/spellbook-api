package com.revature.util;

import java.util.regex.Pattern;

public class ValidationUtil {
	
	/*
	 * Username consists of alphanumeric characters and dots, underscores, 
	 * and hyphens and must be between 5 and 255 characters in length. 
	 */
	private static final String USERNAME_PATTERN = "([a-zA-Z0-9]|[._-]){5,255}$";
	/*
	 * Regex provided by RFC 5322 
	 */
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
//	private static final String PASSWORD_PATTERN;
	
	public static boolean validateUsername(String value) {
		return Pattern.compile(USERNAME_PATTERN).matcher(value).matches();
	}
	
	public static boolean validateEmail(String value) {
		return Pattern.compile(EMAIL_PATTERN).matcher(value).matches();
	}
	
	public static boolean validatePassword(String value) {
		return Pattern.compile(USERNAME_PATTERN).matcher(value).matches();
	}
}
