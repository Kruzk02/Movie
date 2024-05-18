package com.app.Repository;

import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import com.app.Entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface GenreMovieRepository extends ReactiveCrudRepository<GenreMoviePK,Long> {
    @Query("SELECT * FROM genre_movie gm JOIN genre g ON gm.genre_id = g.id WHERE gm.movie_id = :movie_id")
    Flux<Genre> findAllByMovieId(Long movieId);

    @Query("SELECT * FROM genre_movie gm JOIN movie m ON gm.movie_id = m.id WHERE gm.genre_id = :genre_id")
    Flux<Movie> findAllMovieByGenreId(Long genreId);
}
