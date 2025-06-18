package com.app.serde.Deserializer;

import com.app.messaging.event.MovieEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;

public class MovieEventDeserializer implements Deserializer<MovieEvent> {

    private final ObjectMapper objectMapper;

    public MovieEventDeserializer() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
    @Override
    public MovieEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, MovieEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Deserialization error", e);
        }
    }
}
