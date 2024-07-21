CREATE TABLE IF NOT EXISTS "genre" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE INDEX idx_genre_name ON genre(name);