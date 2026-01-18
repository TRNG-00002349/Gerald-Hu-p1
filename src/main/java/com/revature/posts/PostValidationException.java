package com.revature.posts;

public class PostValidationException extends RuntimeException {
	public PostValidationException(String message) {
		super(message);
	}
}
