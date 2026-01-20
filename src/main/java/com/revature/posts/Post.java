package com.revature.posts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.revature.comments.Comment;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
	private Integer id;
	private String content;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer authorId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone="GMT")
	private LocalDateTime createdAt;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone="GMT")
	private LocalDateTime updatedAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Comment> commentList;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Integer> likesList;

	public Post() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public List<Integer> getLikesList() {
		return likesList;
	}

	public void setLikesList(List<Integer> likesList) {
		this.likesList = likesList;
	}
}
