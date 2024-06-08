package com.app.Repository;

import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DirectorMovieRepository extends ReactiveCrudRepository<DirectorMoviePK,Long> {
    @Query("SELECT d.* FROM director d JOIN director_movie dm ON d.id = dm.director_id JOIN movie m ON dm.movie_id = m.id WHERE m.id = :movieId")
    Flux<Director> findDirectorByMovieId(Long movieId);
}
