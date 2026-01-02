package com.revature.users;

import java.sql.SQLException;

/**
 * Handles user business logic
 */
public class UserService {

		private UserDao userDao;

		public UserService(UserDao userDao) {
				this.userDao = userDao;
		}

		public User saveUser (User user) throws UsernameValidationException, SQLException {
				// arbitrary validation
				if (user.getUsername().length() <= 3) {
						throw new UsernameValidationException("username must be longer than 3 characters");
				}

				return userDao.saveUser(user);
		}
}
