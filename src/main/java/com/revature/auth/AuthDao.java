package com.revature.auth;

import com.revature.users.UserNotFoundException;
import com.revature.utils.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDao {

	public AuthDao() {

	}

	public String getUserHashedPassword(String email, String username) throws SQLException {
		String GET_USER_PW_SQL = """
				SELECT hashed_password FROM users
				WHERE
				email = ? OR username = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(GET_USER_PW_SQL);
				) {
			pstmt.setString(1, email);
			pstmt.setString(2, username);
			pstmt.executeQuery();

			ResultSet rs = pstmt.getResultSet();
			if (!rs.isBeforeFirst()) {
				String emailOut = (email == null || email.isEmpty()) ? "none specified" : email;
				String usernameOut = (username == null || username.isEmpty()) ? "none specified" : username;
				throw new UserNotFoundException(
						String.format("username: %s; email: %s", usernameOut, emailOut)
				);
			}
			rs.next();
			return rs.getString("hashed_password");
		}
	}
}
