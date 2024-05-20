package com.app.Repository;

import com.app.Entity.Actor;
import com.app.Entity.ActorMoviePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ActorMovieRepository extends ReactiveCrudRepository<ActorMoviePK,Long> {
    @Query("SELECT * FROM actor_movie am JOIN actor a ON am.actor_id = a.id WHERE am.movie_id = :movieId")
    Flux<Actor> findAllByMovieId(Long movieId);
}
