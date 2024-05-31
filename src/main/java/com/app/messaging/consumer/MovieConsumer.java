package com.app.messaging.consumer;

import com.app.Entity.Movie;
import com.app.messaging.processor.MovieProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieConsumer {

    private static final Logger log = LogManager.getLogger(MovieConsumer.class);
    private final MovieProcessor movieProcessor;

    @Autowired
    public MovieConsumer(MovieProcessor movieProcessor) {
        this.movieProcessor = movieProcessor;
    }

    @RabbitListener(queues = "movie-queue")
    public void receiveMovie(Movie movie){
        try{
            log.info("Successfully receive [{}] from movie-queue",movie);
            movieProcessor.processMovie(movie);
        }catch (Exception e){
            log.error("Failed receive [{}] from movie-queue",movie,e);
        }
    }
}
