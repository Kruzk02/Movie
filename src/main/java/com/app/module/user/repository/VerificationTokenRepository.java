package com.app.module.user.repository;

import com.app.module.user.entity.VerificationToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface VerificationTokenRepository extends ReactiveCrudRepository<VerificationToken,Long> {
    Mono<VerificationToken> findByToken(String token);
}
