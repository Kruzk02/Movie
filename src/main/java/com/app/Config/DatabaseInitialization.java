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
                CREATE TABLE IF NOT EXISTS Genre(
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS Rating(
                    id SERIAL PRIMARY KEY,
                    rating DECIMAL(8, 2) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS Director(
                    id SERIAL PRIMARY KEY,
                    first_name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    nationality VARCHAR(255) NOT NULL,
                    birthDate DATE NOT NULL
                );

                CREATE TABLE IF NOT EXISTS Movie(
                    id SERIAL PRIMARY KEY,
                    director_id INTEGER NOT NULL,
                    genre_id INTEGER NOT NULL,
                    rating_id INTEGER NOT NULL,
                    title VARCHAR(255) NOT NULL,
                    release_year DATE NOT NULL,
                    length INTEGER NOT NULL,
                    CONSTRAINT fk_movie_rating FOREIGN KEY (rating_id) REFERENCES Rating(id)
                );

                CREATE TABLE IF NOT EXISTS Actor(
                    id SERIAL PRIMARY KEY,
                    first_name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    nationality VARCHAR(255) NOT NULL,
                    birthDate DATE NOT NULL
                );

                CREATE TABLE IF NOT EXISTS Genre_Movie(
                    id SERIAL PRIMARY KEY,
                    genre_id INTEGER NOT NULL,
                    movie_id INTEGER NOT NULL,
                    CONSTRAINT fk_genre_movie_genre FOREIGN KEY (genre_id) REFERENCES Genre(id),
                    CONSTRAINT fk_genre_movie_movie FOREIGN KEY (movie_id) REFERENCES Movie(id)
                );

                CREATE TABLE IF NOT EXISTS Director_Movie(
                    id SERIAL PRIMARY KEY,
                    director_id INTEGER NOT NULL,
                    movie_id INTEGER NOT NULL,
                    CONSTRAINT fk_director_movie_director FOREIGN KEY (director_id) REFERENCES Director(id),
                    CONSTRAINT fk_director_movie_movie FOREIGN KEY (movie_id) REFERENCES Movie(id)
                );

                CREATE TABLE IF NOT EXISTS Actor_Movie (
                    id SERIAL PRIMARY KEY,
                    actor_id INTEGER NOT NULL,
                    movie_id INTEGER NOT NULL,
                    CONSTRAINT fk_actor_movie_actor FOREIGN KEY (actor_id) REFERENCES Actor(id),
                    CONSTRAINT fk_actor_movie_movie FOREIGN KEY (movie_id) REFERENCES Movie(id)
                );

                CREATE TABLE IF NOT EXISTS Movie_Rating(
                    id SERIAL PRIMARY KEY,
                    movie_id INTEGER NOT NULL,
                    rating_id INTEGER NOT NULL,
                    CONSTRAINT fk_movie_rating_movie FOREIGN KEY (movie_id) REFERENCES Movie(id),
                    CONSTRAINT fk_movie_rating_rating FOREIGN KEY (rating_id) REFERENCES Rating(id)
                );

                CREATE INDEX idx_movie_title ON Movie(title);
                CREATE INDEX idx_movie_release_year ON Movie(release_year);
                CREATE INDEX idx_movie_length ON Movie(length);
                CREATE INDEX idx_genre_name ON Genre(name);
                CREATE INDEX idx_director_first_name ON Director(first_name);
                CREATE INDEX idx_director_last_name ON Director(last_name);
                CREATE INDEX idx_actor_first_name ON Actor(first_name);
                CREATE INDEX idx_actor_last_name ON Actor(last_name);
                CREATE INDEX idx_director_movie_director_id ON Director_Movie(director_id);
                CREATE INDEX idx_director_movie_movie_id ON Director_Movie(movie_id);
                CREATE INDEX idx_actor_movie_actor_id ON Actor_Movie(actor_id);
                CREATE INDEX idx_actor_movie_movie_id ON Actor_Movie(movie_id);
                CREATE INDEX idx_genre_movie_genre_id ON Genre_Movie(genre_id);
                CREATE INDEX idx_genre_movie_movie_id ON Genre_Movie(movie_id);
                CREATE INDEX idx_movie_rating_movie_id ON Movie_Rating(movie_id);
                CREATE INDEX idx_movie_rating_rating_id ON Movie_Rating(rating_id);
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
