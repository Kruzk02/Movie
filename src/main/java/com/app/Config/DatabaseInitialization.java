package com.app.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class DatabaseInitialization {

    @Autowired
    private DatabaseClient databaseClient;

    @PostConstruct
    public void initializeDatabase(){
        String sql = """
                CREATE TABLE IF NOT EXISTS genre (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS rating (
                    id SERIAL PRIMARY KEY,
                    rating DECIMAL(3, 1) NOT NULL CHECK (rating >= 0 AND rating <= 10)
                );

                CREATE TABLE IF NOT EXISTS director (
                    id SERIAL PRIMARY KEY,
                    first_name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    nationality VARCHAR(255) NOT NULL,
                    birth_date DATE NOT NULL
                );

                CREATE TABLE IF NOT EXISTS movie (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    release_year DATE NOT NULL,
                    length INTEGER NOT NULL
                );

                CREATE TABLE IF NOT EXISTS actor (
                    id SERIAL PRIMARY KEY,
                    first_name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    nationality VARCHAR(255) NOT NULL,
                    birth_date DATE NOT NULL
                );

                CREATE TABLE IF NOT EXISTS genre_movie (
                    genre_id INTEGER NOT NULL,
                    movie_id INTEGER NOT NULL,
                    PRIMARY KEY (genre_id, movie_id),
                    FOREIGN KEY (genre_id) REFERENCES genre(id),
                    FOREIGN KEY (movie_id) REFERENCES movie(id)
                );

                CREATE TABLE IF NOT EXISTS actor_movie (
                    actor_id INTEGER NOT NULL,
                    movie_id INTEGER NOT NULL,
                    PRIMARY KEY (actor_id, movie_id),
                    FOREIGN KEY (actor_id) REFERENCES actor(id),
                    FOREIGN KEY (movie_id) REFERENCES movie(id)
                );

                CREATE TABLE IF NOT EXISTS director_movie (
                    director_id INTEGER NOT NULL,
                    movie_id INTEGER NOT NULL,
                    PRIMARY KEY (director_id, movie_id),
                    FOREIGN KEY (director_id) REFERENCES director(id),
                    FOREIGN KEY (movie_id) REFERENCES movie(id)
                );

                CREATE TABLE IF NOT EXISTS movie_rating (
                    movie_id INTEGER NOT NULL,
                    rating_id INTEGER NOT NULL,
                    PRIMARY KEY (movie_id, rating_id),
                    FOREIGN KEY (movie_id) REFERENCES movie(id),
                    FOREIGN KEY (rating_id) REFERENCES rating(id)
                );

                CREATE INDEX idx_movie_title ON movie(title);
                CREATE INDEX idx_movie_release_year ON movie(release_year);
                CREATE INDEX idx_movie_length ON movie(length);
                CREATE INDEX idx_genre_name ON genre(name);
                CREATE INDEX idx_director_first_name ON director(first_name);
                CREATE INDEX idx_director_last_name ON director(last_name);
                CREATE INDEX idx_actor_first_name ON actor(first_name);
                CREATE INDEX idx_actor_last_name ON actor(last_name);
                CREATE INDEX idx_genre_movie_genre_id ON genre_movie(genre_id);
                CREATE INDEX idx_genre_movie_movie_id ON genre_movie(movie_id);
                CREATE INDEX idx_actor_movie_actor_id ON actor_movie(actor_id);
                CREATE INDEX idx_actor_movie_movie_id ON actor_movie(movie_id);
                CREATE INDEX idx_movie_rating_movie_id ON movie_rating(movie_id);
                CREATE INDEX idx_movie_rating_rating_id ON movie_rating(rating_id);
                """;
        databaseClient.sql(sql).fetch().rowsUpdated().block();
    }

    @PostConstruct
    public void insertDataToTable(){
        String sql = """
                INSERT INTO Genre (name) VALUES
                ('Action'),
                ('Adventure'),
                ('Comedy'),
                ('Drama'),
                ('Romance'),
                ('Horror'),
                ('Science_Fiction'),
                ('Fantasy'),
                ('Thriller'),
                ('Mystery'),
                ('Crime'),
                ('Animation'),
                ('Documentary'),
                ('Historical'),
                ('Biographical'),
                ('Musical'),
                ('War'),
                ('Western'),
                ('Spy_Espionage'),
                ('Supernatural'),
                ('Psychological'),
                ('Family'),
                ('Sports'),
                ('Disaster'),
                ('Romance_Comedy'),
                ('Musical_Comedy'),
                ('Martial_Arts'),
                ('Noir'),
                ('Satire'),
                ('Mockumentary');
                INSERT INTO Rating (rating) VALUES
                (1.0),
                (2.0),
                (3.0),
                (4.0),
                (5.0);
                """;
        databaseClient.sql(sql).fetch().rowsUpdated().block();
    }
}
