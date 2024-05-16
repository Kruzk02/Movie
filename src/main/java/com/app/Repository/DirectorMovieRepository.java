package com.app.Repository;

import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DirectorMovieRepository extends ReactiveCrudRepository<DirectorMoviePK,Long> {
    @Query("SELECT * FROM director_movie dm JOIN director d ON dm.director_id = d.id WHERE dm.movie_id = :movie_id")
    Flux<Director> findAllByMovieId(Long movieId);
}
