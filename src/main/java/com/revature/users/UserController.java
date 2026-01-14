package com.revature.users;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
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
		this.userService = userService;
	}

	public void registerUser(Context ctx) throws SQLException, UserBadRequestException {
		// TODO: test for empty body, body of {}, malformed body, proper body
		UserAuthDTO user;
		try {
			user = ctx.bodyAsClass(UserAuthDTO.class);
		} catch (Exception e) {
			throw new UserBadRequestException(String.format("Could not parse %s", ctx.body()));
		}

		// Send user to service layer (and then to DAO layer); get result back
		User persistedUser = userService.saveUser(user);

		// Prep the response
		ctx.status(HttpStatus.CREATED);
		ctx.json(persistedUser);
	}

	public void showAllUsers(Context ctx) throws SQLException {
		List<UserInfoDTO> users =  userService.getAllUsers();
		ctx.status(HttpStatus.OK);
		ctx.json(users);
	}

	public void showOneUser(Context ctx) throws SQLException {
		User user = userService.getUser(ctx.pathParam("user-id"));
		ctx.status(HttpStatus.OK);
		ctx.json(user);
		// TODO: Write tests for: valid user id, stringy user ID, invalid user ID
	}

	// TODO: get user by username

	// We allow changing username, password, email; these implicitly change updated_at

	public void handleUserBadRequestException(Exception e, Context ctx) {
		ctx.status(HttpStatus.BAD_REQUEST).result(String.format("Bad input to /users/: %s", e.getMessage()));
	}

	public void handleUsernameException(Exception e, Context ctx) {
		ctx.status(HttpStatus.BAD_REQUEST).result(String.format("Username error: %s", e.getMessage()));
	}

	public void handleUserNotFoundException(Exception e, Context ctx) {
		ctx.status(HttpStatus.BAD_REQUEST).result(String.format("User not found: %s", e.getMessage()));
	}

}
