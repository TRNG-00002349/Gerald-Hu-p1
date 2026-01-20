package com.revature.auth;

import com.revature.comments.Comment;
import com.revature.posts.Post;
import com.revature.users.UserAuthDTO;
import com.revature.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.eclipse.jetty.http.HttpMethod;

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
		server.exception(NotAuthenticatedException.class, this::handleNotAuthenticatedException);
	}

	private void loginUser(Context context) throws SQLException {
		UserAuthDTO u = context.bodyAsClass(UserAuthDTO.class);
		try {
			String token = authService.authenticateUser(u);
			Map<String, String> creds = new HashMap<>();
			creds.put("token", token);
			context.status(HttpStatus.OK)
					.json(creds);
		} catch (NotAuthenticatedException e) {
			context.status(HttpStatus.UNAUTHORIZED).result(e.getMessage());
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
		} else if (context.req().getMethod().equals("GET")) {
			System.out.println("method == get; skipping auth");
			return;
		}

		System.out.println("attempting authe");
		Integer presentedUserId;
		try {
			JwtParser jwtParser = Jwts.parser()
					.unsecured()
					.build();
			Claims presentedClaims = (Claims) jwtParser
					.parse(context.header("token"))
					.getPayload();
			presentedUserId = presentedClaims.get("userId", Integer.class);
		} catch (Exception e) {
			throw new NotAuthenticatedException("couldn't parse authentication token");
		}

		Integer payloadUserId = -1;

		// This is probably not very resilient logic.
		if (context.path().contains("user")) {
			payloadUserId = Integer.parseInt(context.pathParam("user-id"));
		} else if (context.path().contains("post")) {
			payloadUserId = context.bodyAsClass(Post.class).getAuthorId();
		} else if (context.path().contains("comment")) {
			payloadUserId = context.bodyAsClass(Comment.class).getAuthorId();
		}

		if (presentedUserId.equals(payloadUserId)) {
			return;
			/*
			TODO: We definitely need to split this method up.
			In particular, we need logic that the presented token == ID stored on the entity (user, post, comment);
			those should be handled by individual controllers.
			We don't actually need to include authorId or userId in the payload; the ID will come (implicitly) from the token.
			We probably also want to reuse...
				- the JWT-decoding code
				- the methods, and paths, we actually apply this to
			 */
		} else {
			throw new NotAuthenticatedException("Author ID doesn't match authentication token");
		}

	}

	public void handleNotAuthenticatedException(Exception e, Context context) {
		context.status(HttpStatus.UNAUTHORIZED).result(e.getMessage());
	}

}
