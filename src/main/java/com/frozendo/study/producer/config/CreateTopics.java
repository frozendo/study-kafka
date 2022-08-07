package com.frozendo.study.producer.config;

import com.frozendo.study.common.KafkaProperties;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTopics {

    private CreateTopics() {}

    private static final Logger logger = LoggerFactory.getLogger(CreateTopics.class);

    public static void createTopic(String topicName) {
        createTopic(topicName, 3);
    }

    public static void createTopic(String topicName, int qtdPartitions) {
        var properties = KafkaProperties.getKafkaHost();
        try (var admin = Admin.create(properties)) {
            var topic = new NewTopic(topicName, qtdPartitions, (short) qtdPartitions);
            admin.createTopics(List.of(topic));
        }
        logger.info("Topic {} created", topicName);
    }

    private static Map<String, String> topicConfig(boolean cleanPolicy) {
        var configMap = new HashMap<String, String>();
        if (cleanPolicy) {
            configMap.put("cleanup.policy", "compact");
        }
        return configMap;
    }

}
