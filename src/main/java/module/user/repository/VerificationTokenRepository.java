package module.user.repository;

import module.user.entity.VerificationToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface VerificationTokenRepository extends ReactiveCrudRepository<VerificationToken,Long> {
    Mono<VerificationToken> findByToken(String token);
}
