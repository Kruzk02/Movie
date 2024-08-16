package com.app.messaging.producer;

import com.app.Entity.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MovieDataProducer {

    private static final Logger log = LogManager.getLogger(MovieDataProducer.class);
    private final KafkaTemplate<Long,byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieDataProducer(KafkaTemplate<Long, byte[]> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(Movie movie) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(movie);
            kafkaTemplate.send("movie-data", bytes);
            log.info("Sending movie data as bytes: {}", Arrays.toString(bytes));
        }catch (JsonProcessingException e) {
            log.error("Error serializing Movie object: {}", e.getMessage());
        }
    }
}
