package com.revature.utils;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ControllerUtil {
	public static void handleDBException(Exception e, Context ctx) {
		// TODO: if-clause for duplicate username or other keys; we want that msg to be user-friendly
		ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(String.format("A database error occurred: %s", e.getMessage()));
		e.printStackTrace();
	}

	public static void handleBadRequestException(Exception e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Bad input to %s: %s", context.path(), e.getMessage()));
		e.printStackTrace();
	}

	public static void handleUnrecognizedPropertyException(Exception e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result("Unrecognized fields in request body");
		e.printStackTrace();
	}

	public static void handleJsonParseException(Exception e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Couldn't parse request body: %s", context.body()));
		e.printStackTrace();
	}

	public static void handleInvalidFormatException(Exception e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Invalid format: %s", context.body()));
	}
}
