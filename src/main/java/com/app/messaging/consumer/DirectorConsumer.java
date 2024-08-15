package com.app.messaging.consumer;

import com.app.Entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class DirectorConsumer {

    private static final Logger log = LogManager.getLogger(DirectorConsumer.class);
    private final Queue<Movie> queue = new ConcurrentLinkedQueue<>();
    @Autowired private ObjectMapper objectMapper;

    public Movie receive() {
        return queue.poll();
    }

    @KafkaListener(topics = "movie-data", groupId = "director-group",containerFactory = "directorKafkaListenerContainerFactory")
    private void listen(byte[] data) throws IOException {
        try {
            Movie movie = objectMapper.readValue(data,Movie.class);
            queue.offer(movie);
        } catch (IOException e) {
            log.error("Error deserializing Movie object: {}", e.getMessage());
        }
    }
}
