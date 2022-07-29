package com.frozendo.study.producer;

import com.frozendo.study.common.KafkaProperties;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CreateTopics {

    private CreateTopics() {}

    private static final Logger logger = LoggerFactory.getLogger(CreateTopics.class);

    public static void createTopic(String topicName) {
        var properties = KafkaProperties.getKafkaHost();
        try (var admin = Admin.create(properties)) {
            var topic = new NewTopic(topicName, 3, (short) 3);
            admin.createTopics(List.of(topic));
        }
        logger.info("Topic {} created", topicName);
    }

}
