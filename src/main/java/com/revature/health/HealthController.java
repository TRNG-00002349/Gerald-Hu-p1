package com.revature.health;

import com.revature.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class HealthController implements Controller {

	@Override
	public void registerRoutes(Javalin server) {
		server.get("/ping", this::ping);
		server.get("/header-test", this::headerTest);
	}

	public void registerExceptions(Javalin server) {

	}

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
