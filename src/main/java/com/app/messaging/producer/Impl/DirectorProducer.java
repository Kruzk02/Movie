package com.app.messaging.producer.Impl;

import com.app.Entity.Director;
import com.app.messaging.producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DirectorProducer implements Producer<Director> {

    private static final Logger log = LogManager.getLogger(DirectorProducer.class);
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public DirectorProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void send(Director director) {
        try{
            amqpTemplate.convertAndSend("director-exchange","director-routing-key",director);
            log.info("Successfully send messaging [{}] to director-queue",director);
        }catch (AmqpException e) {
            log.error("Failed to send messaging [{}] to director-queue",director,e);
        }
    }
}
