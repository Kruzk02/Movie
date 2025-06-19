package com.app.module.user.service;

import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Void> sendVerificationEmail(String to,String token);
}
