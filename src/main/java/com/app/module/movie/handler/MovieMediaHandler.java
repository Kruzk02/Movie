package com.app.module.movie.handler;

import com.app.module.movie.dto.MovieMediaDTO;
import com.app.exception.sub.MovieMediaNotFound;
import com.app.module.movie.service.MovieMediaService;
import com.app.module.movie.service.VideoStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
public class MovieMediaHandler {

    private final MovieMediaService movieMediaService;
    private final VideoStreamService videoStreamService;

    @Autowired
    public MovieMediaHandler(MovieMediaService movieMediaService, VideoStreamService videoStreamService) {
        this.movieMediaService = movieMediaService;
        this.videoStreamService = videoStreamService;
    }

    public Mono<ServerResponse> findAllByMovieId(ServerRequest request) {
        Long movieId = Long.valueOf(request.pathVariable("movieId"));
        return movieMediaService.findAllByMovieId(movieId).collectList()
                .flatMap(movieMedia -> ServerResponse.ok().bodyValue(movieMedia));
    }

    public Mono<ServerResponse> findAllByMovieIdAndQuality(ServerRequest request) {
        Long movieId = Long.valueOf(request.pathVariable("movieId"));
        String quality = request.pathVariable("quality");

        return movieMediaService.findAllByMovieIdAndQuality(movieId,quality).collectList()
                .flatMap(movieMedia -> ServerResponse.ok().bodyValue(movieMedia));
    }

    public Mono<ServerResponse> findByMovieIdAndEpisode(ServerRequest request) {
        Long movieId = Long.valueOf(request.pathVariable("movieId"));
        byte episode = Byte.parseByte(request.pathVariable("episode"));

        return movieMediaService.findByMovieIdAndEpisode(movieId,episode)
                .flatMap(movieMedia -> ServerResponse.ok().bodyValue(movieMedia))
                .switchIfEmpty(ServerResponse.notFound().build())
                .switchIfEmpty(Mono.error(new MovieMediaNotFound("Movie media not found with a movie id: " + movieId + " or episode: " + episode)));
    }

    public Mono<ServerResponse> streamVideo(ServerRequest request) {
        String filename = request.pathVariable("filename");
        int lastIndexOfDot = filename.lastIndexOf('.') + 1;
        String extension = "";
        if (lastIndexOfDot != 1) {
            extension = filename.substring(lastIndexOfDot);
        }

        String range = request.headers().asHttpHeaders().getFirst(HttpHeaders.RANGE);
        long start = 0;
        long end = 1024 * 1024;

        final long fileSize = videoStreamService.getFileSize(filename);
        if (range == null) {
            return ServerResponse.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_TYPE,"video/"+extension)
                    .header(HttpHeaders.ACCEPT_RANGES,"bytes")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(end))
                    .header(HttpHeaders.CONTENT_RANGE, "bytes" + " " + start + "-" + end + "/" + fileSize)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize))
                    .bodyValue(videoStreamService.readByteRange(filename,start,end));
        }else {
            String[] ranges = range.split("-");
            start = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                end = Long.parseLong(ranges[1]);
            }else {
                end = start + 1024 * 1024;
            }

            end = Math.min(end,fileSize - 1);
            final byte[] data = videoStreamService.readByteRange(filename,start,end);
            final String contentLength = String.valueOf((end - start) + 1);
            HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
            if (end >= fileSize) {
                httpStatus = HttpStatus.OK;
            }
            return ServerResponse.status(httpStatus)
                    .header(HttpHeaders.CONTENT_TYPE,"video/"+extension)
                    .header(HttpHeaders.ACCEPT_RANGES,"bytes")
                    .header(HttpHeaders.CONTENT_LENGTH,contentLength)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes" + " " + start + "-" + end + "/" + fileSize)
                    .bodyValue(data).log(Arrays.toString(data));
        }
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.multipartData().flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();
            partMap.forEach((key,value) -> System.out.println("Part: " + key + " -> " + value));

            var filePart = (FilePart) partMap.get("video");
            if (filePart == null) throw new IllegalArgumentException("Video file is missing");

            var movieId = Long.valueOf(Objects.requireNonNull(getFormFieldValue(partMap, "movieId")));
            var episodes = Byte.parseByte(Objects.requireNonNull(getFormFieldValue(partMap, "episodes")));
            var duration = LocalTime.parse(Objects.requireNonNull(getFormFieldValue(partMap, "duration")));
            var quality = getFormFieldValue(partMap,"quality");

            if (movieId == null || episodes == 0 || duration == null || quality == null) {
                throw new IllegalArgumentException("One or two field is missing");
            }

            var movieMediaDTO = new MovieMediaDTO(movieId, episodes, duration, quality, filePart);

            return movieMediaService.save(movieMediaDTO)
                    .flatMap(movieMedia -> ServerResponse.status(HttpStatus.CREATED).bodyValue(movieMedia))
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving movie media: " + e.getMessage()));
        });
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return request.multipartData().flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();
            partMap.forEach((key,value) -> System.out.println("Part: " + key + " -> " + value));

            FilePart filePart = (FilePart) partMap.get("video");
            if (filePart == null) throw new IllegalArgumentException("Video file is missing");

            Long movieId = Long.valueOf(Objects.requireNonNull(getFormFieldValue(partMap, "movieId")));
            byte episodes = Byte.parseByte(Objects.requireNonNull(getFormFieldValue(partMap, "episode")));
            LocalTime duration = LocalTime.parse(Objects.requireNonNull(getFormFieldValue(partMap, "duration")));
            String quality = getFormFieldValue(partMap,"quality");

            if (movieId == null || episodes == 0 || duration == null || quality == null) {
                throw new IllegalArgumentException("One or two field is missing");
            }

            Long id = Long.valueOf(request.pathVariable("id"));

            var movieMediaDTO = new MovieMediaDTO(movieId, episodes, duration, quality, filePart);

            return movieMediaService.update(id,movieMediaDTO)
                    .flatMap(movieMedia -> ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue(movieMedia))
                    .switchIfEmpty(ServerResponse.notFound().build())
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving movie media: " + e.getMessage()));
        });
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return movieMediaService.delete(id)
            .then(ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    private String getFormFieldValue(Map<String,Part> partMap, String fieldName) {
        Part part = partMap.get(fieldName);
        if (part instanceof FormFieldPart) {
            return ((FormFieldPart) part).value();
        }
        return null;
    }

}
