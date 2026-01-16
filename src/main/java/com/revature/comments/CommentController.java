package com.revature.comments;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.revature.posts.Post;
import com.revature.posts.PostController;
import com.revature.posts.PostNotFoundException;
import com.revature.users.UserIsDeletedException;
import com.revature.users.UserNotFoundException;
import com.revature.utils.BadRequestException;
import com.revature.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class CommentController implements Controller {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		// Base URL of /posts/{post-id}/comments.
		// Going to /posts/{post-id} will also show that post's comments, without needing to append /comments to it.

		server.post("/posts/{post-id}/comments", this::createCommentOnPost);
		server.get("/posts/{post-id}/comments", PostController::getBlogPost);
	}

	@Override
	public void registerExceptions(Javalin server) {
		server.exception(CommentValidationException.class, this::handleInvalidCommentException);
	}

	private void createCommentOnPost(Context context) throws SQLException, PostNotFoundException, CommentValidationException, UserNotFoundException, BadRequestException, UserIsDeletedException {
		Comment c = commentService.createCommentOnPost(
				context.pathParam("post-id"),
				context.bodyAsClass(Comment.class)
		);
		context.status(HttpStatus.OK).json(c);
	}

	private void handleInvalidCommentException(CommentValidationException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Comment validation error: %s", e.getMessage()));
	}
}
