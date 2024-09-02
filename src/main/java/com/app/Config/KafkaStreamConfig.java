package com.app.Config;

import com.app.Entity.UserActivity;
import com.app.serde.UserActivitySerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaStreamConfig {

    private static final Logger log = LogManager.getLogger(KafkaStreamConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaStreamsConfiguration kStreamConfig() {
        Map<String,Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"userActivityStream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.ByteArray().getClass());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilderFactoryBean myKStreamBuilder(KafkaStreamsConfiguration kStreamConfig) {
        return new StreamsBuilderFactoryBean(kStreamConfig);
    }

    @Bean
    public KStream<Long, UserActivity> kStream(StreamsBuilder builder) {
        KStream<Long,UserActivity> kStream = builder.stream("user-activity-input",
                Consumed.with(Serdes.Long(), new UserActivitySerde()));

        kStream.peek((key,value) -> log.info("Incoming record: key={}, value={}", key, value));

        KStream<Long,UserActivity> processedStream = kStream
                .filter((key,value) -> value != null && value.getMovie() != null)
                .peek((key, value) -> log.info("Processed record: key={}, value={}", key, value));

        processedStream.to("user-activity-processed");
        return processedStream;
    }
}
