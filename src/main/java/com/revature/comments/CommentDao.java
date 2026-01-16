package com.revature.comments;

import com.revature.utils.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CommentDao {
	public Comment createCommentOnPost(String postId, Comment comment) throws SQLException {
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
}
