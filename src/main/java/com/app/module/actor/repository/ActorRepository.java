package com.app.module.actor.repository;

import com.app.module.actor.entity.Actor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ActorRepository extends ReactiveCrudRepository<Actor,Long> {
}
