package com.revature.utils;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ControllerUtil {
	public static void handleDBException(Exception e, Context ctx) {
		// TODO: if-clause for "duplicate username"; we want that msg to be user-friendly
		ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result(String.format("A database error occurred: %s", e.getMessage()));
		e.printStackTrace();
	}
}
