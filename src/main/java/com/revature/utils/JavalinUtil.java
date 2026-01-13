package com.revature.utils;

import com.revature.health.HealthController;
import com.revature.users.UserController;
import com.revature.users.UserDao;
import com.revature.users.UserService;
import com.revature.users.UsernameValidationException;
import io.javalin.Javalin;


/**
 * Javalin API logic that doesn't belong in any controller.
 */
public class JavalinUtil {
	private static Javalin server;

	public static Javalin startServer() {
		try {
			server = Javalin.create();

			// Health
			HealthController healthController = new HealthController();
			server.get("/ping", healthController::ping);
			server.get("/header-test", healthController::headerTest);

			// User
			UserController userController = new UserController(
					new UserService(
							new UserDao()
					)
			);
			server.post("/users", userController::registerUser);
			server.exception(UsernameValidationException.class, userController::handleException);

			// Additional controllers ...

			return server.start(10001);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
