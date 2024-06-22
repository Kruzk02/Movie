package com.app.Repository;

import com.app.Entity.Privilege;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PrivilegeRepository extends ReactiveCrudRepository<Privilege,Long> {
    @Query("SELECT * FROM privilege WHERE name = :name")
    Mono<Privilege> findByName(String name);
}
