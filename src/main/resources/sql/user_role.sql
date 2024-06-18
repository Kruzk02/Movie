CREATE TABLE IF NOT EXISTS "user_role"(
    user_id INTEGER,
    role_id INTEGER,
    PRIMARY KEY (user_id,role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id)
);