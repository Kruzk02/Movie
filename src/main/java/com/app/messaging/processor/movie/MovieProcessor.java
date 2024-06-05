package com.app.messaging.processor.movie;

import com.app.Entity.Movie;
import com.app.Expection.NoMovieAvailableException;
import com.app.messaging.processor.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component("MovieProcessor")
public class MovieProcessor implements Processor<Movie> {

    private static final Logger log = LogManager.getLogger(MovieProcessor.class);
    private final Map<Long,Movie> map;
    private final ScheduledExecutorService scheduler;

    public MovieProcessor() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.map = new HashMap<>();
    }

    @Override
    public void processMovie(Movie movie) {
        log.info("Process movie: {}", movie.getId());
        map.put(movie.getId(),movie);

        scheduler.schedule(() -> {
           log.info("Removing movie: {}",movie.getId());
           map.remove(movie.getId());
        },30, TimeUnit.MINUTES);
    }

    @Override
    public Mono<Movie> getMovie() {
        return Mono.defer(() -> {
            if (!map.isEmpty()) {
                Map.Entry<Long,Movie> entry = map.entrySet().iterator().next();
                log.info("Retrieved movie: {}", entry.getKey());
                return Mono.just(entry.getValue());
            } else {
                return Mono.error(new NoMovieAvailableException("No movie available"));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
