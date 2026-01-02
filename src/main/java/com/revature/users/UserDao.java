package com.revature.users;

import com.revature.utils.DatabaseUtil;

import java.sql.*;

/**
 * Will contain JDBC code for persisting the User model
 */
public class UserDao {
		Connection conn;

		{
				try {
						conn = DatabaseUtil.getConnection();
				} catch (SQLException e) {
						throw new RuntimeException(e);
				}
		}

		public User saveUser(User user) throws SQLException {
				String sql = "INSERT INTO users (username, email, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)";

				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, user.getUsername());
				pstmt.setString(2, user.getEmail());
				pstmt.setString(3, user.getPassword());
				pstmt.setString(4, user.getFirstName());
				pstmt.setString(5, user.getLastName());
				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();
				if(rs.next()) {
						user.setId(rs.getInt("id"));
				}

				return user;
		}
}
