package com.revature.auth;

import com.revature.users.UserAuthDTO;
import com.revature.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthController implements Controller {

	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		server.post("/login", this::loginUser);
	}

	@Override
	public void registerExceptions(Javalin server) {
		server.exception(AuthFailureException.class, this::handleNotAuthenticatedException);
	}

	private void loginUser(Context context) throws SQLException {
		UserAuthDTO u = context.bodyAsClass(UserAuthDTO.class);
		try {
			String token = authService.authenticateUser(u);
			Map<String, String> creds = new HashMap<>();
			creds.put("token", token);
			context.status(HttpStatus.OK)
					.json(creds);
		} catch (AuthFailureException e) {
			context.status(HttpStatus.UNAUTHORIZED).result(e.getMessage());
		}
	}

	public void handleNotAuthenticatedException(Exception e, Context context) {
		context.status(HttpStatus.UNAUTHORIZED).result(e.getMessage());
	}

}
