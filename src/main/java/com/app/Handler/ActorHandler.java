package com.app.Handler;

import com.app.DTO.ActorDTO;
import com.app.DTO.ActorMovieDTO;
import com.app.Entity.Actor;
import com.app.Entity.Movie;
import com.app.Service.ActorMovieService;
import com.app.Service.ActorService;
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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.app.constants.AppConstants.ACTOR_PHOTO;

@Component
public class ActorHandler {

    private final ActorMovieService actorMovieService;
    private final ActorService actorService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ActorHandler(ActorMovieService actorMovieService, ActorService actorService, ResourceLoader resourceLoader) {
        this.actorMovieService = actorMovieService;
        this.actorService = actorService;
        this.resourceLoader = resourceLoader;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<Actor> actors = actorService.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(actors,Actor.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Actor> actorMono = actorService.findById(id);
        return actorMono.flatMap(actor -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(actor)
            .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> getActorPhoto(ServerRequest request){
        return actorService.findById(Long.valueOf(request.pathVariable("id")))
                .flatMap(actor -> {
                    if (actor.getPhoto() == null || actor.getPhoto().isEmpty()) {
                        return ServerResponse.notFound().build();
                    }

                    Resource resource = resourceLoader.getResource("file:actorPhoto/"+actor.getPhoto());
                    int lastIndexOfDot = resource.getFilename().lastIndexOf('.') + 1;
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
        return request.multipartData().flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();
            partMap.forEach((key, value) -> System.out.println("Part: " + key + " -> " + value));

            FilePart filePart = (FilePart) partMap.get("photo");
            if (filePart == null) {
                return Mono.error(new IllegalArgumentException("Photo file is missing"));
            }

            String firstName = getFormFieldValue(partMap, "firstName");
            String lastName = getFormFieldValue(partMap, "lastName");
            String birthDate = getFormFieldValue(partMap, "birthDate");
            String nationality = getFormFieldValue(partMap, "nationality");

            if (firstName == null || lastName == null || birthDate == null || nationality == null) {
                return Mono.error(new IllegalArgumentException("One or more form fields are missing"));
            }

            int lastIndexOfDot = filePart.filename().lastIndexOf('.');
            String extension = "";
            if (lastIndexOfDot != 1) {
                extension = filePart.filename().substring(lastIndexOfDot);
            }

            String filename = RandomStringUtils.randomAlphabetic(15);
            filename += extension.replaceAll("[(){}]","");

            ActorDTO actorDTO = new ActorDTO();
            actorDTO.setFirstName(firstName);
            actorDTO.setLastName(lastName);
            actorDTO.setBirthDate(LocalDate.parse(birthDate));
            actorDTO.setNationality(nationality);
            actorDTO.setPhoto(filename);

            Path path = Paths.get(ACTOR_PHOTO + filename);

            return actorService.save(actorDTO)
                    .flatMap(actor -> filePart.transferTo(path).then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(actor)))
                    .onErrorResume(e -> ServerResponse.ok().bodyValue("Error saving actor: " + e.getMessage()));
        });
    }

    public Mono<ServerResponse> update(ServerRequest request){
        return request.multipartData().flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();

            partMap.forEach((key, value) -> System.out.println("Part: " + key + " -> " + value));

            FilePart filePart = (FilePart) partMap.get("photo");
            if (filePart == null) {
                return Mono.error(new IllegalArgumentException("Photo file is missing"));
            }

            String firstName = getFormFieldValue(partMap, "firstName");
            String lastName = getFormFieldValue(partMap, "lastName");
            String birthDate = getFormFieldValue(partMap, "birthDate");
            String nationality = getFormFieldValue(partMap, "nationality");

            if (firstName == null || lastName == null || birthDate == null || nationality == null) {
                return Mono.error(new IllegalArgumentException("One or more form fields are missing"));
            }

            int lastIndexOfDot = filePart.filename().lastIndexOf('.');
            String extension = "";
            if (lastIndexOfDot != 1) {
                extension = filePart.filename().substring(lastIndexOfDot);
            }

            String filename = RandomStringUtils.randomAlphabetic(15);
            filename += extension.replaceAll("[(){}]","");

            ActorDTO actorDTO = new ActorDTO();
            actorDTO.setFirstName(firstName);
            actorDTO.setLastName(lastName);
            actorDTO.setBirthDate(LocalDate.parse(birthDate));
            actorDTO.setNationality(nationality);
            actorDTO.setPhoto(filename);

            Long id = Long.valueOf(request.pathVariable("id"));
            Path path = Paths.get(ACTOR_PHOTO+filename);
            return actorService.update(id,actorDTO)
                    .flatMap(actor -> filePart.transferTo(path).then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(actor)))
                    .switchIfEmpty(ServerResponse.notFound().build())
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error update actor: " + e.getMessage()));
        });
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return actorService.delete(id)
            .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build())
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveActorMovie(ServerRequest request){
        return request.bodyToMono(ActorMovieDTO.class)
            .flatMap(actorMovieService::saveActorMovie)
            .flatMap(savedActorMovie -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(savedActorMovie))
            .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving actor: " + error.getMessage()));
    }

    public Mono<ServerResponse> findMovieByActorId(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        Flux<Movie> movieFlux = actorMovieService.findMovieByActor(id);
        return movieFlux.collectList()
                .flatMap(movies -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(movies))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateActorMovie(ServerRequest request){
        return request.bodyToMono(ActorMovieDTO.class)
                .flatMap(actorMovieService::updateActorMovie)
                .flatMap(savedActorMovie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedActorMovie))
                .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error update actor: " + error.getMessage()));
    }

    private String getFormFieldValue(Map<String, Part> partMap, String fieldName) {
        Part part = partMap.get(fieldName);
        if (part instanceof FormFieldPart) {
            return ((FormFieldPart) part).value();
        }
        return null;
    }

}
