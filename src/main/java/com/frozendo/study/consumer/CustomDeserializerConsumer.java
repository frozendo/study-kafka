package com.frozendo.study.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.consumer.config.Base64Deserializer;
import com.frozendo.study.consumer.config.BaseConsumer;
import com.frozendo.study.entity.Product;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class CustomDeserializerConsumer extends BaseConsumer<String, Product> {

    final Logger logger = LoggerFactory.getLogger(CustomDeserializerConsumer.class);

    public static final String GROUP_NAME = "custom-serializer-group";

    public static void main(String[] args) {
        var count = 1;
        while (count <= 3) {
            var consumerInstance = new CustomDeserializerConsumer();
            consumerInstance.initConsumer(count, GROUP_NAME);
            count++;
        }
    }

    @Override
    protected Properties getProperties() {
        var properties = KafkaProperties.getConsumerDefaultProperties(this.groupName);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Base64Deserializer.class);
        return properties;
    }

    @Override
    protected void startListenerTopic(KafkaConsumer<String, Product> kafkaConsumer) {
        kafkaConsumer.subscribe(List.of(TopicName.CUSTOM_SERIALIZER_TOPIC.getName()));
    }

    @Override
    protected void consumeMessages(ConsumerRecord<String, Product> messageRecord) throws JsonProcessingException {
        logger.info("consumer number {}, reading partition {} and offset {}",
                this.consumerNumber, messageRecord.partition(), messageRecord.offset());

        logger.info("Product - {}", messageRecord.value());
    }
}
