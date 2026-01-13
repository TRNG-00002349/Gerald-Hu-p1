package com.revature.utils;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ControllerUtil {
	public static void handleDBException(Exception e, Context ctx) {
		ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("A database error occurred");
		e.printStackTrace();
	}
}
