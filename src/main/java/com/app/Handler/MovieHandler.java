package com.app.Handler;

import com.app.DTO.MovieDTO;
import com.app.Entity.Movie;
import com.app.Expection.MovieNotFound;
import com.app.Service.MovieService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.app.constants.AppConstants.MOVIE_POSTER;

@Component
public class MovieHandler {

    private final MovieService movieService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public MovieHandler(MovieService movieService, ResourceLoader resourceLoader) {
        this.movieService = movieService;
        this.resourceLoader = resourceLoader;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<Movie> movieFlux = movieService.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(movieFlux, Movie.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Movie> movieMono = movieService.findById(id);
        return movieMono.flatMap(movie -> ServerResponse.ok().bodyValue(movie))
                .switchIfEmpty(ServerResponse.notFound().build()) ;
    }

    public Mono<ServerResponse> getMoviePoster(ServerRequest request) {
        return movieService.findById(Long.valueOf(request.pathVariable("id")))
                .flatMap(movie -> {
                    if (movie.getPoster() == null || movie.getPoster().isEmpty()) {
                        return ServerResponse.notFound().build();
                    }
                    Resource resource = resourceLoader.getResource("file:moviePoster/"+movie.getPoster());
                    int lastIndexOfDot = Objects.requireNonNull(resource.getFilename()).lastIndexOf('.') + 1;
                    String extension = "";
                    if (lastIndexOfDot != 1) {
                        extension = resource.getFilename().substring(lastIndexOfDot);
                    }
                    return ServerResponse.ok()
                            .header(HttpHeaders.CONTENT_TYPE,"image/"+extension)
                            .bodyValue(resource);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return checkRoleAndProcess(request, role -> request.multipartData().flatMap(parts ->{
            Map<String, Part> partMap = parts.toSingleValueMap();
            partMap.forEach((key, value) -> System.out.println("Part: " + key + " -> " + value));

            FilePart filePart = (FilePart) partMap.get("poster");
            if (filePart == null) {
                return Mono.error(new IllegalArgumentException("Poster file is missing"));
            }

            String title = getFormFieldValue(partMap,"title");
            String release_year = getFormFieldValue(partMap,"release_year");
            String description = getFormFieldValue(partMap,"description");
            String seasons = getFormFieldValue(partMap,"seasons");

            if (title == null || release_year == null || description == null || seasons == null) {
                return Mono.error(new IllegalArgumentException("One or more form fields are missing"));
            }

            int lastIndexOfDot = filePart.filename().lastIndexOf('.');
            String extension = "";
            if (lastIndexOfDot != 1) {
                extension = filePart.filename().substring(lastIndexOfDot);
            }
            String filename = RandomStringUtils.randomAlphabetic(15);
            filename = filename + extension.replaceAll("[(){}]", "");

            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setTitle(title);
            movieDTO.setDescription(description);
            movieDTO.setRelease_year(LocalDate.parse(release_year));
            movieDTO.setSeasons(Byte.parseByte(seasons));
            movieDTO.setPoster(filename);

            Path path = Paths.get(MOVIE_POSTER + filename);

            return movieService.save(movieDTO)
                    .flatMap(movie -> filePart.transferTo(path).then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(movie)))
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving movie: " + e.getMessage()));
        }));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return checkRoleAndProcess(request, role -> request.multipartData().flatMap(parts -> {
            Map<String,Part> partMap = parts.toSingleValueMap();
            partMap.forEach((key, value) -> System.out.println("Part: " + key + " -> " + value));

            FilePart filePart = (FilePart) partMap.get("poster");
            if (filePart == null) {
                return Mono.error(new IllegalArgumentException("Poster file is missing"));
            }

            String title = getFormFieldValue(partMap,"title");
            String release_year = getFormFieldValue(partMap,"release_year");
            String description = getFormFieldValue(partMap,"description");
            String seasons = getFormFieldValue(partMap,"seasons");

            if (title == null || release_year == null || description == null || seasons == null) {
                return Mono.error(new IllegalArgumentException("One or more form fields are missing"));
            }

            int lastIndexOfDot = filePart.filename().lastIndexOf('.');
            String extension = "";
            if (lastIndexOfDot != 1) {
                extension = filePart.filename().substring(lastIndexOfDot);
            }
            String filename = RandomStringUtils.randomAlphabetic(15);
            filename = filename + extension.replaceAll("[(){}]", "");

            Long id = Long.valueOf(request.pathVariable("id"));

            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setTitle(title);
            movieDTO.setDescription(description);
            movieDTO.setRelease_year(LocalDate.parse(release_year));
            movieDTO.setSeasons(Byte.parseByte(seasons));
            movieDTO.setPoster(filename);

            Path path = Paths.get(MOVIE_POSTER + filename);

            return movieService.update(id,movieDTO)
                    .flatMap(movie -> filePart.transferTo(path).then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(movie)))
                    .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error update movie: " + e.getMessage()));
        }));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return checkRoleAndProcess(request, role -> {
            Long id = Long.valueOf(request.pathVariable("id"));
            return movieService.delete(id)
                    .then(ServerResponse.ok().build())
                    .switchIfEmpty(ServerResponse.notFound().build());
        });
    }

    private String getFormFieldValue(Map<String,Part> partMap,String fieldName) {
        Part part = partMap.get(fieldName);
        if (part instanceof FormFieldPart) {
            return ((FormFieldPart) part).value();
        }
        return null;
    }

    private Mono<ServerResponse> checkRoleAndProcess(ServerRequest request, Function<String, Mono<ServerResponse>> processFunction) {
        String role = request.exchange().getAttribute("role");

        if (Objects.isNull(role)) {
            return ServerResponse.badRequest().bodyValue("User attributes not found");
        }

        if ("ROLE_USER".equals(role)) {
            return ServerResponse.status(403).build();
        }

        return processFunction.apply(role);
    }
}
