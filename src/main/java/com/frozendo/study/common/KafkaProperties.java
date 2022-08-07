package com.frozendo.study.common;

import org.apache.kafka.clients.consumer.ConsumerConfig;
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
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return properties;
    }

}
