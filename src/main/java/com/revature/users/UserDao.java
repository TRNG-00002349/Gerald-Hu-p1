package com.revature.users;

import com.revature.utils.DataSource;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains JDBC code for persisting the User model.
 */
public class UserDao {

	public User createUser(User user) throws SQLException {
		String CREATE_USER_SQL = """
				INSERT INTO users (username, email, hashed_password, salt, created_at)
				VALUES (?, ?, ?, ?, ?)
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
		) {
			LocalDateTime now = LocalDateTime.now();
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getHashedPassword());
			pstmt.setString(4, user.getSalt());
			pstmt.setObject(5, now);
			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				user.setId(rs.getInt("id"));
				user.setCreatedAt(now);
			}
			return user;
		}
	}

	public List<UserInfoDTO> readAllUsers() throws SQLException {
		String READ_USERS_SQL = """
				SELECT * FROM users
				WHERE deleted = FALSE;
				""";
		ArrayList<UserInfoDTO> userList = new ArrayList<>();
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
				UserInfoDTO u = new UserInfoDTO(rs.getString("username"), rs.getInt("id"));
				userList.add(u);
			}
			return userList;
		}
	}

	public User readUser(String id) throws SQLException, UserNotFoundException {
		String READ_ONE_USER_SQL = """
				SELECT * FROM users WHERE id = ?
				AND deleted = FALSE
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
			rs.next();
			User u = new User();
			u.setId(rs.getInt("id"));
			u.setUsername(rs.getString("username"));
			u.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
			u.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
			return u;
		} catch (NumberFormatException e) {
			throw new UserNotFoundException(id);
		}
	}

	public User updateUser(String id, User user) throws SQLException, UserNotFoundException {
		String UPDATE_USER_SQL = """
				UPDATE USERS
				SET username = ?,
				updated_at = ?
				WHERE id = ?
				AND deleted = FALSE
				""";
		try(
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(UPDATE_USER_SQL);
		) {
			pstmt.setString(1, user.getUsername());
			pstmt.setObject(2, LocalDateTime.now());
			pstmt.setInt(3, Integer.parseInt(id));
			Integer updated = pstmt.executeUpdate();
			if (updated.equals(0)) {
				throw new UserNotFoundException(id);
			}
			user.setId(Integer.parseInt(id));
			user.setUpdatedAt(LocalDateTime.now());
			return user;
		} catch (NumberFormatException e) {
			throw new UserNotFoundException(id);
		}
	}

	public void deleteUser(String id) throws SQLException, UserNotFoundException {
		String DELETE_ONE_USER_SQL = """
				UPDATE USERS
				set DELETED = TRUE
				WHERE id = ?
				""";
		try(
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(DELETE_ONE_USER_SQL);
		) {
			pstmt.setInt(1, Integer.parseInt(id));
			Integer deleted = pstmt.executeUpdate();
			if (deleted.equals(0)) {
				throw new UserNotFoundException(id);
			}
		} catch (NumberFormatException e) {
			throw new UserNotFoundException(id);
		}
	}
}
