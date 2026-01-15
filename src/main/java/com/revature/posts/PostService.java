package com.revature.posts;

import com.revature.users.UserNotFoundException;

import java.sql.SQLException;

public class PostService {

	private final PostDao postDao;

	public PostService(PostDao postDao) {
		this.postDao = postDao;
	}


	private void validatePost(Post post) throws PostValidationException {
		if (post.getContent().isEmpty()) {
			throw new PostValidationException("Your post can't be empty!");
		}
	}

	public Post saveNewPost(Post post) throws PostValidationException, SQLException, UserNotFoundException {
		validatePost(post);

		return postDao.createPost(post);
	}

	public Post readPost(String postId) throws SQLException, PostNotFoundException {
		return postDao.readPost(postId);
	}

	public Post updatePost(String postId, Post post) throws UserNotFoundException, SQLException, PostNotFoundException {
		return postDao.updatePost(postId, post);
	}

	public void deletePost(String postId) {
		postDao.deletePost(postId);
	}
}
