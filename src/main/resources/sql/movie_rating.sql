CREATE TABLE IF NOT EXISTS movie_rating (
      movie_id INTEGER,
      rating_id INTEGER,
      PRIMARY KEY (movie_id, rating_id),
      FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE,
      FOREIGN KEY (rating_id) REFERENCES rating(id)
);
CREATE INDEX idx_movie_rating_movie_id ON movie_rating(movie_id);
CREATE INDEX idx_movie_rating_rating_id ON movie_rating(rating_id);