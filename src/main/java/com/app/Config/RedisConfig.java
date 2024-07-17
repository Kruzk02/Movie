package com.app.Config;

import com.app.Entity.Actor;
import com.app.Entity.Director;
import com.app.Entity.Genre;
import com.app.Entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class RedisConfig {

    @Primary
    public ReactiveRedisConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
    }

    @Bean
    public ProxyManager<String> lettuceBasedProxyManager(){
        RedisClient redisClient = RedisClient.create(RedisURI.builder()
                .withHost("localhost")
                .withPort(6379)
                .withSsl(false)
                .build());
        StatefulRedisConnection<String,byte[]> redisConnection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(redisConnection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1L))).build();
    }

    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration(){
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.builder().capacity(200L).refillGreedy(200L,Duration.ofMinutes(1L)).build())
                .build();
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
    public ReactiveRedisTemplate<String, Genre> genreReactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory,ObjectMapper objectMapper) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Genre> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper,Genre.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Genre> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Genre> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

}
