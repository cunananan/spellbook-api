package com.revature.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.exceptions.UserAlreadyExistsException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.exceptions.ValidationException;
import com.revature.models.User;
import com.revature.models.User.UserRole;
import com.revature.models.UserDto;
import com.revature.repositories.UserRepository;
import com.revature.util.ValidationUtil;

import io.micrometer.core.annotation.Timed;

@Service
public class UserService {
	
	private UserRepository ur;
	private PasswordEncoder pe;
	
	@Autowired
	public UserService(UserRepository ur, PasswordEncoder pe) {
		this.ur = ur;
		this.pe = pe;
	}
	
	@Timed(value="user.time")
	public List<UserDto> getUsers() {
		List<User> users = ur.findAllByOrderByIdAsc();
		if (users.isEmpty()) {
			throw new UserNotFoundException("No users were found");
		}
		// Convert List<User> to List<UserDto>
		return users.stream().map(UserDto::new).collect(Collectors.toList());
	}
	
	@Timed(value="user.time")
	@Transactional
	public List<UserDto> getUsersByQuery(String search, UserRole role) {
		List<User> users = (StringUtils.isBlank(search))
		                       ? ur.findAllByOrderByIdAsc()
		                       : findBySearchUsernameAndEmail(search);
		Stream<User> us = users.stream();
		if (role != null && role != UserRole.NOT_SET)
			us = us.filter(u -> u.getRole() == role);
		
		List<UserDto> usersDto = us.map(UserDto::new).collect(Collectors.toList());
		if (usersDto.isEmpty()) {
			throw new UserNotFoundException("No users were found");
		}
		// Convert List<User> to List<UserDto>
		return usersDto;
	}
	
	@Timed(value="user.time")
	public UserDto getUserById(int id) {
		User user = ur.findById(id).orElseThrow(() ->
		                               new UserNotFoundException("User not found") );
		return new UserDto(user);
	}
	
	@Timed(value="user.time")
	@Transactional
	public UserDto addUser(User newUser) {
		if (newUser == null) {
			throw new ValidationException("User was not provided");
		}
		if (!newUser.validate()) {
			throw new ValidationException("User has invalid fields");
		}
		if (ur.existsUserByUsername(newUser.getUsername()) || ur.existsUserByEmail(newUser.getEmail()))
		{
			throw new UserAlreadyExistsException("User with that username or email already exists");
		}
		newUser.setId(0);
		newUser.setPassword(pe.encode(newUser.getPassword()));
		return new UserDto(ur.save(newUser));
	}
	
	@Timed(value="user.time")
	@Transactional
	public String updateUser(int userId, String newPassword, UserRole newRole) {
		String message = "";
		int numUpdates = 0;
		boolean newPassInvalid = false;
		if (newRole != null && newRole != UserRole.NOT_SET) {
			updateUserRole(userId, newRole);
			message += "Role ";
			numUpdates++;
		}
		if (newPassword != null && !newPassword.equals("")) {
			if (ValidationUtil.validatePassword(newPassword)) {
				updateUserPassword(userId, newPassword);
				message += (numUpdates == 0) ? "Password " : "and password ";
				numUpdates++;
			} else {
				newPassInvalid = true;
			}
		}
		message += (numUpdates == 0) ? "No fields " : "";
		message += (numUpdates == 1) ? "was updated" : "were updated";
		message += (newPassInvalid) ? "; new password was invalid" : "";
		if (numUpdates == 0) {
			throw new ValidationException(message); 
		}
		return message;
	}
	
	@Timed(value="user.time")
	@Transactional
	public UserDto updateUserPassword(int userId, String newPassword) {
		User user = ur.findById(userId).orElseThrow(() ->
                                           new UserNotFoundException("User not found") );
		
		if (!ValidationUtil.validatePassword(newPassword)) {
			throw new ValidationException("New password is invalid");
		}

		user.setPassword(pe.encode(newPassword));
		return new UserDto(ur.save(user));
	}
	
	@Timed(value="user.time")
	@Transactional
	public UserDto updateUserRole(int userId, UserRole newRole) throws UserNotFoundException, ValidationException {
		User user = ur.findById(userId).orElseThrow(() ->
                                           new UserNotFoundException("User not found") );
		if (newRole == null || newRole == UserRole.NOT_SET) {
			throw new ValidationException("Invalid role was provided");
		}
		user.setRole(newRole);
		return new UserDto(ur.save(user));
	}
	
	@Timed(value="user.time")
	@Transactional
	public UserDto deleteUser(int id) {
		User removedUser = ur.findById(id).orElseThrow(() -> 
		                                      new UserNotFoundException("User not found") );
		ur.deleteById(id);
		return new UserDto(removedUser);
	}
	 
	// This is slow and I kind of hate it
	private List<User> findBySearchUsernameAndEmail(String search) {
		List<User> userList = ur.findByUsernameContainingIgnoreCaseOrderByIdAsc(search);
		List<User> mailList = ur.findByEmailContainingIgnoreCaseOrderByIdAsc(search);
		// The returned items should be in the following order:
		// - items that match by their username, by id asc
		// - items that match by their email, by id asc
		for (int i = 0; i < mailList.size(); i++) {
			if (!userList.contains(mailList.get(i))) {
				userList.add(mailList.get(i));
			}
		}
		return userList;
	}
}





