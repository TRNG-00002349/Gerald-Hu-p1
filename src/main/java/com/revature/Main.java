package com.revature;

import com.revature.utils.DatabaseUtil;
import com.revature.utils.JavalinUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello world!");

		Properties props = new Properties();
		try {
			props.load(DatabaseUtil.class.getClassLoader().getResourceAsStream("application.properties"));
			if (props.getProperty("mode").equals("create")) {
				DatabaseUtil.initializeData();
			}
		} catch (IOException e) {
			System.out.println("Couldn't find application.properties");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JavalinUtil.startServer();
	}
}