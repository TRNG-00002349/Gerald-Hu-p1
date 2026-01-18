package com.revature.users;

import com.revature.posts.Post;
import com.revature.posts.PostDao;
import com.revature.utils.BadRequestException;
import org.apache.commons.validator.routines.EmailValidator;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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

	private static byte[] salt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	// TODO: update this to use bcrypt instead. jesus
	private static String hashPassword(String password, byte[] salt) throws BadRequestException {
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return new String(factory.generateSecret(spec).getEncoded());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NullPointerException e) {
			e.printStackTrace();
			throw new BadRequestException("Couldn't accept password");
		}
	}

	private void validateUser(UserAuthDTO user) throws UserValidationException {
		if (user.getUsername() == null) {
			throw new UserValidationException("No username provided");
		}
		if (user.getUsername().length() <= 3) {
			throw new UserValidationException("Username must be longer than 3 characters");
		}
		if (!EmailValidator.getInstance().isValid(user.getEmail())) {
			throw new UserValidationException(String.format("Invalid email: %s", user.getEmail()));
		}
		// TODO: additional validations (username has no underscores, no spaces, force lowercasing)
	}

	public User saveUser(UserAuthDTO user) throws SQLException {
		byte[] salt = salt(); // We must reroll salt every time, else we get null bytes in salt
		String hashedPassword = hashPassword(user.getPassword(), salt);

		validateUser(user);

		User u = new User();
		u.setHashedPassword(hashedPassword);
		u.setUsername(user.getUsername());
		u.setEmail(user.getEmail());
		u.setSalt(new String(salt));
		return userDao.createUser(u);
	}

	public List<UserInfoDTO> getAllUsers() throws SQLException {
		return userDao.readAllUsers();
	}

	public User getUser(String id) throws SQLException {
		return userDao.readUser(id);
	}

	public User updateUser(String id, UserAuthDTO user) throws SQLException {
		byte[] salt = salt();
		String hashedPassword = hashPassword(user.getPassword(), salt);
		validateUser(user);

		User u = new User();
		u.setUsername(user.getUsername());
		u.setEmail(user.getEmail());
		u.setHashedPassword(hashedPassword);
		u.setSalt(new String(salt));
		return userDao.updateUser(id, u);
	}

	public void deleteUser(String id) throws SQLException {
		userDao.deleteUser(id);
	}

	public List<Post> getUserPosts(String s) throws SQLException {
		return postDao.readPostsByUser(s);
	}
}
