package com.revature.health;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class HealthController {

		public void ping(Context ctx) {
				String reply = "pong!";
				ctx.result(reply);
		}

		public void headerTest(Context ctx) {
				String headerString = ctx.header("header-test");
				ctx.header("header-test-response", String.format("Response: %s", headerString));
				if (headerString != null) {
						ctx.result(String.format("You sent %s in the header", headerString)).status(HttpStatus.OK);
				}
				else {
						ctx.result("You didn't specify header-test").status(HttpStatus.BAD_REQUEST);
				}
		}
}
