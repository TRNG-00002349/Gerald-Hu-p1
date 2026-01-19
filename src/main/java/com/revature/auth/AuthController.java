package com.revature.auth;

import com.revature.users.UserAuthDTO;
import com.revature.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController implements Controller {

	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		server.before(this::checkAuthBeforeAction);

		server.post("/login", this::loginUser);
	}

	@Override
	public void registerExceptions(Javalin server) {
	}

	private void loginUser(Context context) throws SQLException {
		UserAuthDTO u = context.bodyAsClass(UserAuthDTO.class);
		Boolean authenticated = authService.authenticateUser(u);
		if (authenticated) {
			Map<String, String> creds = new HashMap<>();
			creds.put("token", "sample token fix later");
			// TODO: issue a proper token.
			context.status(HttpStatus.OK)
					.json(creds);
		} else {
			context.status(HttpStatus.UNAUTHORIZED).result("Couldn't log in; check credentials");
		}
	}


	private void checkAuthBeforeAction(Context context) {
		List<String> NO_AUTH_ROUTES = new ArrayList<>(List.of(new String[]{
				"/register",
				"/login"
		}));
		String path = context.path();
		if (path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
		}
		if (NO_AUTH_ROUTES.contains(path)) {
			return;
		}
		// We're allowed to make a trusting system, so we only check that a JWT was included and can be decrypted,
		// nothing about its actual contents.

		// TODO: check valid auth.
		// User must present token; decrypted token must contain the userId or authorId supplied.

	}

}
