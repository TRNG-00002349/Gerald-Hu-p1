TRUNCATE users, posts;

CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP,
	username VARCHAR(255) UNIQUE NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	hashed_password VARCHAR(255) NOT NULL,
	salt VARCHAR(255) NOT NULL
);

ALTER SEQUENCE users_id_seq RESTART WITH 1;

INSERT INTO users (username, email, hashed_password, salt)
VALUES ('test-user', 'test@example.com', 'not actually hashed', 'mmm salt');

CREATE TABLE IF NOT EXISTS posts (
	id SERIAL PRIMARY KEY,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP,
	content VARCHAR,
	author_id INTEGER references users (id)
);

ALTER SEQUENCE posts_id_seq RESTART WITH 1;

INSERT INTO posts (content, author_id)
VALUES ('test post please ignore', 1);