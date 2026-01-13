package com.revature.users;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles HTTP request/response logic. Maybe exception handling.
 */
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		// An example of constructor DI
		this.userService = userService;
	}

	public void showAllUsers(Context ctx) throws SQLException {
		List<User> users =  userService.getAllUsers();

		ctx.status(HttpStatus.OK);
		ctx.json(users);
	}

	public void registerUser(Context ctx) throws UsernameValidationException, SQLException {
		// Get user from request body
		User user = ctx.bodyAsClass(User.class); // Javalin -> Jackson will build a User automatically

		// Send user to service layer (and then to DAO layer); get result back
		User persistedUser = userService.saveUser(user);

		// Prep the response
		ctx.status(HttpStatus.CREATED);
		ctx.json(persistedUser);
	}

	public void handleException(Exception e, Context ctx) {
		ctx.status(HttpStatus.BAD_REQUEST).result("username error");
	}
}
