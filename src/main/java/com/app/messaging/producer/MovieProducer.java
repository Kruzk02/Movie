package com.app.messaging.producer;

import com.app.DTO.MovieDTO;
import com.app.Entity.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieProducer {

    private static final Logger log = LogManager.getLogger(MovieProducer.class);
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public MovieProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMovie(Movie movie){
        try{
            amqpTemplate.convertAndSend("movie-exchange","movie-routing-key",movie);
            log.info("Successfully send messaging [{}] to movie-queue",movie);
        }catch (AmqpException e){
            log.error("Failed to send messaging [{}] to movie-queue",movie,e);
        }
    }
}
