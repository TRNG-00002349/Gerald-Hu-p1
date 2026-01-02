package com.revature.utils;

import com.revature.health.HealthController;
import com.revature.users.UserController;
import com.revature.users.UserDao;
import com.revature.users.UserService;
import io.javalin.Javalin;

/**
 * Javalin API logic that doesn't belong in any controller.
 */
public class JavalinUtil {
		private static Javalin server;

		public static Javalin startServer() {
				try {
						server = Javalin.create();

						// User and other backbone controllers.
						HealthController healthController = new HealthController();
						UserController userController = new UserController(
								new UserService(
										new UserDao()
								)
						);

						// Content controllers with DAOs.

						server.get("/ping", healthController::ping);

						return server.start(10001);
				} catch (Exception e) {
						e.printStackTrace();
				}

				return null;
		}
}
