package com.revature.posts;

import com.revature.comments.Comment;
import com.revature.utils.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class PostDao {

	public Integer getPostAuthorId(String postId) throws SQLException {
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
			checkPost.setInt(1, Integer.parseInt(postId));
			checkPost.executeQuery();
			rs = checkPost.getResultSet();
			if (!rs.isBeforeFirst()) {
				throw new PostNotFoundException(postId);
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
		String READ_POST_LIKES_SQL = """
				SELECT user_id FROM likes WHERE
				POST_ID = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(READ_POST_SQL);
				var pstmtComments = conn.prepareStatement(READ_POST_COMMENTS_SQL);
				var pstmtLikes = conn.prepareStatement(READ_POST_LIKES_SQL);
				) {
			pstmt.setInt(1, Integer.parseInt(postId));
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			if (!rs.isBeforeFirst()) {
				throw new PostNotFoundException(postId);
			}
			rs.next();
			Post p = new Post();
			p.setId(rs.getInt("id"));
			p.setAuthorId(rs.getInt("author_id"));
			p.setContent(rs.getString("content"));
			p.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
			p.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));

			pstmtComments.setInt(1, Integer.parseInt(postId));
			pstmtComments.executeQuery();
			ResultSet rsComments = pstmtComments.getResultSet();
			List<Comment> commentList = new LinkedList<>();
			p.setCommentList(commentList);
			if (rsComments.isBeforeFirst()) {
				while (rsComments.next()) {
					Comment c = new Comment();
					c.setId(rsComments.getInt("id"));
					c.setAuthorId(rsComments.getInt("author_id"));
					c.setContent(rsComments.getString("content"));
					c.setCreatedAt(rsComments.getObject("created_at", LocalDateTime.class));
					c.setUpdatedAt(rsComments.getObject("updated_at", LocalDateTime.class));
					commentList.add(c);
				}
			}


			pstmtLikes.setInt(1, Integer.parseInt(postId));
			pstmtLikes.executeQuery();
			ResultSet rsLikes = pstmtLikes.getResultSet();
			List<Integer> likesList = new LinkedList<>();
			p.setLikesList(likesList);
			if (rsLikes.isBeforeFirst()) {
				while (rsLikes.next()) {
					likesList.add(rsLikes.getInt("user_id"));
				}
			}
			return p;
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

		Integer authorId = getPostAuthorId(postId);

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
		}
	}

	public void deletePost(String postId) throws SQLException {
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
		}
	}

	public static List<Post> readPostsByUser(String authorId) throws SQLException {
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
		}
	}
}
