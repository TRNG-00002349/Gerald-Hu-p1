package com.revature.comments;

import com.revature.posts.PostDao;

public class CommentService {

	private PostDao postDao;
	private CommentDao commentDao;

	public CommentService() {	}

	public CommentService setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
		return this;
	}

	public CommentService setPostDao(PostDao postDao) {
		this.postDao = postDao;
		return this;
	}
}
