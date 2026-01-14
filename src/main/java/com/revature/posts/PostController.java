package com.revature.posts;

import io.javalin.http.Context;

public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	public void createNewBlogPost(Context context) {

	}

	public void getBlogPost(Context context) {

	}

	public void updateBlogPost(Context context) {

	}

	public void deleteBlogPost(Context context) {

	}
}
