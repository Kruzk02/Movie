package com.app.module.genre.repository;

import com.app.module.genre.entity.Genre;
import com.app.module.genre.entity.GenreMoviePK;
import com.app.module.movie.entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreMovieRepository extends ReactiveCrudRepository<GenreMoviePK,Long> {
    @Query("SELECT * FROM genre_movie gm JOIN genre g ON gm.genre_id = g.id WHERE gm.movie_id = :movie_id")
    Flux<Genre> findAllByMovieId(Long movieId);

    @Query("SELECT g.* FROM genre g JOIN genre_movie gm ON g.id = gm.genre_id JOIN movie m ON gm.movie_id = m.id WHERE m.id = :movieId")
    Flux<Genre> findByMovieId(Long movieId);

    @Query("SELECT m.* FROM movie m JOIN genre_movie gm ON m.id = gm.movie_id JOIN genre g ON gm.genre_id = g.id WHERE g.id = :id")
    Flux<Movie> findMovieByGenreId(Long id);

    @Query("DELETE FROM genre_movie WHERE movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
