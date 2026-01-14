package com.revature.posts;

public class PostValidationException extends Exception {
	// TODO: Consider refactoring this and UserNotFoundException to just generic "Entity not found exception".
	public PostValidationException(String message) {
		super(message);
	}
}
