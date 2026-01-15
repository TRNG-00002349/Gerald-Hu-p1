package com.revature.posts;

import com.revature.users.UserNotFoundException;
import com.revature.utils.BadRequestException;
import com.revature.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class PostController implements Controller {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		server.post("/posts/", this::createNewBlogPost);
		server.get("/posts/{post-id}", this::getBlogPost);
		server.put("/posts", this::updateBlogPost);
		server.delete("/posts", this::deleteBlogPost);
	}

	@Override
	public void registerExceptions(Javalin server) {
		server.exception(PostValidationException.class, this::handlePostValidationException);
		server.exception(PostNotFoundException.class, this::handlePostNotFoundException);
	}

	public void createNewBlogPost(Context context) throws BadRequestException, SQLException, PostValidationException, UserNotFoundException {
		Post post;
		try {
			post = context.bodyAsClass(Post.class);
		} catch (Exception e) {
			throw new BadRequestException(String.format("Couldn't parse %s", context.body()));
		}

		Post persistedPost = postService.saveNewPost(post);
		context.status(HttpStatus.CREATED).json(persistedPost);
	}

	public void getBlogPost(Context context) throws SQLException, PostNotFoundException {
		Post post = postService.readPost(context.pathParam("{post-id}"));
		context.status(HttpStatus.OK).json(post);
	}

	public void updateBlogPost(Context context) {

	}

	public void deleteBlogPost(Context context) {

	}

	private void handlePostNotFoundException(PostNotFoundException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Post not found: %s", e.getMessage()));
	}

	private void handlePostValidationException(PostValidationException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Post validation error: %s", e.getMessage()));
	}
}
