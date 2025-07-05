package com.app.module.movie.repository;

import com.app.module.movie.entity.Genre;
import com.app.module.movie.entity.GenreMoviePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreMovieRepository extends ReactiveCrudRepository<GenreMoviePK,Long> {
    @Query("SELECT * FROM genre_movie gm JOIN genre g ON gm.genre_id = g.id WHERE gm.movie_id = :movie_id")
    Flux<Genre> findAllByMovieId(Long movieId);

    @Query("SELECT g.* FROM genre g JOIN genre_movie gm ON g.id = gm.genre_id JOIN movie m ON gm.movie_id = m.id WHERE m.id = :movieId")
    Flux<Genre> findByMovieId(Long movieId);

    @Query("DELETE FROM genre_movie WHERE movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
