package com.revature.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
}
