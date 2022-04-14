package com.revature.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidationUtilTests {

	@Test
	void testValidateUsername() {
		assertTrue(ValidationUtil.validateUsername("admin"));
		assertTrue(ValidationUtil.validateUsername("test_user"));
		assertTrue(ValidationUtil.validateUsername("test-user"));
		assertTrue(ValidationUtil.validateUsername("test.user"));
		assertTrue(ValidationUtil.validateUsername("test1"));
		assertFalse(ValidationUtil.validateUsername("admi"));
		assertFalse(ValidationUtil.validateUsername("admin!"));
	}
	
	@Test
	void testValidateEmail() {
		assertTrue(ValidationUtil.validateEmail("admin@test.com"));
		assertFalse(ValidationUtil.validateEmail("admin"));
		assertFalse(ValidationUtil.validateEmail("@com"));
	}
	
	@Test
	void testValidatePassword() {
		assertTrue(ValidationUtil.validatePassword("admin123"));
		assertTrue(ValidationUtil.validatePassword("ADMIN123"));
		assertTrue(ValidationUtil.validatePassword("testuser!"));
		assertFalse(ValidationUtil.validatePassword("admin"));
		assertFalse(ValidationUtil.validatePassword("admin!"));
	}
}
