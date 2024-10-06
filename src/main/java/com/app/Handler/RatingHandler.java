package com.app.Handler;

import com.app.DTO.RatingDTO;
import com.app.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RatingHandler {

    private final RatingService ratingService;

    @Autowired
    public RatingHandler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public Mono<ServerResponse> findAvgRatingByMovieId(ServerRequest request){
        Long movieId = Long.valueOf(request.pathVariable("movieId"));
        return ratingService.findAvgRatingByMovieId(movieId)
                .flatMap(avgRating -> ServerResponse.ok().bodyValue(avgRating))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findRatingByMovieIdAndUserId(ServerRequest request){
        Long movieId = Long.valueOf(request.pathVariable("movieId"));
        Long userId = request.exchange().getAttribute("userId");
        return ratingService.findRatingByMovieIdAndUserId(movieId,userId)
                .flatMap(rating -> ServerResponse.ok().bodyValue(rating))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request){
        Long userId = request.exchange().getAttribute("userId");
        Mono<RatingDTO> ratingDTOMono = request.bodyToMono(RatingDTO.class);
        return ratingDTOMono.flatMap(ratingDTO -> ratingService.save(ratingDTO,userId)
                    .flatMap(rating -> ServerResponse.ok().bodyValue(rating))
        );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Long userId = request.exchange().getAttribute("userId");
        Mono<RatingDTO> ratingDTOMono = request.bodyToMono(RatingDTO.class);
        return ratingDTOMono.flatMap(ratingDTO -> ratingService.update(id,ratingDTO,userId)
                .flatMap(rating -> ServerResponse.ok().bodyValue(rating)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Long userId = request.exchange().getAttribute("userId");
        return ratingService.delete(id,userId)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
