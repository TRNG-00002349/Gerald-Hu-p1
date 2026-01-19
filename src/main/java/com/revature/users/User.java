package com.revature.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revature.posts.Post;

import java.time.LocalDateTime;
import java.util.List;

public class User {
	private Integer id;
	private String username;

	@JsonIgnore
	private String email;
	@JsonIgnore
	private String hashedPassword;
	// Note: Could use private UserAuthDTO instead, using composition.

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime createdAt;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime updatedAt;

	@JsonIgnore
	private Boolean deleted;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Post> userPosts;

	public User() {

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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}


	public List<Post> getUserPosts() {
		return userPosts;
	}

	public void setUserPosts(List<Post> userPosts) {
		this.userPosts = userPosts;
	}

}
