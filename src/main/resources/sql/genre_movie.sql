CREATE TABLE IF NOT EXISTS genre_movie (
      genre_id INTEGER,
      movie_id INTEGER,
      PRIMARY KEY (genre_id, movie_id),
      FOREIGN KEY (genre_id) REFERENCES genre(id),
      FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
);
CREATE INDEX idx_genre_movie_genre_id ON genre_movie(genre_id);
CREATE INDEX idx_genre_movie_movie_id ON genre_movie(movie_id);