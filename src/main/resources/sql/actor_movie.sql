CREATE TABLE IF NOT EXISTS actor_movie (
      actor_id INTEGER,
      movie_id INTEGER,
      PRIMARY KEY (actor_id, movie_id),
      FOREIGN KEY (actor_id) REFERENCES actor(id),
      FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
);

CREATE INDEX idx_actor_movie_actor_id ON actor_movie(actor_id);
CREATE INDEX idx_actor_movie_movie_id ON actor_movie(movie_id);