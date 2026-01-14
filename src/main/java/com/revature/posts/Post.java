package com.revature.posts;

public class Post {
	private Integer id;
	private String content;
	private Integer authorId;

	public Post () {

	}

	public Post(Integer id, String content, Integer authorId) {
		this.id = id;
		this.content = content;
		this.authorId = authorId;
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
}
