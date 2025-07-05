package com.app.module.movie.handler;

import com.app.module.movie.dto.GenreDTO;
import com.app.module.movie.entity.Genre;
import com.app.module.movie.entity.Movie;
import com.app.module.movie.service.GenreMovieService;
import com.app.module.movie.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GenreHandler {

    private final GenreMovieService genreMovieService;
    private final GenreService genreService;

    @Autowired
    public GenreHandler(GenreMovieService genreMovieService, GenreService genreService) {
        this.genreMovieService = genreMovieService;
        this.genreService = genreService;
    }

    /**
     * Retrieves all genres.
     *
     * @param request The server request.
     * @return A Mono emitting the ServerResponse containing a list of genres.
     */
    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreService.findAll(),Genre.class);
    }

    /**
     * Retrieves a genre by id
     * @param request The server request containing the genre id.
     * @return A Mono emitting the ServerResponse containing the genre or a 404 Not Found if not found.
     */
    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Genre> genreMono = genreService.findById(id);
        return genreMono.flatMap(genre -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(genre))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Saves a new movie genre.
     *
     * @param request The server request containing the GenreDTO in the request body.
     * @return A Mono emitting the ServerResponse containing the saved genre movie or an error message in case of failure.
     */
    public Mono<ServerResponse> saveGenreMovie(ServerRequest request){
         return request.bodyToMono(GenreDTO.class)
                 .flatMap(genreMovieService::saveGenreMovie)
                 .flatMap(savedGenreMovie -> ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .bodyValue(savedGenreMovie))
                 .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .bodyValue("Error saving genre: " + error.getMessage()));
    }

    public Mono<ServerResponse> updateGenreMovie(ServerRequest request){
         return request.bodyToMono(GenreDTO.class)
                 .flatMap(genreMovieService::updateGenreMovie)
                 .flatMap(savedGenreMovie -> ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .bodyValue(savedGenreMovie))
                 .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .bodyValue("Error update genre: " + error.getMessage()));
    }

}
