package com.revature.users;

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

	private final UserDao userDao;

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	private static byte[] salt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	private static String hashPassword(String password, byte[] salt) throws UserBadRequestException {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return new String(factory.generateSecret(spec).getEncoded());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			throw new UserBadRequestException("Couldn't accept password");
		}
	}

	public User saveUser(UserAuthDTO user) throws UserBadRequestException, SQLException {
		byte[] salt = salt(); // We must reroll salt every time, else we get null bytes in salt

		if (user.getUsername() == null) {
			throw new UserBadRequestException("No username provided");
		}
		if (user.getUsername().length() <= 3) {
			throw new UserBadRequestException("Username must be longer than 3 characters");
		}
		if (!EmailValidator.getInstance().isValid(user.getEmail())) {
			throw new UserBadRequestException(String.format("Invalid email: %s", user.getEmail()));
		}
		// TODO: additional validations (username has no underscores, no spaces, force lowercasing)
		String hashedPassword = hashPassword(user.getPassword(), salt);

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

	public User updateUser(String id, User user) throws SQLException {
		return userDao.updateUser(id, user);
	}

	public void deleteUser(String id) throws SQLException {
		userDao.deleteUser(id);
	}
}
