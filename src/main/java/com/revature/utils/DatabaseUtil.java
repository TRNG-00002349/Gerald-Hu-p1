package com.revature.utils;

import com.revature.users.UserIsDeletedException;
import com.revature.users.UserNotFoundException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseUtil {

	/**
	 * Seeds the database from a prewritten SQL script.
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void initializeData() throws SQLException {
		BufferedReader scriptReader = new BufferedReader(
				new InputStreamReader(
						DatabaseUtil.class.getClassLoader().getResourceAsStream("script.sql")));

		String script = scriptReader.lines().collect(Collectors.joining("\n"));
		String[] statements = script.split(";");

		try (
				Statement stmt = DataSource.getConnection().createStatement()
				) {
			for(String sqlString : statements) {
				sqlString = sqlString.trim();
				stmt.execute(sqlString);
			}
		}
	}


	public static void checkIfAuthorDeleted(Integer authorId) throws SQLException, UserNotFoundException, UserIsDeletedException {
		String CHECK_IF_AUTHOR_DELETED = """
				SELECT deleted FROM users
				WHERE
				ID = ?
				""";
		try(
				var conn = DataSource.getConnection();
				var checkAuthor = conn.prepareStatement(CHECK_IF_AUTHOR_DELETED);
		) {

			if (authorId == null) {
				throw new UserNotFoundException("No user specified");
			}
			ResultSet rs;
			checkAuthor.setInt(1, authorId);
			checkAuthor.executeQuery();
			rs = checkAuthor.getResultSet();
			if (!rs.isBeforeFirst()) {
				throw new UserNotFoundException(authorId.toString());
			}
			rs.next();
			if (rs.getBoolean("deleted")) {
				throw new UserIsDeletedException(authorId.toString());
			}
		}
	}

}
