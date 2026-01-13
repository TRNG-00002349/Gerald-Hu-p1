package com.revature.users;

import com.revature.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains JDBC code for persisting the User model.
 */
public class UserDao {

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

	public User readUser(String id) throws SQLException {
		String READ_ONE_USER_SQL = """
			SELECT * FROM users WHERE id = ?
			""";
		try(
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(READ_ONE_USER_SQL);
		)	{
			pstmt.setInt(1, Integer.parseInt(id));
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			if (!rs.isBeforeFirst()) {
				throw new UserNotFoundException(id);
			}
			rs.first();
			User u = new User();
			u.setId(rs.getInt("id"));
			u.setUsername((rs.getString("username")));
			return u;
		} catch (NumberFormatException e) {
			throw new UserNotFoundException(id);
		}
		// TODO: We also want to get a list of posts belonging to this user. (Either the entire post or just the ID... probably entire post.)
	}
}
