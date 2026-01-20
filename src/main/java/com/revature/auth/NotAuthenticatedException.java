package com.revature.auth;

public class NotAuthenticatedException extends RuntimeException {
	public NotAuthenticatedException(String message) {
		super(message);
	}
}
