package com.frozendo.study.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.entity.Product;
import com.frozendo.study.producer.config.BaseProducer;
import com.frozendo.study.producer.config.ProductDepartmentPartitioner;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CustomPartitionerProducer extends BaseProducer<String, String> {

    final Logger logger = LoggerFactory.getLogger(CustomPartitionerProducer.class);

    public static void main(String[] args) throws InterruptedException {
        var customPartitionerProducer = new CustomPartitionerProducer();
        customPartitionerProducer.executeProducer();
    }

    @Override
    protected Properties getProperties() {
        final var properties = KafkaProperties.getProducerDefaultProperties();
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, ProductDepartmentPartitioner.class);
        return properties;
    }

    @Override
    protected String getTopicName() {
        return TopicName.CUSTOM_PARTITIONER_TOPIC.getName();
    }

    @Override
    protected void sendMessage(KafkaProducer<String, String> kafkaProducer, Product product) throws JsonProcessingException, ExecutionException, InterruptedException {
        var json = mapper.writeValueAsString(product);
        var productRecord = new ProducerRecord<>(TopicName.CUSTOM_PARTITIONER_TOPIC.getName(), product.department(), json);

        var metadata = kafkaProducer.send(productRecord).get();
        logger.info("sent to topic {} and partition {}, with offset {} and timestamp {}",
                metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
    }
}
