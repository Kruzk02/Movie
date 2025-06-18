package com.app.messaging.consumer;

import module.movie.entity.Movie;
import com.app.Entity.UserActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class RecommendMovieConsumer {

    private static final Logger log = LogManager.getLogger(RecommendMovieConsumer.class);
    private final Map<Long, Movie> map = new ConcurrentHashMap<>();
    @Autowired private ObjectMapper objectMapper;

    public Queue<Movie> receive(Long userId) {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(userId))
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(LinkedList::new));

    }

    @KafkaListener(topics = "recommend-movie", groupId = "recommend-movie-group",containerFactory = "recommendMovieKafkaListenerContainerFactory")
    private void listen(byte[] bytes) {
        try {
            UserActivity userActivity = objectMapper.readValue(bytes, UserActivity.class);
            map.put(userActivity.getUserId(),userActivity.getMovie());
            log.info("User id: [{}], Movie: [{}]",userActivity.getUserId(),userActivity.getMovie());
        } catch (Exception e) {
            log.error("Error deserializing User Activity object: {}", e.getMessage());
        }
    }

}
