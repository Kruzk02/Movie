package com.app.messaging.processor.sub;

import com.app.Entity.Movie;
import com.app.Expection.NoMovieAvailableException;
import com.app.messaging.processor.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component("DirectorProcessor")
public class DirectorProcessor implements Processor<Movie> {
    private static final Logger log = LogManager.getLogger(DirectorProcessor.class);
    private final BlockingQueue<Movie> movieQueue = new LinkedBlockingQueue<>();

    @Override
    public void processMovie(Movie movie) {
        log.info("Process movie: {}", movie.getId());
        movieQueue.add(movie);
    }

    @Override
    public Mono<Movie> getMovie() {
        return Mono.defer(() -> {
            if (!movieQueue.isEmpty()) {
                Movie movie = movieQueue.poll();
                log.info("Retrieved movie: {}", movie.getId());
                return Mono.just(movie);
            } else {
                return Mono.error(new NoMovieAvailableException("No movie available"));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
