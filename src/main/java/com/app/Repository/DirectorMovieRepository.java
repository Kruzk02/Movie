package com.app.Repository;

import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import module.movie.entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorMovieRepository extends ReactiveCrudRepository<DirectorMoviePK,Long> {
    @Query("SELECT d.* FROM director d JOIN director_movie dm ON d.id = dm.director_id JOIN movie m ON dm.movie_id = m.id WHERE m.id = :movieId")
    Flux<Director> findDirectorByMovieId(Long movieId);

    @Query("SELECT m.* FROM movie m JOIN director_movie dm ON m.id = dm.movie_id JOIN director d ON dm.director_id = d.id WHERE d.id = :id")
    Flux<Movie> findMovieByDirectorId(Long id);

    @Query("DELETE FROM director_movie dm WHERE dm.movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
