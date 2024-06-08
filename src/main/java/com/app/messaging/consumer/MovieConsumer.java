package com.app.messaging.consumer;

import com.app.Entity.Movie;
import com.app.messaging.processor.Processor;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;


@Component
public class MovieConsumer {

    private static final Logger log = LogManager.getLogger(MovieConsumer.class);
    private final Processor<Movie> processor;

    @Autowired
    public MovieConsumer(Processor<Movie> processor) {
        this.processor = processor;
    }

    @RabbitListener(queues = "movie-queue")
    public void receiveMovie(Movie movie){
        try{
            log.info("Successfully receive [{}] from movie-queue",movie);
            processor.process(movie);
        }catch (Exception e){
            log.error("Failed receive [{}] from movie-queue",movie,e);
        }
    }
}

