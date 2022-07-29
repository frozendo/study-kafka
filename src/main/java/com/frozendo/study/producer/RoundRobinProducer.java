package com.frozendo.study.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.source.ProductSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

public class RoundRobinProducer {

    final Logger logger = LoggerFactory.getLogger(RoundRobinProducer.class);

    public static void main(String[] args) {
        var roundRobinProducer = new RoundRobinProducer();
        roundRobinProducer.executeProducer();
    }

    private void executeProducer() {
        final var properties = KafkaProperties.getProducerDefaultProperties();
        CreateTopics.createTopic(TopicName.ROUND_ROBIN_TOPIC.getName());

        try (var producer = new KafkaProducer<String, String>(properties)) {
            this.producerLoop(producer);
        } catch (ExecutionException | InterruptedException ex) {
            logger.error("Error to get record metadata - {}", ex.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("Error when parse object to json - {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Generic error - {}", e.getMessage());
        }
    }

    private void producerLoop(KafkaProducer<String, String> producer) throws JsonProcessingException, ExecutionException, InterruptedException {
        var initTime = LocalTime.now();
        var stopExecution = false;

        while(!stopExecution) {
            this.buildAndSendMessage(producer);

            Thread.sleep(20000);

            var duration = Duration.between(initTime, LocalTime.now());
            stopExecution = duration.getSeconds() > 300;

        }
    }

    private void buildAndSendMessage(KafkaProducer<String, String> producer) throws JsonProcessingException, ExecutionException, InterruptedException {
        var mapper = new ObjectMapper();
        for (int i = 0; i < 1_000; i++) {
            var product = ProductSource.getProduct();
            var json = mapper.writeValueAsString(product);
            var productRecord = new ProducerRecord<String, String>(TopicName.ROUND_ROBIN_TOPIC.getName(), null, json);

            var metadata = producer.send(productRecord).get();
            logger.info("sent to topic {} and partition {}, with offset {} and timestamp {}",
                    metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
        }
    }
}
