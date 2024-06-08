package com.app.messaging.producer.Impl;

import com.app.Entity.Actor;
import com.app.messaging.producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorProducer implements Producer<Actor> {

    private static final Logger log = LogManager.getLogger(ActorProducer.class);
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public ActorProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void send(Actor actor){
        try{
            amqpTemplate.convertAndSend("actor-exchange","actor-routing-key",actor);
            log.info("Successfully send messaging [{}] to actor-queue",actor);
        }catch (AmqpException e) {
            log.error("Failed to send messaging [{}] to actor-queue",actor,e);
        }
    }
}
