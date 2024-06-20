package com.app.Repository;

import com.app.Entity.Privilege;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PrivilegeRepository extends ReactiveCrudRepository<Privilege,Long> {
    Mono<Privilege> findByName(String name);
}
