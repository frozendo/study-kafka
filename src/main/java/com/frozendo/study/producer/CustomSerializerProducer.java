package com.frozendo.study.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.entity.Product;
import com.frozendo.study.source.ProductSource;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

public class CustomSerializerProducer {

    final Logger logger = LoggerFactory.getLogger(CustomSerializerProducer.class);

    private final Callback callback = new ProducerAsyncCallback();

    public static void main(String[] args) {
        var roundRobinProducer = new CustomSerializerProducer();
        roundRobinProducer.executeProducer();
    }

    private void executeProducer() {
        final var properties = KafkaProperties.getProducerDefaultProperties();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Base64Serializer.class);
        CreateTopics.createTopic(TopicName.CUSTOM_SERIALIZER_TOPIC.getName());

        try (var producer = new KafkaProducer<String, Product>(properties)) {
            this.producerLoop(producer);
        } catch (ExecutionException | InterruptedException ex) {
            logger.error("Error to get record metadata - {}", ex.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("Error when parse object to json - {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Generic error - {}", e.getMessage());
        }
    }

    private void producerLoop(KafkaProducer<String, Product> producer) throws JsonProcessingException, ExecutionException, InterruptedException {
        var initTime = LocalTime.now();
        var stopExecution = false;

        while(!stopExecution) {
            this.buildAndSendMessage(producer);

            Thread.sleep(20000);

            var duration = Duration.between(initTime, LocalTime.now());
            stopExecution = duration.getSeconds() > 300;

        }
    }

    private void buildAndSendMessage(KafkaProducer<String, Product> producer) throws JsonProcessingException, ExecutionException, InterruptedException {
        for (int i = 0; i < 1_000; i++) {
            var product = ProductSource.getProduct();
            var productRecord = new ProducerRecord<>(TopicName.CUSTOM_SERIALIZER_TOPIC.getName(), product.code(), product);

            producer.send(productRecord, callback);
        }
    }
}
