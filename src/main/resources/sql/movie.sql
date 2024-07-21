CREATE TABLE IF NOT EXISTS "movie" (
      id SERIAL PRIMARY KEY,
      title VARCHAR(100) NOT NULL,
      release_year DATE NOT NULL,
      description TEXT NOT NULL,
      seasons INTEGER,
      poster VARCHAR(255) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_movie_title ON "movie"(title);
CREATE INDEX IF NOT EXISTS idx_movie_release_year ON "movie"(release_year);