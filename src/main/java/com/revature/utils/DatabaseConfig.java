package com.revature.utils;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Instead of making a single connection, Alex made a connection pool via Hikari.
 * I'm not using Alex's code at the moment, but it can be implemented in future upgrades.
 */
public class DatabaseConfig {
		private static final HikariDataSource dataSource;

		static {
				var config = new HikariConfig();
				config.setJdbcUrl("jdbc:postgresql://trng-00002349.c92agkkieazq.us-east-2.rds.amazonaws.com:5432/alex_k");
				config.setUsername("postgres");
				config.setPassword("");
				dataSource = new HikariDataSource(config);
		}

		public static DataSource getDataSource() {
				return dataSource;
		}
}
