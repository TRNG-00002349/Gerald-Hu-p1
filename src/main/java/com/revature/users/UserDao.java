package com.revature.users;

import com.revature.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains JDBC code for persisting the User model.
 */
public class UserDao {

	private static final String READ_ONE_USER_SQL = """
			SELECT * FROM users WHERE id = ?
			""";

	public User createUser(User user) throws SQLException {
		String CREATE_USER_SQL = """
			INSERT INTO users (username, email, password_hash)
			VALUES (?, ?, ?)
			""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
		) {
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getHashedPassword());
			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				user.setId(rs.getInt("id"));
			}
			return user;
		}
	}

	public List<User> readAllUsers() throws SQLException {
		String READ_USERS_SQL = """
			SELECT * FROM users
			""";
		ArrayList<User> userList = new ArrayList<>();
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(READ_USERS_SQL);
		) {
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			if (rs == null) {
				return userList;
			}
			while (rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setUsername((rs.getString("username")));
				userList.add(u);
			}
			return userList;
		}
	}
}
