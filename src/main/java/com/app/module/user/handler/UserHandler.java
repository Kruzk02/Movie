package com.app.module.user.handler;

import com.app.module.user.dto.MessageResponseDTO;
import com.app.module.user.dto.TokenDTO;
import com.app.module.user.dto.UserDTO;
import com.app.module.user.service.EmailService;
import com.app.module.user.service.UserService;
import com.app.module.user.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
                .flatMap(token -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(new TokenDTO(token)))
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
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(new MessageResponseDTO("User registered successfully. Check your email for verification.")));
    }

    public Mono<ServerResponse> verifyAccount(ServerRequest request){
        String token = request.queryParam("token").orElseThrow(() -> new IllegalArgumentException("Token is missing"));
        return verificationTokenService.verifyAccount(token)
                .flatMap(ok -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(new MessageResponseDTO("Account verified successfully.")))
                .onErrorResume(e ->
                        ServerResponse.status(400).contentType(MediaType.APPLICATION_JSON).bodyValue(new MessageResponseDTO("Verification failed: " + e.getMessage()))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(UserDTO.class)
            .flatMap(userDTO -> userService.update(id,userDTO)
                .flatMap(user -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UserDTO(
                                user.getUsername(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getPhoneNumber(),
                                user.getNationality(),
                                user.getBirthDate()
                        ))));
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
                    String role = exchange.getAttribute("role");
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("Username: " + username + ", User ID: " + userId + ", Roles: " + role);
                })
                .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).bodyValue(new MessageResponseDTO("User attributes not found")));
    }
}
