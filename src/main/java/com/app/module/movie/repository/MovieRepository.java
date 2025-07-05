package com.app.module.movie.repository;

import com.app.module.movie.entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovieRepository extends ReactiveCrudRepository<Movie,Long> {
    @Query("SELECT * FROM movie WHERE genre_id = :genreId")
    Flux<Movie> findByGenreId(Long genreId);

    @Query("SELECT m.* FROM movie m JOIN actor_movie am on m.id = am.movie_id JOIN actor a ON am.actor_id = actor_id WHERE a.id = :id")
    Flux<Movie> findMovieByActorId(Long id);

    @Query("SELECT m.* FROM movie m JOIN director_movie dm ON m.id = dm.movie_id JOIN director d ON dm.director_id = d.id WHERE d.id = :id")
    Flux<Movie> findMovieByDirectorId(Long id);

}
