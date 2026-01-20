package com.revature;

import com.revature.auth.AuthController;
import com.revature.auth.AuthDao;
import com.revature.auth.AuthService;
import com.revature.comments.CommentController;
import com.revature.comments.CommentDao;
import com.revature.comments.CommentService;
import com.revature.health.HealthController;
import com.revature.likes.LikeController;
import com.revature.likes.LikeDao;
import com.revature.likes.LikeService;
import com.revature.posts.Post;
import com.revature.posts.PostController;
import com.revature.posts.PostDao;
import com.revature.posts.PostService;
import com.revature.users.User;
import com.revature.users.UserController;
import com.revature.users.UserDao;
import com.revature.users.UserService;
import com.revature.utils.DatabaseUtil;
import com.revature.utils.WebServer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
	/*
	Future work: Test suite.
		- Most endpoints: empty body, body of {}, malformed body, proper body
		- Most endpoints (testing Ids): proper user ID, not a number, negative number, not-specified
		- Auth: username no email, email no username, neither, both; valid password, invalid password
	 */

	/*
	Future work: better authE/authZ. Roles, including an admin role, for updating and deleting
	content that isn't directly yours.
	 */

	// TODO: likes feature.

	public static void main(String[] args) {
		System.out.println("Hello world!");

		Properties props = new Properties();
		try {
			props.load(DatabaseUtil.class.getClassLoader().getResourceAsStream("application.properties"));
			if (props.getProperty("mode").equals("create")) {
				DatabaseUtil.initializeData();
				System.out.println("Database initialized");
			}

			UserDao userDao = new UserDao();
			PostDao postDao = new PostDao();
			CommentDao commentDao = new CommentDao();

			HealthController healthController = new HealthController();
			UserController userController = new UserController(
					new UserService()
							.setUserDao(userDao)
							.setPostDao(postDao)
			);
			PostController postController = new PostController(
					new PostService()
							.setPostDao(postDao)
			);
			CommentController commentController = new CommentController(
					new CommentService()
							.setPostDao(postDao)
							.setCommentDao(commentDao)
			);
			AuthController authController = new AuthController(
					new AuthService(
							new AuthDao()
					)
			);
			LikeController likeController = new LikeController(
					new LikeService(
							new LikeDao()
					)
			);

			WebServer server = WebServer.builder()
					.port(10001)
					.addController(healthController)
					.addController(userController)
					.addController(postController)
					.addController(commentController)
					.addController(authController)
					.addController(likeController)
					.build();
			server.start();
		} catch (IOException e) {
			System.out.println("Couldn't find application.properties");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}