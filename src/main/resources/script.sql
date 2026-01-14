CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP,
	username VARCHAR(255) UNIQUE NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	hashed_password VARCHAR(255) NOT NULL,
	salt VARCHAR(255) NOT NULL
);

TRUNCATE users;

INSERT INTO users (username, email, hashed_password, salt)
VALUES ('test-user', 'test@example.com', 'not actually hashed', 'mmm salt');