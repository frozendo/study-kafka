package com.frozendo.study.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.entity.Product;
import com.frozendo.study.producer.config.BaseProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class KeyHashProducer extends BaseProducer<String, String> {

    final Logger logger = LoggerFactory.getLogger(KeyHashProducer.class);

    public static void main(String[] args) throws InterruptedException {
        var keyHashProducer = new KeyHashProducer();
        keyHashProducer.executeProducer();
    }

    @Override
    protected String getTopicName() {
        return TopicName.KEY_HASH_TOPIC.getName();
    }

    @Override
    protected void sendMessage(KafkaProducer<String, String> kafkaProducer, Product product) throws JsonProcessingException, ExecutionException, InterruptedException {
        var json = mapper.writeValueAsString(product);
        var productRecord = new ProducerRecord<>(TopicName.KEY_HASH_TOPIC.getName(), product.code(), json);

        var metadata = kafkaProducer.send(productRecord).get();
        logger.info("sent to topic {} and partition {}, with key {}, offset {} and timestamp {}",
                metadata.topic(), metadata.partition(), product.code(), metadata.offset(), metadata.timestamp());
    }
}
