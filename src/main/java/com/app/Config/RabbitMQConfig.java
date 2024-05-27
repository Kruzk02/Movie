package com.app.Config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue movieQueue() {
        return new Queue("movie-queue", false);
    }

    @Bean public Exchange movieExchange() {
        return new DirectExchange("movie-exchange");
    }

    @Bean
    public Binding movieBinding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("movie-routing-key")
                .noargs();
    }
}
