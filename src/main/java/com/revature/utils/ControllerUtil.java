package com.revature.utils;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.revature.auth.AuthFailureException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerUtil {

	public static Integer getUserIdFromContext(Context context) {
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
			throw new AuthFailureException("couldn't parse authentication token");
		}
		return presentedUserId;
	}

	public static Boolean checkIfNeedsAuth(Context context) {
		List<String> NO_AUTH_ROUTES = new ArrayList<>(List.of(new String[]{
				"/register",
				"/login"
		}));
		String path = context.path();
		if (path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
		}
		if (NO_AUTH_ROUTES.contains(path)) {
			return false;
		} else if (context.req().getMethod().equals("GET")) {
			System.out.println("method == get; skipping auth");
			return false;
		}
		return true;
	}

	public static void handleDBException(SQLException e, Context ctx) {
		if (e.getSQLState().equals("23505")) {
			ctx.status(HttpStatus.BAD_REQUEST).result("Duplicate username or email already exists");
		} else {
			ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(String.format("A database error occurred: %s", e.getMessage()));
			e.printStackTrace();
		}
	}

	public static void handleUnrecognizedPropertyException(UnrecognizedPropertyException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result("Unrecognized fields in request body");
		e.printStackTrace();
	}

	public static void handleParseException(Exception e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Couldn't parse request body: %s", context.body()));
		e.printStackTrace();
	}

	public static void handleNumberFormatException(NumberFormatException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Invalid ID: %s", e.getMessage()));
	}
}
