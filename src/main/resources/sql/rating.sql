CREATE TABLE IF NOT EXISTS "rating" (
    id SERIAL PRIMARY KEY,
    rating DECIMAL(3, 1) NOT NULL CHECK (rating >= 0 AND rating <= 10)
);