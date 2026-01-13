package com.revature.users;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles user business logic
 */
public class UserService {

	private final UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public List<User> getAllUsers() throws SQLException {
		return userDao.readAllUsers();
	}

	public User saveUser(User user) throws UsernameValidationException, SQLException {
		// example validation
		if (user.getUsername().length() <= 3) {
			throw new UsernameValidationException("username must be longer than 3 characters");
		}
		return userDao.createUser(user);
	}
}
