package com.app.module.director.handler;

import com.app.module.director.dto.DirectorDTO;
import com.app.module.director.dto.DirectorMovieDTO;
import com.app.module.director.entity.Director;
import com.app.module.movie.entity.Movie;
import com.app.module.director.service.DirectorMovieService;
import com.app.module.director.service.DirectorService;
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
import static com.app.constants.AppConstants.DIRECTOR_PHOTO;

@Component
public class DirectorHandler {

    private final DirectorMovieService directorMovieService;
    private final DirectorService directorService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public DirectorHandler(DirectorMovieService directorMovieService, DirectorService directorService, ResourceLoader resourceLoader) {
        this.directorMovieService = directorMovieService;
        this.directorService = directorService;
        this.resourceLoader = resourceLoader;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<Director> directors = directorService.findAll();
        return ServerResponse.ok().body(directors, Director.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Director> directorMono = directorService.findById(id);
        return directorMono.flatMap(director -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(director)
                .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> getDirectorPhoto(ServerRequest request) {
        return directorService.findById(Long.valueOf(request.pathVariable("id")))
            .flatMap(director -> {
                if (director.getPhoto() == null || director.getPhoto().isEmpty()) {
                    return ServerResponse.notFound().build();
                }
                Resource resource = resourceLoader.getResource("file:directorPhoto/" +director.getPhoto());
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

    public Mono<ServerResponse> create(ServerRequest request){
        return request.multipartData().flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();

            FilePart filePart = (FilePart) partMap.get("photo");
            if (filePart == null) {
                return Mono.error(new IllegalArgumentException("Photo file is missing"));
            }

            String firstName = getFormFieldValue(partMap, "firstName");
            String lastName = getFormFieldValue(partMap, "lastName");
            String birthDate = getFormFieldValue(partMap, "birthDate");
            String nationality = getFormFieldValue(partMap, "nationality");

            if (firstName == null || lastName == null || birthDate == null || nationality == null) {
                return Mono.error(new IllegalArgumentException("One or two form fields are missing"));
            }
            int lastIndexOfDot = filePart.filename().lastIndexOf('.');
            String extension = "";
            if (lastIndexOfDot != 1) {
                extension = filePart.filename().substring(lastIndexOfDot);
            }

            String filename = RandomStringUtils.randomAlphabetic(15);
            filename += extension.replaceAll("[(){}]", "");


            DirectorDTO directorDTO = new DirectorDTO();
            directorDTO.setFirstName(firstName);
            directorDTO.setLastName(lastName);
            directorDTO.setNationality(nationality);
            directorDTO.setBirthDate(LocalDate.parse(birthDate));
            directorDTO.setPhoto(filename);

            Path path = Paths.get(DIRECTOR_PHOTO + filename);
            return directorService.save(directorDTO)
                    .flatMap(director -> filePart.transferTo(path)
                            .then(ServerResponse.ok().bodyValue(director)))
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving director: " + e.getMessage()));
        });
    }

    public Mono<ServerResponse> update(ServerRequest request){
        return request.multipartData().flatMap(parts -> {
            Map<String,Part> partMap = parts.toSingleValueMap();

            FilePart filePart = (FilePart) partMap.get("photo");
            if (filePart == null) {
                return Mono.error(new IllegalArgumentException("Photo file is missing"));
            }

            String firstName = getFormFieldValue(partMap, "firstName");
            String lastName = getFormFieldValue(partMap, "lastName");
            String birthDate = getFormFieldValue(partMap, "birthDate");
            String nationality = getFormFieldValue(partMap, "nationality");

            if (firstName == null || lastName == null || birthDate == null || nationality == null) {
                return Mono.error(new IllegalArgumentException("One or two form fields are missing"));
            }

            int lastIndexOfDot = filePart.filename().lastIndexOf('.');
            String extension = "";
            if (lastIndexOfDot != 1) {
                extension = filePart.filename().substring(lastIndexOfDot);
            }

            String filename = RandomStringUtils.randomAlphabetic(15);
            filename += extension.replaceAll("[(){}]", "");

            DirectorDTO directorDTO = new DirectorDTO();
            directorDTO.setFirstName(firstName);
            directorDTO.setLastName(lastName);
            directorDTO.setNationality(nationality);
            directorDTO.setBirthDate(LocalDate.parse(birthDate));
            directorDTO.setPhoto(filename);

            Path path = Paths.get(DIRECTOR_PHOTO + filename);
            Long id = Long.valueOf(request.pathVariable("id"));
            return directorService.update(id, directorDTO)
                    .flatMap(director ->
                            filePart.transferTo(path)
                                    .then(ServerResponse.ok().bodyValue(director))
                    )
                    .switchIfEmpty(ServerResponse.notFound().build())
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error updating director: " + e.getMessage()));
        });
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return directorService.delete(id)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveDirectorMovie(ServerRequest request){
        return request.bodyToMono(DirectorMovieDTO.class)
            .flatMap(directorMovieService::saveDirectorMovie)
            .flatMap(savedDirectorMovie -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(savedDirectorMovie))
            .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving director: " + error.getMessage()));
    }

    public Mono<ServerResponse> updateDirectorMovie(ServerRequest request){
        return request.bodyToMono(DirectorMovieDTO.class)
                .flatMap(directorMovieService::updateDirectorMovie)
                .flatMap(savedDirectorMovie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedDirectorMovie))
                .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error update director: " + error.getMessage()));
    }

    public Mono<ServerResponse> findMovieByDirectorId(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Flux<Movie> movieFlux = directorMovieService.findMovieByDirector(id);
        return movieFlux.collectList()
            .flatMap(movies -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movies))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    private String getFormFieldValue(Map<String,Part> partMap,String fieldName){
        Part part = partMap.get(fieldName);
        if (part instanceof FormFieldPart) {
            return ((FormFieldPart) part).value();
        }
        return null;
    }

}
