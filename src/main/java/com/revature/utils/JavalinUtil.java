package com.revature.utils;

import com.revature.health.HealthController;
import com.revature.users.*;
import io.javalin.Javalin;

import java.sql.SQLException;


/**
 * Javalin API logic that doesn't belong in any controller.
 */
public class JavalinUtil {
	private static Javalin server;

	public static Javalin startServer() {
		try {
			server = Javalin.create();


			// User
			UserController userController = new UserController(
					new UserService(
							new UserDao()
					)
			);




			// Additional controllers ...

			return server.start(10001);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
