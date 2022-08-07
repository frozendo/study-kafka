package com.frozendo.study.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.entity.Product;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ManualCommitConsumer extends BaseConsumer<String, String> {

    final Logger logger = LoggerFactory.getLogger(ManualCommitConsumer.class);

    public static final String GROUP_NAME = "key-hash-with-manual-commit-group";

    public static void main(String[] args) {
        var count = 1;
        while (count <= 3) {
            var consumerInstance = new ManualCommitConsumer();
            consumerInstance.initConsumer(count, GROUP_NAME);
            count++;
        }
    }

    @Override
    protected void startListenerTopic(KafkaConsumer<String, String> kafkaConsumer) {
        kafkaConsumer.subscribe(List.of(TopicName.KEY_HASH_TOPIC.getName()));
    }

    @Override
    protected void consumeMessages(ConsumerRecord<String, String> messageRecord) throws JsonProcessingException {
        logger.info("consumer number {}, reading partition {} and offset {}",
                this.consumerNumber, messageRecord.partition(), messageRecord.offset());

        var product = mapper.readValue(messageRecord.value(), Product.class);

        logger.info("Product - {}", product);
    }

    @Override
    protected void manualCommitOffset(KafkaConsumer<String, String> kafkaConsumer) {
        kafkaConsumer.commitSync();
        logger.info("commit execute with success!");
    }
}
