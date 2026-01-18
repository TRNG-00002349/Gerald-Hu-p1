package com.revature.users;

public class UserIsDeletedException extends RuntimeException {
	// TODO: get rid of this guy, replace all usages with UserNotFound. client doens't need to know a user was deleted or not.
	public UserIsDeletedException(String message) {
		super(message);
	}
}
