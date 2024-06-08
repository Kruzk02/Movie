package com.app.messaging.producer.Impl;

import com.app.Entity.Genre;
import com.app.messaging.producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenreProducer implements Producer<Genre> {

    private static final Logger log = LogManager.getLogger(GenreProducer.class);
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public GenreProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void send(Genre genre) {
        try{
            amqpTemplate.convertAndSend("genre-exchange","genre-routing-key",genre);
            log.info("Successfully send messaging [{}] to genre-queue",genre);
        }catch (AmqpException e) {
            log.error("Failed to send messaging [{}] to genre-queue",genre,e);
        }
    }
}
