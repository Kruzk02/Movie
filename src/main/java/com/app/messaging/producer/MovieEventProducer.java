package com.app.messaging.producer;

import com.app.messaging.event.MovieEvent;
import com.app.Expection.MovieEventException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MovieEventProducer {

    private final KafkaTemplate<Long,MovieEvent> movieEventTemplate;

    @Autowired
    public MovieEventProducer(KafkaTemplate<Long, MovieEvent> movieEventTemplate) {
        this.movieEventTemplate = movieEventTemplate;
    }

    public void send(MovieEvent event) {
        if (event == null) {
            throw new MovieEventException("Event is empty");
        }
        movieEventTemplate.send("movie-event",event);
    }
}
