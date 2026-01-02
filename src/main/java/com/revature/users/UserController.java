package com.revature.users;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Handles HTTP request/response logic. Maybe exception handling.
 */
public class UserController {

		public void ping(Context ctx) {
				String reply = "pong!";
				ctx.result(reply);
		}
}
