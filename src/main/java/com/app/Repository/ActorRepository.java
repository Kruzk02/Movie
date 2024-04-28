package com.app.Repository;

import com.app.Entity.Actor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ActorRepository extends ReactiveCrudRepository<Actor,Long> {
}
