package com.app.Handler;

import com.app.DTO.MovieDTO;
import com.app.Entity.Movie;
import com.app.Service.MovieService;
import net.sf.jsqlparser.schema.Server;
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

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<Movie> movieFlux = movieService.findAll();
        return ServerResponse.ok().body(movieFlux, Movie.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Movie> movieMono = movieService.findById(id);
        return movieMono.flatMap(movie -> ServerResponse.ok().bodyValue(movie)
                .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> save(ServerRequest request){
        Mono<MovieDTO> movieDTOMono = request.bodyToMono(MovieDTO.class);
        return movieDTOMono.flatMap(movieDTO -> movieService.save(movieDTO)
                .flatMap(savedMovie -> ServerResponse.ok().bodyValue(savedMovie)));
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<MovieDTO> movieDTOMono = request.bodyToMono(MovieDTO.class);
        return movieDTOMono.flatMap(movieDTO -> movieService.update(id,movieDTO)
                .flatMap(savedMovie -> ServerResponse.ok().bodyValue(savedMovie))
                .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return movieService.delete(id)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
