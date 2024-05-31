package com.app.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("*", "*"));
        return converter;
    }
}
