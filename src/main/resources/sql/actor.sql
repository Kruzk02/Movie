CREATE TABLE IF NOT EXISTS "actor" (
      id SERIAL PRIMARY KEY,
      first_name VARCHAR(100) NOT NULL,
      last_name VARCHAR(100) NOT NULL,
      nationality VARCHAR(100) NOT NULL,
      birth_date DATE NOT NULL
);

CREATE INDEX idx_actor_first_name ON actor(first_name);
CREATE INDEX idx_actor_last_name ON actor(last_name);