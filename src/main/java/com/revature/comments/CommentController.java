package com.revature.comments;

import com.revature.posts.PostController;
import com.revature.utils.Controller;
import com.revature.utils.ControllerUtil;
import com.revature.utils.ServiceUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;

public class CommentController implements Controller {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		// Going to /posts/{post-id} will also show that post's comments, without needing to append /comments to it.

		server.post("/posts/{post-id}/comments", this::createCommentOnPost);
		server.get("/posts/{post-id}/comments", PostController::getBlogPost);
		server.get("/posts/{post-id}/comments/{comment-id}", this::getCommentOnPost);
		server.put("/posts/{post-id}/comments/{comment-id}", this::updateCommentOnPost);
		server.delete("/posts/{post-id}/comments/{comment-id}", this::deleteCommentOnPost);
		server.before("/posts/{post-id}/comments/{comment-id}*", this::validatePostAndCommentId);
	}


	@Override
	public void registerExceptions(Javalin server) {
		server.exception(CommentValidationException.class, this::handleInvalidCommentException);
		server.exception(CommentNotFoundException.class, this::handleCommentNotFoundException);
	}

	public void validatePostAndCommentId(Context context) {
		String postId = context.pathParam("post-id");
		ServiceUtil.validateId(postId);
		String commentId = context.pathParam("comment-id");
		ServiceUtil.validateId(commentId);
	}

	private void handleCommentNotFoundException(CommentNotFoundException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Comment not found: %s", e.getMessage()));
	}

	private void createCommentOnPost(Context context) throws SQLException {
		Comment c = commentService.createCommentOnPost(
				context.pathParam("post-id"),
				context.bodyAsClass(Comment.class)
		);
		context.status(HttpStatus.CREATED).json(c);
	}

	private void getCommentOnPost(Context context) throws SQLException {
		Comment c = commentService.getCommentOnPost(
				context.pathParam("comment-id"),
				context.bodyAsClass(Comment.class)
		);
		context.status(HttpStatus.OK).json(c);
	}

	private void updateCommentOnPost(Context context) throws SQLException {
		Comment c = commentService.updateCommentOnPost(
				context.pathParam("comment-id"),
				context.bodyAsClass(Comment.class)
		);
		context.status(HttpStatus.OK).json(c);
	}

	private void deleteCommentOnPost(Context context) throws SQLException {
		commentService.deleteCommentOnPost(context.pathParam("comment-id"));
		context.status(HttpStatus.NO_CONTENT);
	}

	private void handleInvalidCommentException(CommentValidationException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Comment validation error: %s", e.getMessage()));
	}
}
