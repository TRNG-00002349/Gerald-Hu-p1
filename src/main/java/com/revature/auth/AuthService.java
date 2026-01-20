package com.revature.auth;

import com.revature.users.User;
import com.revature.users.UserAuthDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class AuthService {

	private AuthDao authDao;

	public AuthService(AuthDao authDao) {
		this.authDao = authDao;
	}

	public String authenticateUser(UserAuthDTO userAuthDTO) throws SQLException {
		User storedUser = authDao.getUserIdAndHashedPassword(userAuthDTO.getEmail(), userAuthDTO.getUsername());
		Integer storedId = storedUser.getId();
		String storedPassword = storedUser.getHashedPassword();

		if(BCrypt.checkpw(userAuthDTO.getPassword(), storedPassword)) {
			return Jwts.builder()
					.claim("userId", storedId)
					.compact();
		} else {
			throw new NotAuthenticatedException("Invalid login credentials");
		}
	}

}
