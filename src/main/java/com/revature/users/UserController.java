package com.revature.users;

import com.revature.posts.Post;
import com.revature.utils.BadRequestException;
import com.revature.utils.Controller;
import com.revature.utils.ControllerUtil;
import com.revature.utils.ServiceUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.eclipse.jetty.http.HttpMethod;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles HTTP request/response logic. Maybe exception handling.
 */
public class UserController implements Controller {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		server.post("/users", this::registerUser);
		server.get("/users", this::showAllUsers);
		server.get("/users/{user-id}", this::showOneUser);
		server.get("/users/{user-id}/posts", this::showOneUsersPosts);
		server.put("/users/{user-id}", this::updateUser);
		server.delete("/users/{user-id}", this::deleteUser);
		server.before("/users/{user-id}*", this::validateUserId);
	}

	@Override
	public void registerExceptions(Javalin server) {
		server.exception(UserNotFoundException.class, this::handleUserNotFoundException);
		server.exception(UserValidationException.class, this::handleUserValidationException);
		server.exception(UserIsDeletedException.class, this::handleUserIsDeletedException);
	}

	public void validateUserId(Context context) {
		String userId = context.pathParam("user-id");
		ServiceUtil.validateId(userId);
	}

	public void registerUser(Context context) throws SQLException {
		UserAuthDTO user;
		try {
			user = context.bodyAsClass(UserAuthDTO.class);
		} catch (Exception e) {
			throw new BadRequestException(String.format("Couldn't parse %s", context.body()));
		}

		// Send user to service layer (and then to DAO layer); get result back
		User persistedUser = userService.createUser(user);

		// Prep the response
		context.status(HttpStatus.CREATED);
		context.json(persistedUser);
		// TODO: write tests for empty body, body of {}, malformed body, proper body
	}

	public void showAllUsers(Context context) throws SQLException {
		List<UserInfoDTO> users =  userService.getAllUsers();
		context.status(HttpStatus.OK);
		context.json(users);
	}

	public void showOneUser(Context context) throws SQLException {
		User user = userService.getUser(context.pathParam("user-id"));
		context.status(HttpStatus.OK);
		context.json(user);
		// TODO: Write tests for: valid user id, stringy user ID, invalid user ID
	}

	// get user by username

	public void showOneUsersPosts(Context context) throws SQLException {
		User user = userService.getUser(context.pathParam("user-id"));
		List<Post> userPosts = userService.getUserPosts(context.pathParam("user-id"));
		user.setUserPosts(userPosts);

		context.status(HttpStatus.OK).json(user);
	}

	// We allow changing username, password, email; these implicitly change updated_at
	public void updateUser(Context context) throws SQLException {
		UserAuthDTO user;
		try {
			user = context.bodyAsClass(UserAuthDTO.class);
		} catch (Exception e) {
			throw new BadRequestException(String.format("Couldn't parse %s", context.body()));
		}

		User persistedUser = userService.updateUser(context.pathParam("user-id"), user);
		context.status(HttpStatus.OK).json(persistedUser);
	}

	public void deleteUser(Context context) throws SQLException {
		userService.deleteUser(context.pathParam("user-id"));
		context.status(HttpStatus.NO_CONTENT);
	}

	public void handleUserNotFoundException(Exception e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("User not found: %s", e.getMessage()));
	}

	private void handleUserValidationException(UserValidationException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("User validation error: %s", e.getMessage()));
	}

	private void handleUserIsDeletedException(Exception e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("User #%s is deactivated and can't do this action", e.getMessage()));
	}

}
