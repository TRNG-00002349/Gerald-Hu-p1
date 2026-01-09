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

/**
 * Handles DB connectivity, e.g. connection strings.
 */
public class DatabaseUtil {

		public static Connection getConnection() throws SQLException, IOException {
				Properties props = new Properties();
				props.load(DatabaseUtil.class.getClassLoader().getResourceAsStream("application.properties"));

				try (Connection conn = DriverManager.getConnection(
						props.getProperty("url"),
						props.getProperty("username"),
						props.getProperty("password"));
				) {
						if(Objects.equals(props.getProperty("mode"), "create")) {
								initializeData();
						}
						return conn;
				} catch (Exception e) {
						e.printStackTrace();
				}
				return null;
		}

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

						Statement stmt = getConnection().createStatement();
						stmt.execute(sqlString);
				}
		}
}
