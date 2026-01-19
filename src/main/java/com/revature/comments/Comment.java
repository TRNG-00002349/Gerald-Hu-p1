package com.revature.comments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class Comment {
	private Integer id;
	private String content;
	private Integer authorId;
	@JsonIgnore
	private Integer postId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime createdAt;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime updatedAt;

	public Comment() {

	}

	public Comment(Integer id, LocalDateTime createdAt, LocalDateTime updatedAt, String content, Integer authorId, Integer postId) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.content = content;
		this.authorId = authorId;
		this.postId = postId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}
}
