package com.revature.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;

public class ControllerUtil {
	public static void handleDBException(SQLException e, Context ctx) {
		if (e.getSQLState().equals("23505")) {
			ctx.status(HttpStatus.BAD_REQUEST).result("Duplicate username or email already exists");
		} else {
			ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(String.format("A database error occurred: %s", e.getMessage()));
			e.printStackTrace();
		}
	}

	public static void handleBadRequestException(BadRequestException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Bad input to %s: %s", context.path(), e.getMessage()));
		e.printStackTrace();
	}

	public static void handleUnrecognizedPropertyException(UnrecognizedPropertyException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result("Unrecognized fields in request body");
		e.printStackTrace();
	}

	public static void handleJsonParseException(JsonParseException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Couldn't parse request body: %s", context.body()));
		e.printStackTrace();
	}

	public static void handleInvalidFormatException(InvalidFormatException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Invalid format: %s", context.body()));
		e.printStackTrace();
	}

	public static void handleNumberFormatException(NumberFormatException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Invalid number: %s", e.getMessage()));
	}
}
