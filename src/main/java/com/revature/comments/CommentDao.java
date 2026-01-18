package com.revature.comments;

import com.revature.users.UserNotFoundException;
import com.revature.utils.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CommentDao {
	public Comment createCommentOnPost(String postId, Comment comment) throws SQLException {
		// TODO: validate comment isn't empty
		String CREATE_COMMENT_ON_POST_SQL = """
				INSERT INTO comments (created_at, content, author_id, post_id)
				VALUES (?, ?, ?, ?)
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(CREATE_COMMENT_ON_POST_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
				) {
			LocalDateTime now = LocalDateTime.now();
			pstmt.setObject(1, now);
			pstmt.setString(2, comment.getContent());
			pstmt.setInt(3, comment.getAuthorId());
			pstmt.setInt(4, Integer.parseInt(postId));
			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				comment.setId(rs.getInt("id"));
				comment.setCreatedAt(now);
			}
			return comment;
		}
	}

	public Comment updateCommentOnPost(String commentId, Comment comment) throws SQLException {
		String UPDATE_COMMENT_ON_POST_SQL = """
				UPDATE comments SET
				content = ?,
				updated_at = ?
				WHERE
				id = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(UPDATE_COMMENT_ON_POST_SQL);
				) {
			LocalDateTime now = LocalDateTime.now();
			pstmt.setString(1, comment.getContent());
			pstmt.setObject(2, now);
			pstmt.setInt(3, Integer.parseInt(commentId));
			Integer updated = pstmt.executeUpdate();
			if (updated.equals(0)) {
				throw new CommentNotFoundException(commentId);
			}
			comment.setId(Integer.parseInt(commentId));
			comment.setUpdatedAt(now);
			return comment;
		} catch (NumberFormatException e) {
			throw new CommentNotFoundException(commentId);
		}
	}

	public void deleteCommentOnPost(String commentId) throws SQLException {
		String DELETE_COMMENT_ON_POST_SQL = """
				DELETE FROM comments WHERE
				id = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(DELETE_COMMENT_ON_POST_SQL);
				) {
			pstmt.setInt(1, Integer.parseInt(commentId));
			int updated = pstmt.executeUpdate();
			if (updated == 0) {
				throw new CommentNotFoundException(commentId);
			}

		} catch (NumberFormatException e) {
			throw new CommentNotFoundException(commentId);
		}
	}
}
