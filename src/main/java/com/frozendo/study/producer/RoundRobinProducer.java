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

public class RoundRobinProducer extends BaseProducer<String, String> {

    final Logger logger = LoggerFactory.getLogger(RoundRobinProducer.class);

    public static void main(String[] args) throws InterruptedException {
        var roundRobinProducer = new RoundRobinProducer();
        roundRobinProducer.executeProducer();
    }

    @Override
    protected String getTopicName() {
        return TopicName.ROUND_ROBIN_TOPIC.getName();
    }

    @Override
    protected void sendMessage(KafkaProducer<String, String> kafkaProducer, Product product) throws JsonProcessingException, ExecutionException, InterruptedException {
        var json = mapper.writeValueAsString(product);
        var productRecord = new ProducerRecord<String, String>(TopicName.ROUND_ROBIN_TOPIC.getName(), json);

        var metadata = kafkaProducer.send(productRecord).get();
        logger.info("sent to topic {} and partition {}, with offset {} and timestamp {}",
                metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
    }
}
