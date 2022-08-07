package com.frozendo.study.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.consumer.config.BaseConsumer;
import com.frozendo.study.entity.Product;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AssignConsumer extends BaseConsumer<String, String> {

    final Logger logger = LoggerFactory.getLogger(AssignConsumer.class);

    public static final String GROUP_NAME = "assign-partition-group";

    public static void main(String[] args) {
        var count = 1;
        while (count <= 3) {
            var consumerInstance = new AssignConsumer();
            consumerInstance.initConsumer(count, GROUP_NAME);
            count++;
        }
    }

    @Override
    protected void startListenerTopic(KafkaConsumer<String, String> kafkaConsumer) {
        var partitionNumber = this.consumerNumber - 1;
        var topicPartition = new TopicPartition(TopicName.PARTITION_DIRECT_SEND_TOPIC.getName(), partitionNumber);
        kafkaConsumer.assign(List.of(topicPartition));
    }

    @Override
    protected void consumeMessages(ConsumerRecord<String, String> messageRecord) throws JsonProcessingException {
        logger.info("consumer number {}, reading partition {} and offset {}",
                this.consumerNumber, messageRecord.partition(), messageRecord.offset());

        var product = mapper.readValue(messageRecord.value(), Product.class);

        logger.info("Product - {}", product);
    }
}
