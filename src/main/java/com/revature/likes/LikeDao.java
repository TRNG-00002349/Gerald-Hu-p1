package com.revature.likes;

import com.revature.posts.PostNotFoundException;
import com.revature.utils.DataSource;

import java.sql.SQLException;

public class LikeDao {
	public void createLikeOnPost(Integer userId, Integer postId) throws SQLException {
		String CREATE_LIKE_ON_POST_SQL = """
				insert into likes (user_id, post_id)
				values (?, ?)
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(CREATE_LIKE_ON_POST_SQL);
				) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		}
	}

	public void deleteLikeOnPost(Integer userId, Integer postId) throws SQLException {
		String CREATE_LIKE_ON_POST_SQL = """
				delete from likes
				where
				user_id = ? and post_id = ?
				""";
		try (
				var conn = DataSource.getConnection();
				var pstmt = conn.prepareStatement(CREATE_LIKE_ON_POST_SQL);
		) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		}
	}
}
