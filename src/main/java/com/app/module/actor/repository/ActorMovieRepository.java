package com.app.module.actor.repository;

import com.app.module.actor.entity.Actor;
import com.app.module.actor.entity.ActorMoviePK;
import com.app.module.actor.entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorMovieRepository extends ReactiveCrudRepository<ActorMoviePK,Long> {
    @Query("SELECT a.* FROM actor a JOIN actor_movie am ON a.id = am.actor_id JOIN movie m ON am.movie_id = m.id WHERE m.id = :movieId")
    Flux<Actor> findActorByMovieId(Long movieId);

    @Query("DELETE FROM actor_movie am where am.movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
