package com.revature.posts;

import java.sql.SQLException;

public class PostService {

	private PostDao postDao;

	public PostService() {}

	public PostService setPostDao(PostDao postDao) {
		this.postDao = postDao;
		return this;
	}

	private void validatePost(Post post) {
		if (post.getContent() == null || post.getContent().isEmpty()) {
			throw new PostValidationException("Your post can't be empty!");
		}
	}

	public Post saveNewPost(Post post) throws SQLException {
		validatePost(post);

		return postDao.createPost(post);
	}

	public Post readPost(String postId) throws SQLException {
		return postDao.readPost(postId);
	}

	public Post updatePost(String postId, Post post) throws SQLException {
		return postDao.updatePost(postId, post);
	}

	public void deletePost(String postId) throws SQLException {
		postDao.deletePost(postId);
	}
}
