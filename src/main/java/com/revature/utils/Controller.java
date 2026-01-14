package com.revature.utils;

import io.javalin.Javalin;

public interface Controller {
	void registerRoutes(Javalin server);

	void registerExceptions(Javalin server);
}
