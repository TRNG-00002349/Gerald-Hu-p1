package com.revature.posts;

import com.revature.utils.BadRequestException;
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
		server.get("/posts/{post-id}", PostController::getBlogPost);
		server.put("/posts/{post-id}", this::updateBlogPost);
		server.delete("/posts/{post-id}", this::deleteBlogPost);
		server.before("/posts/{post-id}", this::validateBlogPostId);
		server.before("/posts/{post-id}/", this::validateBlogPostId);
	}

	@Override
	public void registerExceptions(Javalin server) {
		server.exception(PostValidationException.class, this::handlePostValidationException);
		server.exception(PostNotFoundException.class, this::handlePostNotFoundException);
	}

	public void validateBlogPostId(Context context) {
		String postId = context.pathParam("post-id");
		ServiceUtil.validateId(postId);
	}

	public void createNewBlogPost(Context context) throws SQLException {
		Post post;
		try {
			post = context.bodyAsClass(Post.class);
		} catch (Exception e) {
			// TODO: see if this can be trimmed down too, using InvalidFormatException to pivot
			throw new BadRequestException(String.format("Couldn't parse %s", context.body()));
		}

		Post persistedPost = postService.saveNewPost(post);
		context.status(HttpStatus.CREATED).json(persistedPost);
	}

	public static void getBlogPost(Context context) throws SQLException {
		Post post = postService.readPost(context.pathParam("post-id"));
		context.status(HttpStatus.OK).json(post);
	}

	public void updateBlogPost(Context context) throws SQLException {
		Post p;
		try {
			p = context.bodyAsClass(Post.class);
		} catch (Exception e) {
			throw new BadRequestException(String.format("Couldn't parse %s", context.body()));
		}
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
