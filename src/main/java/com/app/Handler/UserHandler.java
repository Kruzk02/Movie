package com.app.Handler;

import com.app.DTO.UserDTO;
import com.app.Service.EmailService;
import com.app.Service.UserService;
import com.app.Service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserHandler {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @Autowired
    public UserHandler(UserService userService, VerificationTokenService verificationTokenService, EmailService emailService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    public Mono<ServerResponse> login(ServerRequest request){
        return request.bodyToMono(UserDTO.class)
            .flatMap(userService::login)
                .flatMap(token -> ServerResponse.ok().bodyValue(token))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> register(ServerRequest request){
        return request.bodyToMono(UserDTO.class)
            .flatMap(userService::register)
                .flatMap(user ->
                    verificationTokenService.generationVerificationToken(user.getId())
                        .flatMap(verificationToken -> emailService.sendVerificationEmail(user.getEmail(),verificationToken.getToken()))
                        .then(Mono.just(user))
                )
                .flatMap(user -> ServerResponse.ok().bodyValue("User registered successfully.\nCheck your email for verification."));
    }

    public Mono<ServerResponse> verifyAccount(ServerRequest request){
        String token = request.queryParam("token").orElseThrow(() -> new IllegalArgumentException("Token is missing"));
        return verificationTokenService.verifyAccount(token)
                .flatMap(ok -> ServerResponse.ok().bodyValue("Account verified successfully."))
                .onErrorResume(e ->
                        ServerResponse.status(400).bodyValue("Verification failed: " + e.getMessage())
                );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(UserDTO.class)
            .flatMap(userDTO -> userService.update(id,userDTO)
                .flatMap(user -> ServerResponse.ok().bodyValue(user)));
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return userService.delete(id)
            .then(ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getUserProfile(ServerRequest request){
        return Mono.just(request.exchange())
                .flatMap(exchange -> {
                    String username = exchange.getAttribute("username");
                    Long userId = exchange.getAttribute("userId");
                    return ServerResponse.ok().bodyValue("Username: " + username + ", User ID: " + userId);
                })
                .switchIfEmpty(ServerResponse.badRequest().bodyValue("User attributes not found"));
    }

//    public Mono<ServerResponse> checkAdmin(ServerRequest request) {
//        String username = request.pathVariable("username");
//        return userService.isUserHasAdminRole(username)
//                .flatMap(isAdmin -> ServerResponse.ok().bodyValue("User is an admin"))
//                .onErrorResume(IllegalAccessException.class, e -> ServerResponse.status(HttpStatus.FORBIDDEN).bodyValue(e.getMessage()));
//    }
}
