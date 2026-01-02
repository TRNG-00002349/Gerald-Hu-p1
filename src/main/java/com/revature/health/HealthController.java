package com.revature.health;

import io.javalin.http.Context;

public class HealthController {

		public void ping(Context ctx) {
				String reply = "pong!";
				ctx.result(reply);
		}
}
