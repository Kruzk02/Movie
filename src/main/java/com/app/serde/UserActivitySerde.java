package com.app.serde;

import com.app.Entity.UserActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class UserActivitySerde extends Serdes.WrapperSerde<UserActivity> {

    public UserActivitySerde() {
        super(new UserActivitySerializer(),new UserActivityDeserializer());
    }

    public static class UserActivitySerializer implements Serializer<UserActivity> {
        private final ObjectMapper objectMapper;

        public UserActivitySerializer() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
        }
        @Override
        public byte[] serialize(String topic, UserActivity data) {
           try {
               return objectMapper.writeValueAsBytes(data);
           } catch (Exception e) {
               throw new RuntimeException("Error serializing UserActivity", e);
           }
        }
    }

    public static class UserActivityDeserializer implements Deserializer<UserActivity> {
        private final ObjectMapper objectMapper;

        public UserActivityDeserializer() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
        }
        @Override
        public UserActivity deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readValue(data, UserActivity.class);
            } catch (Exception e) {
                throw new RuntimeException("Error deserializing UserActivity", e);
            }
        }
    }
}
