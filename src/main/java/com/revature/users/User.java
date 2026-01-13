package com.revature.users;

public class User {
	private Integer id;
	private String username;
	private String hashedPassword; // tbd whether hashed or not
	// Note: Could use private UserAuthDTO instead, using composition.
	// For simplicity when writing javalin/JDBC, we're doing it this way for now.
	private String email;

	public User() {

	}

	public User(Integer id, String username, String hashedPassword, String email) {
		this.id = id;
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
