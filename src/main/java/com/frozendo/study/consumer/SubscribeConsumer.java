package com.frozendo.study.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.consumer.config.BaseConsumer;
import com.frozendo.study.entity.Product;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubscribeConsumer extends BaseConsumer<String, String> {

    final Logger logger = LoggerFactory.getLogger(SubscribeConsumer.class);

    public static final String GROUP_NAME = "round-robin-group";

    public static void main(String[] args) {
        var consumerInstance = new SubscribeConsumer();
        consumerInstance.initConsumer(1, GROUP_NAME);
    }

    @Override
    protected void startListenerTopic(KafkaConsumer<String, String> kafkaConsumer) {
        kafkaConsumer.subscribe(List.of(TopicName.ROUND_ROBIN_TOPIC.getName()));
    }

    @Override
    protected void consumeMessages(ConsumerRecord<String, String> messageRecord) throws JsonProcessingException {
        logger.info("consumer number {}, reading partition {} and offset {}",
                this.consumerNumber, messageRecord.partition(), messageRecord.offset());

        var product = mapper.readValue(messageRecord.value(), Product.class);

        logger.info("Product - {}", product);
    }

}
