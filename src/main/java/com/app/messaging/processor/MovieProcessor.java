package com.app.messaging.processor;

import com.app.Entity.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Component
public class MovieProcessor  {

    private static final Logger log = LogManager.getLogger(MovieProcessor.class);
    private final BlockingQueue<Movie> movieQueue = new LinkedBlockingQueue<>();

    public void processMovie(Movie movie) {
        log.info("Process movie: {}", movie.getId());
        movieQueue.offer(movie);
    }

    public Mono<Movie> getMovie() {
        return Mono.fromCallable(() -> {
            Movie movie = movieQueue.take();
            log.info("Retrieved movie: {}", movie.getId());
            return movie;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
