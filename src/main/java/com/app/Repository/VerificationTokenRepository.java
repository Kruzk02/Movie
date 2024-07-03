package com.app.Repository;

import com.app.Entity.VerificationToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface VerificationTokenRepository extends ReactiveCrudRepository<VerificationToken,Long> {
    Mono<VerificationToken> findByToken(String token);
}
