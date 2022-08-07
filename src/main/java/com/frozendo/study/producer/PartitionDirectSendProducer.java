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

public class PartitionDirectSendProducer extends BaseProducer<String, String> {

    private int partitionCount = -1;

    final Logger logger = LoggerFactory.getLogger(PartitionDirectSendProducer.class);

    public static void main(String[] args) throws InterruptedException {
        var partitionDirectSendProducer = new PartitionDirectSendProducer();
        partitionDirectSendProducer.executeProducer();
    }

    @Override
    protected String getTopicName() {
        return TopicName.PARTITION_DIRECT_SEND_TOPIC.getName();
    }

    @Override
    protected void sendMessage(KafkaProducer<String, String> kafkaProducer, Product product) throws JsonProcessingException, ExecutionException, InterruptedException {
        var json = mapper.writeValueAsString(product);

        var productRecord = new ProducerRecord<>(TopicName.PARTITION_DIRECT_SEND_TOPIC.getName(),
                getPartition(), product.code(), json);

        var metadata = kafkaProducer.send(productRecord).get();
        logger.info("sent to topic {} and partition {}, with key {}, offset {} and timestamp {}",
                metadata.topic(), metadata.partition(), product.code(), metadata.offset(), metadata.timestamp());
    }

    private int getPartition() {
        partitionCount = partitionCount >= 2 ? 0 : partitionCount + 1;
        return partitionCount;
    }
}
