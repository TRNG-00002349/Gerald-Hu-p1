package com.revature.posts;

import com.revature.users.UserNotFoundException;
import com.revature.utils.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class PostDao {

	public Post createPost(Post post) throws SQLException, UserNotFoundException {
		// TODO: deleted users should not be able to make posts.
		String CHECK_IF_AUTHOR_DELETED = """
				SELECT deleted FROM users
				WHERE
				ID = ? AND deleted = FALSE
				""";
		String CREATE_POST_SQL = """
				INSERT INTO posts (content, author_id)
				VALUES (?, ?)
				""";
		try (
				var conn = DataSource.getConnection();
				var checkStmt = conn.prepareStatement(CHECK_IF_AUTHOR_DELETED);
				var pstmt = conn.prepareStatement(CREATE_POST_SQL, Statement.RETURN_GENERATED_KEYS);
				) {
			checkStmt.setInt(1, post.getAuthorId());
			checkStmt.executeQuery();
			ResultSet rs;
			rs = checkStmt.getResultSet();
			if (!rs.isBeforeFirst()) {
				throw new UserNotFoundException(post.getAuthorId().toString());
			}
			rs.next();
			if (rs.getBoolean(1)) {
				throw new UserNotFoundException(post.getAuthorId().toString());
			}

			pstmt.setString(1, post.getContent());
			pstmt.setInt(2, post.getAuthorId());
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				post.setId(rs.getInt("id"));
			}
			post.setCreatedAt(LocalDateTime.now());
			return post;
		}
	}

	public Post readPost(String postId) throws SQLException, PostNotFoundException {
		String READ_POST_SQL = """
				SELECT * FROM POSTS WHERE
				ID = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(READ_POST_SQL)
				) {
			pstmt.setInt(1, Integer.parseInt(postId));
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			if (!rs.isBeforeFirst()) {
				throw new PostNotFoundException(postId);
			}
			rs.next();
			// Post p = new post or whatever
			Post p = new Post();
			p.setId(rs.getInt("id"));
			p.setAuthorId(rs.getInt("author_id"));
			p.setContent(rs.getString("content"));
			p.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
			p.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
			return p;
		} catch (NumberFormatException e) {
			throw new PostNotFoundException(postId);
		}
	}

	public Post updatePost(String postId, Post post) {
		return new Post();
	}

	public void deletePost(String postId) {

	}
}
