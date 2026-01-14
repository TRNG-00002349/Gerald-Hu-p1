package com.revature.users;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
	private Integer id;
	private String username;
	private String email;
	@JsonIgnore
	private String hashedPassword;
	@JsonIgnore
	private String salt;
	// Note: Could use private UserAuthDTO instead, using composition.

	public User() {

	}

	public User(Integer id, String username, String hashedPassword, String email, String salt) {
		this.id = id;
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.email = email;
		this.salt = salt;
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
