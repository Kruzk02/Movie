package com.app.messaging.consumer;

import com.app.Entity.MovieEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MovieEventConsumer {

    private final Queue<MovieEvent> actorMovieEvents;
    private final Queue<MovieEvent> directorMovieEvents;
    private final Queue<MovieEvent> genreMovieEvents;

    public MovieEventConsumer() {
        actorMovieEvents = new ConcurrentLinkedQueue<>();
        directorMovieEvents = new ConcurrentLinkedQueue<>();
        genreMovieEvents = new ConcurrentLinkedQueue<>();
    }

    public MovieEvent consumerForActor() {
        return actorMovieEvents.poll();
    }

    @KafkaListener(topics = "movie-event", groupId = "actor-group",containerFactory = "actorKafkaListenerContainerFactory")
    private void listenForActor(MovieEvent movieEvent) {
        actorMovieEvents.offer(movieEvent);
    }

    public MovieEvent consumerForDirector() {
        return directorMovieEvents.poll();
    }

    public MovieEvent consumerForGenre() {
        return genreMovieEvents.poll();
    }

    @KafkaListener(topics = "movie-event", groupId = "director-group",containerFactory = "directorKafkaListenerContainerFactory")
    private void listenForDirector(MovieEvent movieEvent) {
        directorMovieEvents.offer(movieEvent);
    }

    @KafkaListener(topics = "movie-event", groupId = "genre-group",containerFactory = "genreKafkaListenerContainerFactory")
    private void listenForGenre(MovieEvent movieEvent) {
        genreMovieEvents.offer(movieEvent);
    }

}
