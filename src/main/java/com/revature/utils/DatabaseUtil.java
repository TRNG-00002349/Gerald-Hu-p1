package com.revature.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class DatabaseUtil {

	/**
	 * Seeds the database from a prewritten SQL script.
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void initializeData() throws SQLException, Exception {
		BufferedReader scriptReader = new BufferedReader(
				new InputStreamReader(
						DatabaseUtil.class.getClassLoader().getResourceAsStream("script.sql")));

		String script = scriptReader.lines().collect(Collectors.joining("\n"));
		String[] statements = script.split(";");

		for(String sqlString : statements) {
			sqlString = sqlString.trim();
			try (
					Statement stmt = DataSource.getConnection().createStatement();
			) {
				stmt.execute(sqlString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
