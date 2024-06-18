CREATE TABLE IF NOT EXISTS director_movie (
      director_id INTEGER,
      movie_id INTEGER,
      PRIMARY KEY (director_id, movie_id),
      FOREIGN KEY (director_id) REFERENCES director(id),
      FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
);

CREATE INDEX idx_director_movie_director_id ON director_movie(director_id);
CREATE INDEX idx_director_movie_movie_id ON director_movie(movie_id);