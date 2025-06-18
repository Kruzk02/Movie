package com.app.module.user.service;

import com.app.module.user.entity.VerificationToken;
import reactor.core.publisher.Mono;

public interface VerificationTokenService {
    Mono<VerificationToken> generationVerificationToken(Long userId);
    Mono<Void> verifyAccount(String token);
}
