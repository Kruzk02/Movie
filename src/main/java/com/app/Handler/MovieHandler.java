package com.app.Handler;

import com.app.DTO.MovieDTO;
import com.app.Entity.Movie;
import com.app.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MovieHandler {

    private final MovieService movieService;

    @Autowired
    public MovieHandler(MovieService movieService) {
        this.movieService = movieService;
    }

    public Mono<ServerResponse> getAllMovie(ServerRequest request){
        Flux<Movie> movies = movieService.getAll();
        return ServerResponse.ok().body(movies,Movie.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<Movie> movies = movieService.getById(Long.valueOf(id));
        return movies.flatMap(movie -> ServerResponse.ok().bodyValue(movies))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Mono<MovieDTO> movieDTOMono = request.bodyToMono(MovieDTO.class);
        return movieDTOMono.flatMap(movieDTO ->
                movieService.save(movieDTO)
                        .flatMap(savedMovie -> ServerResponse.ok().bodyValue(savedMovie))
                        .onErrorResume(error -> ServerResponse.badRequest().bodyValue("Failed to create a Movie: " + error.getMessage())));
    }

    public Mono<ServerResponse> update(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<MovieDTO> movieDTOMono = request.bodyToMono(MovieDTO.class);
        return movieDTOMono.flatMap(movieDTO ->
                movieService.update(Long.valueOf(id),movieDTO)
                        .flatMap(updatedMovie -> ServerResponse.ok().bodyValue(updatedMovie))
                        .onErrorResume(error -> ServerResponse.badRequest().bodyValue("Failed to update movie: " + error.getMessage())));
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String id = request.pathVariable("id");
        return movieService.delete(Long.valueOf(id))
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
