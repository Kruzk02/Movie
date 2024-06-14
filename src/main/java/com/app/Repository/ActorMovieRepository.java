package com.app.Repository;

import com.app.Entity.Actor;
import com.app.Entity.ActorMoviePK;
import com.app.Entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorMovieRepository extends ReactiveCrudRepository<ActorMoviePK,Long> {
    @Query("SELECT a.* FROM actor a JOIN actor_movie am ON a.id = am.actor_id JOIN movie m ON am.movie_id = m.id WHERE m.id = :movieId")
    Flux<Actor> findActorByMovieId(Long movieId);

    @Query("SELECT m.* FROM movie m JOIN actor_movie am on m.id = am.movie_id JOIN actor a ON am.actor_id = actor_id WHERE a.id = :id")
    Flux<Movie> findMovieByActorId(Long id);

    @Query("DELETE FROM actor_movie am where am.movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
