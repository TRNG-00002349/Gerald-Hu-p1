package com.revature.likes;

import com.revature.auth.AuthFailureException;
import com.revature.utils.Controller;
import com.revature.utils.ControllerUtil;
import com.revature.utils.ServiceUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class LikesController implements Controller {

	// private static LikesService likesService;

	@Override
	public void registerRoutes(Javalin server) {
		server.post("/posts/{post-id}/likes*", this::createLikeOnPost);
		server.delete("/posts/{post-id}/likes*", this::deleteLikeOnPost);

		server.before("/posts/{post-id}/likes*", this::validateBlogPostId);
		server.before("/posts/{post-id}/likes*", this::authorizeForLikeEndpoints);
	}

	@Override
	public void registerExceptions(Javalin server) {

	}

	public void validateBlogPostId(Context context) {
		String postId = context.pathParam("post-id");
		ServiceUtil.validateIdFormat(postId);
	}

	public void authorizeForLikeEndpoints(Context context) {
		Boolean needsAuth = ControllerUtil.checkIfNeedsAuth(context);
		if (!needsAuth) {
			return;
		}
		Integer presentedUserId = ControllerUtil.getUserIdFromContext(context);
		if (presentedUserId == null) {
			throw new AuthFailureException("couldn't parse user token");
		}
		// When we DELETE, we're always deleting the user's own "Like",
		// so there's no need to compare authorId; it will always be the same.
	}

	public void createLikeOnPost(Context context) {

	}

	public void deleteLikeOnPost(Context context) {

	}
}
