package com.revature.comments;

import com.revature.posts.Post;
import com.revature.posts.PostController;
import com.revature.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class CommentController implements Controller {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@Override
	public void registerRoutes(Javalin server) {
		// Base URL of /posts/{post-id}/comments.
		// Going to /posts/{post-id} will also show that post's comments, without needing to append /comments to it.

		server.get("/posts/{post-id}/comments", PostController::getBlogPost);
	}

	@Override
	public void registerExceptions(Javalin server) {

	}


	private void showPostComments(Context context) {

	}

}
