package com.revature.comments;

import com.revature.auth.AuthFailureException;
import com.revature.posts.PostController;
import com.revature.utils.Controller;
import com.revature.utils.ControllerUtil;
import com.revature.utils.ServiceUtil;
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
		// Going to /posts/{post-id} will also show that post's comments, without needing to append /comments to it.

		server.post("/posts/{post-id}/comments", this::createCommentOnPost);
		server.get("/posts/{post-id}/comments", PostController::getBlogPost);
		server.get("/posts/{post-id}/comments/{comment-id}", this::getCommentOnPost);
		server.put("/posts/{post-id}/comments/{comment-id}", this::updateCommentOnPost);
		server.delete("/posts/{post-id}/comments/{comment-id}", this::deleteCommentOnPost);

		server.before("/posts/{post-id}/comments/{comment-id}*", this::validatePostAndCommentId);
		server.before("/posts/{post-id}/comments/{comment-id}*", this::authorizeForCommentEndpoints);
	}

	@Override
	public void registerExceptions(Javalin server) {
		server.exception(CommentValidationException.class, this::handleInvalidCommentException);
		server.exception(CommentNotFoundException.class, this::handleCommentNotFoundException);
	}

	public void validatePostAndCommentId(Context context) {
		String postId = context.pathParam("post-id");
		ServiceUtil.validateIdFormat(postId);
		String commentId = context.pathParam("comment-id");
		ServiceUtil.validateIdFormat(commentId);
	}

	private void authorizeForCommentEndpoints(Context context) throws SQLException {
		Boolean needsAuth = ControllerUtil.checkIfNeedsAuth(context);
		if (!needsAuth) {
			return;
		}
		Integer presentedUserId = ControllerUtil.getUserIdFromContext(context);
		if (presentedUserId == null) {
			throw new AuthFailureException("couldn't parse user token");
		}
		if (context.req().getMethod().equals("POST")) {
			// a comment that hasn't been made yet won't have an author to compare it to
			return;
		}
		Integer contentAuthorId = commentService.getCommentAuthorId(
				context.pathParam("comment-id")
		);
		if (!presentedUserId.equals(contentAuthorId)) {
			throw new AuthFailureException("Not allowed to modify someone else's comment");
		}
	}


	private void createCommentOnPost(Context context) throws SQLException {
		Comment c = context.bodyAsClass(Comment.class);
		c.setAuthorId(ControllerUtil.getUserIdFromContext(context));

		Comment persistedComment = commentService.createCommentOnPost(
				context.pathParam("post-id"), c
		);
		context.status(HttpStatus.CREATED).json(persistedComment);
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

	private void handleCommentNotFoundException(CommentNotFoundException e, Context context) {
		context.status(HttpStatus.BAD_REQUEST).result(String.format("Comment not found: %s", e.getMessage()));
	}
}
