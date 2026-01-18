package com.revature.posts;

import com.revature.comments.Comment;
import com.revature.users.UserNotFoundException;
import com.revature.utils.DataSource;
import com.revature.utils.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class PostDao {

	private Integer getPostAuthorId(Integer postId) throws SQLException {
		String GET_AUTHOR_ID = """
				SELECT author_id FROM posts
				WHERE
				ID = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var checkPost = conn.prepareStatement(GET_AUTHOR_ID);
		) {
			ResultSet rs;
			checkPost.setInt(1, postId);
			checkPost.executeQuery();
			rs = checkPost.getResultSet();
			if (!rs.isBeforeFirst()) {
				throw new PostNotFoundException(postId.toString());
			}
			rs.next();
			return rs.getInt("author_id");
		}
	}

	public Post createPost(Post post) throws SQLException {
		String CREATE_POST_SQL = """
				INSERT INTO posts (content, author_id)
				VALUES (?, ?)
				""";
		DatabaseUtil.checkIfAuthorDeleted(post.getAuthorId());

		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(CREATE_POST_SQL, Statement.RETURN_GENERATED_KEYS);
				) {
			pstmt.setString(1, post.getContent());
			pstmt.setInt(2, post.getAuthorId());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				post.setId(rs.getInt("id"));
			}
			post.setCreatedAt(LocalDateTime.now());
			return post;
		}
	}

	public Post readPost(String postId) throws SQLException {
		String READ_POST_SQL = """
				SELECT * FROM POSTS WHERE
				ID = ?
				""";
		String READ_POST_COMMENTS_SQL = """
				SELECT * FROM COMMENTS WHERE
				POST_ID = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(READ_POST_SQL);
				var pstmt2 = conn.prepareStatement(READ_POST_COMMENTS_SQL);
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

			pstmt2.setInt(1, Integer.parseInt(postId));
			pstmt2.executeQuery();
			ResultSet rs2 = pstmt2.getResultSet();
			List<Comment> commentList = new LinkedList<>();
			p.setCommentList(commentList);
			if (!rs2.isBeforeFirst()) {
				return p;
			}
			while (rs2.next()) {
				Comment c = new Comment();
				c.setAuthorId(rs2.getInt("author_id"));
				c.setContent(rs2.getString("content"));
				c.setCreatedAt(rs2.getObject("created_at", LocalDateTime.class));
				c.setUpdatedAt(rs2.getObject("updated_at", LocalDateTime.class));
				commentList.add(c);
			}
			return p;
		} catch (NumberFormatException e) {
			throw new PostNotFoundException(postId);
		}
	}

	public Post updatePost(String postId, Post post) throws SQLException {
		String UPDATE_POST_SQL = """
				UPDATE posts SET
				content = ?,
				updated_at = ?
				WHERE
				id = ?
				""";

		Integer authorId = getPostAuthorId(Integer.parseInt(postId));
		// System.out.println(String.format("we should see an author ID here: %s", authorId));
		DatabaseUtil.checkIfAuthorDeleted(authorId);

		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(UPDATE_POST_SQL);
				) {
			pstmt.setString(1, post.getContent());
			pstmt.setObject(2, LocalDateTime.now());
			pstmt.setInt(3, Integer.parseInt(postId));
			Integer updated = pstmt.executeUpdate();
			if (updated.equals(0)) {
				throw new PostNotFoundException(post.getId().toString());
			}
			post.setUpdatedAt(LocalDateTime.now());
			post.setId(Integer.parseInt(postId));
			post.setAuthorId(authorId);
			return post;
		} catch (NumberFormatException e) {
			// TODO: refactor so NumberFormatException is caught and returned at the controller level.
			throw new PostNotFoundException(post.getId().toString());
		}
	}

	public void deletePost(String postId) throws SQLException {
		// TODO: some functions take string ID and some take int ID. fix for consistency. I guess.
		// TODO: When deleting a post, cascade delete comments
		String DELETE_POST_SQL = """
				DELETE FROM posts WHERE id = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(DELETE_POST_SQL);
		) {
			pstmt.setInt(1, Integer.parseInt(postId));
			Integer deleted = pstmt.executeUpdate();
			if (deleted.equals(0)) {
				throw new PostNotFoundException(postId);
			}
		} catch (NumberFormatException e) {
			throw new PostNotFoundException(postId);
		}
	}

	public List<Post> readPostsByUser(String authorId) throws SQLException {
		String READ_POSTS_BY_USER_SQL = """
				SELECT * FROM posts
				WHERE 
				author_id = ?
				""";
		LinkedList<Post> postList = new LinkedList<>();
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(READ_POSTS_BY_USER_SQL);
				) {
			pstmt.setInt(1, Integer.parseInt(authorId));
			ResultSet rs = pstmt.executeQuery();
			if (rs == null) {
				return postList;
			}
			while (rs.next()) {
				Post p = new Post();
				p.setId(rs.getInt("id"));
				p.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
				p.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
				p.setContent(rs.getString("content"));
				postList.add(p);
			}
			return postList;

		} catch (NumberFormatException e) {
			throw new UserNotFoundException(authorId);
		}
	}
}
