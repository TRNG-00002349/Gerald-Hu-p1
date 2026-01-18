package com.revature.comments;

import com.revature.posts.PostDao;
import com.revature.posts.PostNotFoundException;
import com.revature.users.User;
import com.revature.users.UserDao;
import com.revature.users.UserIsDeletedException;
import com.revature.users.UserNotFoundException;
import com.revature.utils.BadRequestException;
import com.revature.utils.DatabaseUtil;

import java.sql.SQLException;

public class CommentService {

	private PostDao postDao;
	private CommentDao commentDao;
	private UserDao userDao;

	public CommentService() {	}

	public CommentService setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
		return this;
	}

	public CommentService setPostDao(PostDao postDao) {
		this.postDao = postDao;
		return this;
	}

	public CommentService setUserDao(UserDao userDao) {
		this.userDao = userDao;
		return this;
	}

	private void validateComment(Comment comment) throws CommentValidationException {
		if (comment.getContent() == null || comment.getContent().isEmpty()) {
			throw new CommentValidationException("Your comment can't be empty!");
		}
	}

	public Comment createCommentOnPost(String postId, Comment comment) throws SQLException, PostNotFoundException, CommentValidationException, UserNotFoundException, UserIsDeletedException {
		if (comment.getAuthorId() == null) {
			throw new UserNotFoundException("no user specified");
		}
		DatabaseUtil.checkIfAuthorDeleted(comment.getAuthorId());
		postDao.readPost(postId); // Check if post exists before commenting. Throw exception if not found.
		validateComment(comment);

		return commentDao.createCommentOnPost(postId, comment);
	}

	public Comment updateCommentOnPost(String commentId, Comment comment) throws UserNotFoundException, SQLException, UserIsDeletedException, CommentValidationException, CommentNotFoundException {
		if (comment.getAuthorId() == null) {
			throw new UserNotFoundException("no user specified");
		}
		DatabaseUtil.checkIfAuthorDeleted(comment.getAuthorId());
		validateComment(comment);

		return commentDao.updateCommentOnPost(commentId, comment);
	}

	public void deleteCommentOnPost(String commentId) throws SQLException, CommentNotFoundException {
		commentDao.deleteCommentOnPost(commentId);
	}
}
