package com.revature.utils;

import com.revature.users.UserController;
import io.javalin.Javalin;

/**
 * Javalin API logic that doesn't belong in any controller.
 */
public class JavalinUtil {
		private static Javalin server;

		public static Javalin startServer() {
				server = Javalin.create(); // Javalin docs frequently calls this "app"
				// server.addEndpoint() if needed

				server.get("/ping", (ctx) -> {
						// UserController.ping(ctx);

						ctx.result("pong!");
				});

				return server.start(10001);
		}
}
