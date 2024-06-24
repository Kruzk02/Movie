package com.app.Repository;

import com.app.Entity.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveCrudRepository<Role,Long> {
    @Query("SELECT * FROM role WHERE name = :name")
    Mono<Role> findByName(String name);
}
