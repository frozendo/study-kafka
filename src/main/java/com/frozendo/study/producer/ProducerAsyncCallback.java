package com.frozendo.study.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerAsyncCallback implements Callback {

    private final Logger logger = LoggerFactory.getLogger(ProducerAsyncCallback.class);

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (metadata != null) {
            logger.info("Message send to topic {} and partition {}, with offset {}", metadata.topic(), metadata.partition(), metadata.offset());
        } else {
            logger.info("Error on send message - {}", exception.getMessage());
        }
    }
}
