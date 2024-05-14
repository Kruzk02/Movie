package com.app.Handler;

import com.app.DTO.MovieRatingDTO;
import com.app.Entity.Movie;
import com.app.Entity.Rating;
import com.app.Service.MovieRatingService;
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
    private final MovieRatingService movieRatingService;

    @Autowired
    public RatingHandler(RatingService ratingService, MovieRatingService movieRatingService) {
        this.ratingService = ratingService;
        this.movieRatingService = movieRatingService;
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

    public Mono<ServerResponse> saveMovieRating(ServerRequest request){
        Mono<MovieRatingDTO> ratingMono = request.bodyToMono(MovieRatingDTO.class);
        return ratingMono.flatMap(movieRatingDTO -> movieRatingService.save (movieRatingDTO)
                .flatMap(savedRating -> ServerResponse.ok().bodyValue(savedRating)));
    }
}
