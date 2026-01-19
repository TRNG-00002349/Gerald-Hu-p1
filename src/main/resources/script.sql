CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP,
	username VARCHAR(255) UNIQUE NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	hashed_password VARCHAR(255) NOT NULL,
	deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS posts (
	id SERIAL PRIMARY KEY,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP,
	content VARCHAR NOT NULL,
	author_id INTEGER REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
	id SERIAL PRIMARY KEY,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP,
	content VARCHAR NOT NULL,
	author_id INTEGER REFERENCES users (id),
	post_id INTEGER
		REFERENCES posts (id)
		ON DELETE CASCADE
);


TRUNCATE users, posts, comments;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE posts_id_seq RESTART WITH 1;
ALTER SEQUENCE comments_id_seq RESTART WITH 1;

INSERT INTO users (username, email, hashed_password)
VALUES ('test-user', 'test@example.com', 'not actually hashed');

INSERT INTO users (username, email, hashed_password, deleted)
VALUES ('test-user-deleted', 'deleted@example.com', 'not hashed', TRUE);

INSERT INTO posts (content, author_id)
VALUES ('test post please ignore', 1);

INSERT INTO posts (content, author_id)
VALUES ('test post by deleted author', 2);

INSERT INTO comments (content, author_id, post_id)
VALUES ('comment by active user on post by active user', 1, 1);

INSERT INTO comments (content, author_id, post_id)
VALUES ('comment by deleted user on post', 2, 1);

INSERT INTO comments (content, author_id, post_id)
VALUES ('comment by active user on post by deleted user', 1, 2);

INSERT INTO comments (content, author_id, post_id)
VALUES ('comment by deleted user on post by deleted user', 2, 2);

INSERT INTO comments (content, author_id, post_id)
VALUES ('same user can leave multiple comments', 1, 1);
