package com.frozendo.study.common;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProperties {

    public static final String KAFKA_HOST = "http://localhost:29092";
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    private KafkaProperties() {}

    public static Properties getKafkaHost() {
        var properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS, KAFKA_HOST);
        return properties;
    }

    public static Properties getProducerDefaultProperties() {
        var properties = getKafkaHost();
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static Properties getConsumerDefaultProperties(String groupId) {
        var properties = getKafkaHost();
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);
        return properties;
    }

}
