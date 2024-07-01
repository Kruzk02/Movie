package com.app.Handler;

import com.app.DTO.UserDTO;
import com.app.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final UserService userService;

    @Autowired
    public UserHandler(UserService userService) {
        this.userService = userService;
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
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
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

    public Mono<ServerResponse> getUserInfo(ServerRequest request){
        String header = request.headers().header("Authorization").getFirst();
        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            return userService.getUserInfo(token)
                    .flatMap(user -> ServerResponse.ok().bodyValue(user))
                    .switchIfEmpty(ServerResponse.notFound().build());
        }
        return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
    }

//    public Mono<ServerResponse> checkAdmin(ServerRequest request) {
//        String username = request.pathVariable("username");
//        return userService.isUserHasAdminRole(username)
//                .flatMap(isAdmin -> ServerResponse.ok().bodyValue("User is an admin"))
//                .onErrorResume(IllegalAccessException.class, e -> ServerResponse.status(HttpStatus.FORBIDDEN).bodyValue(e.getMessage()));
//    }
}
