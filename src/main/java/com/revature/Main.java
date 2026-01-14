package com.revature;

import com.revature.health.HealthController;
import com.revature.posts.PostController;
import com.revature.posts.PostDao;
import com.revature.posts.PostService;
import com.revature.users.UserController;
import com.revature.users.UserDao;
import com.revature.users.UserService;
import com.revature.utils.DatabaseUtil;
import com.revature.utils.WebServer;

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

			HealthController healthController = new HealthController();
			UserController userController = new UserController(
					new UserService(
							new UserDao()
					)
			);
			PostController postController = new PostController(
					new PostService(
							new PostDao()
					)
			);

			WebServer server = WebServer.builder()
					.port(10001)
					.addController(healthController)
					.addController(userController)
					.build();
			server.start();
		} catch (IOException e) {
			System.out.println("Couldn't find application.properties");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}