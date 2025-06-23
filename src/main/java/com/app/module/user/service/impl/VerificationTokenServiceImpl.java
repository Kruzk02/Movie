package com.app.module.user.service.impl;

import com.app.module.user.entity.VerificationToken;
import com.app.exception.sub.InvalidTokenException;
import com.app.module.user.repository.UserRepository;
import com.app.module.user.repository.VerificationTokenRepository;
import com.app.module.user.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenServiceImpl(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public Mono<VerificationToken> generationVerificationToken(Long userId) {
        return Mono.fromCallable(() -> {
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUserId(userId);
            verificationToken.setExpireDate(LocalDate.now().plusDays(1));

            return verificationToken;
        }).flatMap(verificationTokenRepository::save);
    }

    @Override
    public Mono<Void> verifyAccount(String token) {
        return verificationTokenRepository.findByToken(token)
            .filter(verificationToken -> verificationToken != null && verificationToken.getExpireDate().isAfter(LocalDate.now()))
            .switchIfEmpty(Mono.error(new InvalidTokenException("Invalid or expired token")))
            .flatMap(verificationToken -> {
                Long userId = verificationToken.getUserId();
                return userRepository.findById(userId)
                    .flatMap(user -> {
                        user.setEnabled(true);
                        return userRepository.save(user).then(verificationTokenRepository.delete(verificationToken));
                    });
            });
    }
}
