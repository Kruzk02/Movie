CREATE TABLE IF NOT EXISTS "rating" (
    id SERIAL PRIMARY KEY,
    user_id int,
    movie_id int,
    rating DECIMAL(3, 1) NOT NULL CHECK (rating >= 0 AND rating <= 5),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);