package com.revature.posts;

public class PostService {

	private final PostDao postDao;

	public PostService(PostDao postDao) {
		this.postDao = postDao;
	}

	public Post saveNewPost(Post post) {
		return postDao.createPost(post);
	}

	public Post readPost(Integer postId) {
		return postDao.readPost(postId);
	}

	public Post updatePost(Integer postId, Post post) {
		return postDao.updatePost(postId, post);
	}

	public void deletePost(Integer postId) {
		postDao.deletePost(postId);
	}
}
