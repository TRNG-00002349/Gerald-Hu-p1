package com.revature.utils;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * TBD: connection pool based on Aliaksandr's code, using Hikari
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
