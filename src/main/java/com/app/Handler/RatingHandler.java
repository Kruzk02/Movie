package com.app.Handler;

import com.app.Entity.Movie;
import com.app.Entity.Rating;
import com.app.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RatingHandler {

    private final RatingService ratingService;

    @Autowired
    public RatingHandler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<Rating> ratingFlux = ratingService.findAll();
        return ServerResponse.ok().body(ratingFlux, Movie.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Rating> ratingMono = ratingService.findById(id);
        return ratingMono.flatMap(rating -> ServerResponse.ok().bodyValue(rating)
                .switchIfEmpty(ServerResponse.notFound().build()));
    }
}
