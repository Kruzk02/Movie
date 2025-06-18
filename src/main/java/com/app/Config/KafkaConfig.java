package com.app.Config;

import com.app.messaging.event.MovieEvent;
import com.app.serde.Deserializer.MovieEventDeserializer;
import com.app.serde.Serializer.MovieEventSerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String,Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic movieEventTopic() {
        return new NewTopic("movie-event",1, (short) 1);
    }

    private ProducerFactory<Long, byte[]> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<Long, byte[]> template() {
        return new KafkaTemplate<>(producerFactory());
    }

    private ProducerFactory<Long, MovieEvent> movieEventproducerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MovieEventSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<Long, MovieEvent> movieEventTemplate() {
        return new KafkaTemplate<>(movieEventproducerFactory());
    }

    private ConsumerFactory<Long, MovieEvent> movieEventconsumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MovieEventDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, MovieEvent> genreKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, MovieEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movieEventconsumerFactory("genre-group"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, MovieEvent> actorKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, MovieEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movieEventconsumerFactory("actor-group"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, MovieEvent> directorKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, MovieEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movieEventconsumerFactory("director-group"));
        return factory;
    }
}
