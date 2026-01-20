package com.revature.posts;

import com.revature.auth.AuthFailureException;
import com.revature.utils.Controller;
import com.revature.utils.ControllerUtil;
import com.revature.utils.ServiceUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;

public class PostController implements Controller {
	private static PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		server.post("/posts/", this::createNewBlogPost);
		server.get("/posts/{post-id}*", this::getBlogPost);
		// Will get comments and likes as well. TODO: modify this to get the likes as well.
		server.put("/posts/{post-id}", this::updateBlogPost);
		server.delete("/posts/{post-id}", this::deleteBlogPost);

		server.before("/posts/{post-id}*", this::validateBlogPostId);
		server.before("/posts/{post-id}", this::authorizeForPostEndpoints);
		server.before("/posts/{post-id}/", this::authorizeForPostEndpoints);
	}

	@Override
	public void registerExceptions(Javalin server) {
		server.exception(PostValidationException.class, this::handlePostValidationException);
		server.exception(PostNotFoundException.class, this::handlePostNotFoundException);
	}

	private void authorizeForPostEndpoints(Context context) throws SQLException {
		Boolean needsAuth = ControllerUtil.checkIfNeedsAuth(context);
		if (!needsAuth) {
			return;
		}
		Integer presentedUserId = ControllerUtil.getUserIdFromContext(context);
		if (presentedUserId == null) {
			throw new AuthFailureException("couldn't parse user token");
		}
		if (context.req().getMethod().equals("POST")) {
			// a post that hasn't been made yet won't have an author to compare it to
			return;
		}
		Integer contentAuthorId = postService.getPostAuthorId(
				context.pathParam("post-id")
		);
		if (!presentedUserId.equals(contentAuthorId)) {
			throw new AuthFailureException("Not allowed to modify someone else's post");
		}
	}

	public void validateBlogPostId(Context context) {
		String postId = context.pathParam("post-id");
		ServiceUtil.validateIdFormat(postId);
	}

	public void createNewBlogPost(Context context) throws SQLException {
		Post post = context.bodyAsClass(Post.class);
		Integer authorId = ControllerUtil.getUserIdFromContext(context);
		post.setAuthorId(authorId);
		Post persistedPost = postService.saveNewPost(post);
		context.status(HttpStatus.CREATED).json(persistedPost);
	}

	public void getBlogPost(Context context) throws SQLException {
		Post post = postService.readPost(context.pathParam("post-id"));
		context.status(HttpStatus.OK).json(post);
	}

	public void updateBlogPost(Context context) throws SQLException {
		Post p = context.bodyAsClass(Post.class);
		Post post = postService.updatePost(context.pathParam("post-id"), p);
		context.status(HttpStatus.OK).json(post);
	}

	public void deleteBlogPost(Context context) throws SQLException {
		postService.deletePost(context.pathParam("post-id"));
		context.status(HttpStatus.NO_CONTENT);
	}

	private void handlePostNotFoundException(PostNotFoundException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Post not found: %s", e.getMessage()));
	}

	private void handlePostValidationException(PostValidationException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Post validation error: %s", e.getMessage()));
	}
}
