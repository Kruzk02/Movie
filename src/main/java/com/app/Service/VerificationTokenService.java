package com.app.Service;

import com.app.Entity.VerificationToken;
import reactor.core.publisher.Mono;

public interface VerificationTokenService {
    Mono<VerificationToken> generationVerificationToken(Long userId);
    Mono<Void> verifyAccount(String token);
}
