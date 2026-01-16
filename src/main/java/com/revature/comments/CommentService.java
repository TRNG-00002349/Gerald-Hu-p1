package com.revature.comments;

import com.revature.posts.PostDao;
import com.revature.posts.PostNotFoundException;

import java.sql.SQLException;

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

	private void validateComment(Comment comment) throws CommentValidationException {
		if (comment.getContent() == null || comment.getContent().isEmpty()) {
			throw new CommentValidationException("Your comment can't be empty!");
		}
	}

	public Comment createCommentOnPost(String postId, Comment comment) throws SQLException, PostNotFoundException, CommentValidationException {
		postDao.readPost(postId); // Check if post exists before commenting. Throw exception if not found.
		validateComment(comment);

		return commentDao.createCommentOnPost(postId, comment);
	}
}
