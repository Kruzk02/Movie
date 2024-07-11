package com.app.Router;

import com.app.Handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRouter {

    private final UserHandler userHandler;

    @Autowired
    public UserRouter(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> usersRouter(){
        return RouterFunctions
                .route(POST("/users/login"),userHandler::login)
                .andRoute(POST("/users/register"),userHandler::register)
                .andRoute(PUT("/users/{id}"),userHandler::update)
                .andRoute(DELETE("/users/{id}"),userHandler::delete)
                .andRoute(GET("/users/verify"),userHandler::verifyAccount)
                .andRoute(GET("/users/profile"),userHandler::getUserProfile);
    }
}
