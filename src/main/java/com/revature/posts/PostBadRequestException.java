package com.revature.posts;

public class PostBadRequestException extends RuntimeException {
	public PostBadRequestException(String message) {
		super(message);
	}
}
