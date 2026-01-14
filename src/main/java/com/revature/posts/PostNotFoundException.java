package com.revature.posts;

public class PostNotFoundException extends Throwable {
	public PostNotFoundException(String message) {
		super(message);
	}
}
