package com.revature.users;

public class UserInfoDTO {
	private Integer id;
	private String username;

	public UserInfoDTO() {

	}

	public UserInfoDTO(String username, Integer id) {
		this.username = username;
		this.id = id;
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
}
