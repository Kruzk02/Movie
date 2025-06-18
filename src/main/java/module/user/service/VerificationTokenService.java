package module.user.service;

import module.user.entity.VerificationToken;
import reactor.core.publisher.Mono;

public interface VerificationTokenService {
    Mono<VerificationToken> generationVerificationToken(Long userId);
    Mono<Void> verifyAccount(String token);
}
