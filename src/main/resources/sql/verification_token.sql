CREATE TABLE IF NOT EXISTS "verification_token"(
    id SERIAL PRIMARY KEY,
    token VARCHAR(100),
    user_id INTEGER,
    expire_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);