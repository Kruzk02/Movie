package com.app.module.user.repository;

import com.app.module.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,Long> {
    Mono<User> findByUsername(String username);

    Mono<User> findByEmail(String email);
}
