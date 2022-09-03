package com.frozendo.study.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.consumer.config.BaseConsumer;
import com.frozendo.study.entity.Product;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class ManualAsyncCommitConsumer extends BaseConsumer<String, String> {

    final Logger logger = LoggerFactory.getLogger(ManualAsyncCommitConsumer.class);

    public static final String GROUP_NAME = "custom-partitioner-async-group";

    public static void main(String[] args) {
        var count = 1;
        while (count <= 3) {
            var consumerInstance = new ManualAsyncCommitConsumer();
            consumerInstance.initConsumer(count, GROUP_NAME);
            count++;
        }
    }

    @Override
    protected void startListenerTopic(KafkaConsumer<String, String> kafkaConsumer) {
        kafkaConsumer.subscribe(List.of(TopicName.CUSTOM_PARTITIONER_TOPIC.getName()));
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
        kafkaConsumer.commitAsync();
        logger.info("commit async execute!");
    }

    protected Properties getProperties() {
        var properties = KafkaProperties.getConsumerDefaultProperties(GROUP_NAME);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return properties;
    }
}
