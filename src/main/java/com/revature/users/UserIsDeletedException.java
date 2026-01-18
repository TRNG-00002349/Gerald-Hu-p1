package com.revature.users;

public class UserIsDeletedException extends RuntimeException {
	public UserIsDeletedException(String message) {
		super(message);
	}
}
