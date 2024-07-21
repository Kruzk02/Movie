CREATE TABLE IF NOT EXISTS "movie_media" (
      id SERIAL PRIMARY KEY,
      movie_id INTEGER,
      file_path VARCHAR(255) NOT NULL,
      episodes INTEGER,
      duration TIME,
      quality VARCHAR(50),
      CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES "movie"(id)
      ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_movie_media_movie_id ON "movie_media"(movie_id);