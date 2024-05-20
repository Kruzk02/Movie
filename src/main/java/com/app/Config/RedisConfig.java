package com.app.Config;

import com.app.Entity.Actor;
import com.app.Entity.Director;
import com.app.Entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Primary
    public ReactiveRedisConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
    }

    @Bean
    public ReactiveRedisTemplate<String, Movie> movieReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Movie> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,Movie.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Movie> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Movie> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, Actor> actorReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Actor> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,Actor.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Actor> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Actor> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, Director> directorReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Director> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,Director.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Director> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Director> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }
}
