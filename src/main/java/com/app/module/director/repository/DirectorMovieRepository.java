package com.app.module.director.repository;

import com.app.module.director.entity.Director;
import com.app.module.director.entity.DirectorMoviePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorMovieRepository extends ReactiveCrudRepository<DirectorMoviePK,Long> {
    @Query("SELECT d.* FROM director d JOIN director_movie dm ON d.id = dm.director_id JOIN movie m ON dm.movie_id = m.id WHERE m.id = :movieId")
    Flux<Director> findDirectorByMovieId(Long movieId);

    @Query("DELETE FROM director_movie dm WHERE dm.movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
