package com.revature.posts;

public class PostNotFoundException extends Exception {
	// TODO: Consider refactoring to just generic "Entity not found" exception
	public PostNotFoundException(String message) {
		super(message);
	}
}
