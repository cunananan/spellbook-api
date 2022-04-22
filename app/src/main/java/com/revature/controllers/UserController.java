package com.revature.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.AccessDeniedException;
import com.revature.exceptions.ValidationException;
import com.revature.models.User;
import com.revature.models.User.UserRole;
import com.revature.models.UserDto;
import com.revature.services.AuthService;
import com.revature.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private UserService us;
	private AuthService as;
	private static final String ENDPOINT = "/users";
	
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	public UserController(UserService us, AuthService as) {
		super();
		this.us = us;
		this.as = as;
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> getUsers(@RequestHeader(name="Authorization", required=false) String token,
			                                      @RequestParam(name="search", required=false) String searchStr,
	                                              @RequestParam(name="role", required=false) String roleStr)
	{
		setMDC(ENDPOINT, "GET", token);
		
		List<UserDto> users;
		// If user is not an admin, only show self
		if (!as.authorizeRole(token, UserRole.ADMIN)) {
			users = new ArrayList<>();
			users.add(us.getUserById(as.extractIdFromToken(token)));
			LOG.info("{} was returned", users);
			return new ResponseEntity<>(users, HttpStatus.OK);
		}
		UserRole role = null;
		boolean queriesAllNull = true;
		
		if (!StringUtils.isBlank(searchStr)) {
			queriesAllNull = false;
		}
		role = userRoleFromString(roleStr);
		if (role != UserRole.NOT_SET) {
			queriesAllNull = false; 
		}
		// May throw runtime exceptions; handled by GlobalExceptionHandler
		users = (queriesAllNull) ? us.getUsers() : us.getUsersByQuery(searchStr, role);
		LOG.info("{} users were returned", users.size());
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@RequestHeader(name="Authorization", required=false) String token,
	                                           @PathVariable("id") int id)
	{
		setMDC(ENDPOINT + "/" + id, "GET", token);
		// Only admins and the id's user can view this
		if (!as.authorizeRole(token, UserRole.ADMIN) && !as.authorizeUser(token, id)) {
			LOG.warn("Failed to view User #{}", id);
			throw new AccessDeniedException("Not authorized to view user");
		}
		UserDto user = us.getUserById(id);
		LOG.info("{} was returned", user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<String> createUser(@RequestHeader(name="Authorization", required=false) String token,
	                                         @RequestBody User newUser)
	{
		setMDC(ENDPOINT, "POST", token);
		// If user is not an admin, can only add a user with the USER role
		if (StringUtils.isBlank(token) || !as.authorizeRole(token, UserRole.ADMIN)) {
			LOG.debug("New user being created by non-admin; role reduced to USER from {}", newUser.getRole());
			newUser.setRole(UserRole.USER);
		}
		UserDto user = us.addUser(newUser);
		LOG.info("{} was created", user);
		return new ResponseEntity<>("New user \"" + user.username + "\" was added at index " + user.id, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateUser(@RequestHeader(name="Authorization", required=false) String token,
	                                         @PathVariable("id") int id,
	                                         @RequestParam(name="currentPassword", required=false) String currPass,
	                                         @RequestParam(name="newPassword", required=false) String newPass,
	                                         @RequestParam(name="newRole", required=false) String newRole)
	{
		setMDC(ENDPOINT + "/" + id, "PUT", token);
		
		// Admin can change user role and password without confirmation
		if (as.authorizeRole(token, UserRole.ADMIN)) {
			String message = us.updateUser(id, newPass, userRoleFromString(newRole));
			LOG.info("{} for User #{}", message, id);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		}
		// User can change own password, but must provide current password first
		else if (as.authorizeUser(token, id)) {
			if (!as.verifyPassword(token, currPass)) {
				throw new ValidationException("Current password was incorrect");
			}
			us.updateUserPassword(id, newPass);
			LOG.info("Password was updated for User #{}", id);
			return new ResponseEntity<>("Password was updated", HttpStatus.CREATED);
		}
		else {
			LOG.warn("Failed to modify User #{}", id);
			throw new AccessDeniedException("Not authorized to modify user");
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@RequestHeader(name="Authorization", required=false) String token,
	                                         @PathVariable("id") int id)
	{
		setMDC(ENDPOINT + "/" + id, "DELETE", token);
		
		if (!as.authorizeRole(token, UserRole.ADMIN)) {
			LOG.warn("Failed to delete User #{}", id);
			throw new AccessDeniedException("Not authorized to delete users");
		}
		UserDto user = us.deleteUser(id);
		LOG.info("{} was deleted", user);
		return new ResponseEntity<>("Deleted user \"" + user.username + "\"", HttpStatus.OK);
	}
	
	private void setMDC(String endpoint, String method, String token) {
		MDC.clear();
		MDC.put("endpoint", endpoint);
		MDC.put("method", method);
		MDC.put("user", as.extractUsernameFromToken(token));
		MDC.put("requestId", UUID.randomUUID());
	}
	
	private UserRole userRoleFromString(String roleStr) {
		if (StringUtils.isBlank(roleStr)) {
			return UserRole.NOT_SET;
		}
		try {
			return UserRole.valueOf(roleStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			LOG.debug("UserController.userRoleFromString() is catching exception: {}", e.getMessage());
			LOG.warn("User is passing bad argument through a UserRole param: {}", roleStr);
			return UserRole.NOT_SET;
		}
	}
}





