package com.revature.utils;

import com.revature.health.HealthController;
import com.revature.users.*;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.sql.SQLException;


/**
 * Javalin API logic that doesn't belong in any controller.
 */
public class JavalinUtil {
	private static Javalin server;

	public static Javalin startServer() {
		try {
			server = Javalin.create();

			// Health and boilerplate
			HealthController healthController = new HealthController();
			server.get("/ping", healthController::ping);
			server.get("/header-test", healthController::headerTest);
			server.exception(SQLException.class, ControllerUtil::handleDBException);

			// User
			UserController userController = new UserController(
					new UserService(
							new UserDao()
					)
			);

			server.get("/users", userController::showAllUsers);
			server.get("/users/{user-id}", userController::showOneUser);
			server.post("/users", userController::registerUser);
			server.exception(UsernameValidationException.class, userController::handleUsernameException);
			server.exception(UserNotFoundException.class, userController::handleUserNotFoundException);

			// Additional controllers ...

			return server.start(10001);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
