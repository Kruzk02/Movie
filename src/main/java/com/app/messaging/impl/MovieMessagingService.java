package com.app.messaging.impl;

import com.app.Entity.Movie;
import com.app.Expection.NoMovieAvailableException;
import com.app.messaging.MessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component("movieMessagingService")
public class MovieMessagingService implements MessagingService<Movie> {

    private static final Logger log = LogManager.getLogger(MovieMessagingService.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduler;
    private final Map<Long, Movie> movieMap = new ConcurrentHashMap<>();

    @Autowired
    public MovieMessagingService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void sendMessage(Movie message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend("movie-exchange", "movie-routing-key", json);
            log.info("Successfully sent message [{}] to movie-queue", message);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message to JSON", e);
        } catch (AmqpException e) {
            log.error("Failed to send message [{}] to movie-queue", message, e);
        }
    }

    @RabbitListener(queues = "movie-queue")
    private void handleReceivedMessage(String json) {
        try {
            Movie movie = objectMapper.readValue(json, Movie.class);
            log.info("Successfully received message [{}] from movie-queue", movie);
            movieMap.put(movie.getId(), movie);

            scheduler.schedule(() -> {
                log.info("Removing movie: {}", movie.getId());
                movieMap.remove(movie.getId());
            }, 30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON message", e);
        }
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

