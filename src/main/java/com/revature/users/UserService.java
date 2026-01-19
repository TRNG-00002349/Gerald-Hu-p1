package com.revature.users;

import com.revature.posts.Post;
import com.revature.posts.PostDao;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles user business logic
 */
public class UserService {

	private UserDao userDao;
	private PostDao postDao;

	public UserService() {

	}

	public UserService setUserDao(UserDao userDao) {
		this.userDao = userDao;
		return this;
	}

	public UserService setPostDao(PostDao postDao) {
		this.postDao = postDao;
		return this;
	}

	private void validateUserInfo(UserAuthDTO user) {
		if (user.getUsername() == null) {
			throw new UserValidationException("No username provided");
		}
		if (user.getUsername().length() <= 3) {
			throw new UserValidationException("Username must be longer than 3 characters");
		}
		if (!EmailValidator.getInstance().isValid(user.getEmail())) {
			throw new UserValidationException(String.format("Invalid email: %s", user.getEmail()));
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			throw new UserValidationException("Password can't be empty!");
		}
		// future work: additional validations (username has no underscores, no spaces, force lowercasing)
	}

	public User createUser(UserAuthDTO user) throws SQLException {
		validateUserInfo(user);
		String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(16));

		User u = new User();
		u.setHashedPassword(hashedPassword);
		u.setUsername(user.getUsername());
		u.setEmail(user.getEmail());
		return userDao.createUser(u);
	}

	public List<UserInfoDTO> getAllUsers() throws SQLException {
		return userDao.readAllUsers();
	}

	public User getUser(String id) throws SQLException {
		return userDao.readUser(id);
	}

	public User updateUser(String id, UserAuthDTO user) throws SQLException {
		validateUserInfo(user);
		String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(16));

		User u = new User();
		u.setUsername(user.getUsername());
		u.setEmail(user.getEmail());
		u.setHashedPassword(hashedPassword);
		return userDao.updateUser(id, u);
	}

	public void deleteUser(String id) throws SQLException {
		userDao.deleteUser(id);
	}

	public List<Post> getUserPosts(String s) throws SQLException {
		return PostDao.readPostsByUser(s);
	}
}
