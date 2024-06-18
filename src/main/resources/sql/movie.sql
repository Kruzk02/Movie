CREATE TABLE IF NOT EXISTS "movie" (
      id SERIAL PRIMARY KEY,
      title VARCHAR(255) NOT NULL,
      release_year DATE NOT NULL,
      length INTEGER NOT NULL
);

CREATE INDEX idx_movie_title ON movie(title);
CREATE INDEX idx_movie_release_year ON movie(release_year);