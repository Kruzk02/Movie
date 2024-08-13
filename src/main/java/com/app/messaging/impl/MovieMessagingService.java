package com.app.messaging.impl;

import com.app.Entity.Movie;
import com.app.Expection.NoMovieAvailableException;
import com.app.messaging.MessagingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component("movieMessagingService")
public class MovieMessagingService implements MessagingService<Movie> {

    private static final Logger log = LogManager.getLogger(MovieMessagingService.class);
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler;
    private final Map<Long, Movie> movieMap = new ConcurrentHashMap<>();

    @Autowired
    public MovieMessagingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void sendMessage(Movie message) {

    }

    private void handleReceivedMessage(String json) {

    }

    @Override
    public Mono<Movie> receiveMessage() {
        return Mono.defer(() -> {
            if (!movieMap.isEmpty()) {
                Map.Entry<Long, Movie> entry = movieMap.entrySet().iterator().next();
                log.info("Retrieving movie: {}", entry.getKey());
                return Mono.just(entry.getValue());
            } else {
                return Mono.error(new NoMovieAvailableException("No movie available"));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}

