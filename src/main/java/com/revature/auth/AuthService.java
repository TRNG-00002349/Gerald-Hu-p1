package com.revature.auth;

import com.revature.users.UserAuthDTO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class AuthService {

	private AuthDao authDao;

	public AuthService(AuthDao authDao) {
		this.authDao = authDao;
	}

	public Boolean authenticateUser(UserAuthDTO userAuthDTO) throws SQLException {
		String storedPassword = authDao.getUserHashedPassword(userAuthDTO.getEmail(), userAuthDTO.getUsername());
		return BCrypt.checkpw(userAuthDTO.getPassword(), storedPassword);
	}
}
