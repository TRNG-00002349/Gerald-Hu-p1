package com.revature.users;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class UserInfoDTO {
	private Integer id;
	private String username;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime createdAt;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime updatedAt;

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
