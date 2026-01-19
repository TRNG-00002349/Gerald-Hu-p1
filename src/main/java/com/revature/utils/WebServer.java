package com.revature.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.javalin.Javalin;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/*
	Wholly based on Kyle Plummer's builder code. Thank you.
 */
public class WebServer {

	private final Javalin server;
	private final Integer port;

	public WebServer(Builder builder) {
		this.server = builder.server;
		this.port = builder.port;

		for (Controller c : builder.controllers) {
			c.registerRoutes(server);
			c.registerExceptions(server);
		}

		server.exception(SQLException.class, ControllerUtil::handleDBException);
		server.exception(UnrecognizedPropertyException.class, ControllerUtil::handleUnrecognizedPropertyException);
		server.exception(JsonParseException.class, ControllerUtil::handleJsonParseException);
		server.exception(InvalidFormatException.class, ControllerUtil::handleInvalidFormatException);
		server.exception(NumberFormatException.class, ControllerUtil::handleNumberFormatException);
	}

	public void start() {
		this.server.start(this.port);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Javalin server;
		private Integer port;
		private List<Controller> controllers = new LinkedList<>();

		public Builder addController(Controller controller) {
			controllers.add(controller);
			return this;
		}

		public Builder port(Integer port) {
			this.port = port;
			return this;
		}

		public WebServer build() {
			this.server = Javalin.create();
			return new WebServer(this);
		}

	}
}
