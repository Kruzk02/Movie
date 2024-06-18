CREATE TABLE IF NOT EXISTS "director" (
      id SERIAL PRIMARY KEY,
      first_name VARCHAR(100) NOT NULL,
      last_name VARCHAR(100) NOT NULL,
      nationality VARCHAR(100) NOT NULL,
      birth_date DATE NOT NULL
);

CREATE INDEX idx_director_first_name ON director(first_name);
CREATE INDEX idx_director_last_name ON director(last_name);