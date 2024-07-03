CREATE TABLE IF NOT EXISTS "verification_token"(
    id SERIAL PRIMARY KEY,
    token varchar(100),
    userId INTEGER,
    expireDate DATE NOT NULL
);