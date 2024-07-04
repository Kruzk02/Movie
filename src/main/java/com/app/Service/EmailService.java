package com.app.Service;

import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Void> sendVerificationEmail(String to,String token);
}
