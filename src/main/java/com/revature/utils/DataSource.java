package com.revature.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Instead of making a single connection, Alex made a connection pool via Hikari.
 */
public class DataSource {
	private static final HikariDataSource dataSource;

	static {
		var config = new HikariConfig();
		Properties props = new Properties();
		try {
			props.load(DatabaseUtil.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			System.out.println("Couldn't find application.properties");
		}
		config.setJdbcUrl(props.getProperty("database_url"));
		config.setUsername(props.getProperty("username"));
		config.setPassword(props.getProperty("password"));
		dataSource = new HikariDataSource(config);
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
