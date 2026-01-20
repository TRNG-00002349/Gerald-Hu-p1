package com.revature.likes;

import java.sql.SQLException;

public class LikeService {

	private LikeDao likeDao;

	public LikeService(LikeDao likeDao) {
		this.likeDao = likeDao;
	}

	public void createLikeOnPost(Integer userId, Integer postId) throws SQLException {
		likeDao.createLikeOnPost(userId, postId);
	}
}
