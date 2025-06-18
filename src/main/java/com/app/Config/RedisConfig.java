package com.app.Config;

import com.app.Entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import com.app.module.movie.entity.Movie;
import com.app.module.movie.entity.MovieMedia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class RedisConfig {

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager() {
        RedisClient redisClient = RedisClient.create(RedisURI.builder()
                .withHost("localhost")
                .withPort(6379)
                .withSsl(false)
                .build());

        StatefulRedisConnection<String, byte[]> redisConnection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1L)))
                .build();
    }

    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration() {
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(200L)
                        .refillGreedy(200L, Duration.ofMinutes(1L))
                        .build())
                .build();
    }

    @Bean
    public ReactiveRedisTemplate<String, Movie> movieReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, Movie.class);
    }

    @Bean
    public ReactiveRedisTemplate<String, Actor> actorReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, Actor.class);
    }

    @Bean
    public ReactiveRedisTemplate<String, Director> directorReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, Director.class);
    }

    @Bean
    public ReactiveRedisTemplate<String, Genre> genreReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, Genre.class);
    }

    @Bean
    public ReactiveRedisTemplate<String, MovieMedia> movieMediaReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, MovieMedia.class);
    }

    @Bean
    public ReactiveRedisTemplate<String, Rating> ratingReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, Rating.class);
    }

    @Bean
    public ReactiveRedisTemplate<String, Comment> commentReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, Comment.class);
    }

    @Bean
    public ReactiveRedisTemplate<String, Double> doubleReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        return createReactiveRedisTemplate(factory,objectMapper, Double.class);
    }

    private <T> ReactiveRedisTemplate<String, T> createReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory, ObjectMapper objectMapper, Class<T> clazz) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<T> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, clazz);
        RedisSerializationContext<String, T> context = RedisSerializationContext.<String, T>newSerializationContext(keySerializer)
                .value(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
