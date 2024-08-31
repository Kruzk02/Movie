package com.app.messaging.producer;

import com.app.Entity.UserActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserActivityProducer {

    private static final Logger log = LogManager.getLogger(UserActivityProducer.class);
    private final KafkaTemplate<Long,byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserActivityProducer(KafkaTemplate<Long, byte[]> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(UserActivity userActivity) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(userActivity);
            kafkaTemplate.send("user-activity-input",bytes);
            log.info("Send user activity data as byte: [{}]",bytes);
        } catch (Exception e) {
            log.error("Error send user activity: [{}]",e.getMessage());
        }
    }
}
