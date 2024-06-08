package com.app.Repository;

import com.app.Entity.Actor;
import com.app.Entity.ActorMoviePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ActorMovieRepository extends ReactiveCrudRepository<ActorMoviePK,Long> {
    @Query("SELECT a.* FROM actor a JOIN actor_movie am ON a.id = am.actor_id JOIN movie m ON am.movie_id = m.id WHERE m.id = :movieId")
    Flux<Actor> findActorByMovieId(Long movieId);
}
