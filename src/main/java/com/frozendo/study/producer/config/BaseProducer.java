package com.frozendo.study.producer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.entity.Product;
import com.frozendo.study.source.ProductSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public abstract class BaseProducer<K, V> {

    final Logger baseLogger = LoggerFactory.getLogger(BaseProducer.class);

    protected final ObjectMapper mapper = new ObjectMapper();

    protected abstract String getTopicName();
    
    protected abstract void sendMessage(KafkaProducer<K, V> kafkaProducer, Product product) throws JsonProcessingException, ExecutionException, InterruptedException;

    protected KafkaProducer<K, V> getKafkaProducer() {
        return new KafkaProducer<>(getProperties());
    }

    protected Properties getProperties() {
        return KafkaProperties.getProducerDefaultProperties();
    }

    protected void executeProducer() throws InterruptedException {
        final var name = getTopicName();
        
        CreateTopics.createTopic(name);

        try (var producer = getKafkaProducer()) {
            this.producerLoop(producer);
        } catch (InterruptedException ex) {
            throw ex;
        } catch (JsonProcessingException e) {
            baseLogger.error("Error when parse object to json - {}", e.getMessage());
        } catch (Exception e) {
            baseLogger.error("Generic error - {}", e.getMessage());
        }
    }

    private void producerLoop(KafkaProducer<K, V> producer) throws JsonProcessingException, InterruptedException, ExecutionException {
        var initTime = LocalTime.now();
        var stopExecution = false;

        while(!stopExecution) {
            this.produceMessage(producer);

            Thread.sleep(20000);

            var duration = Duration.between(initTime, LocalTime.now());
            stopExecution = duration.getSeconds() > 300;

        }
    }

    private void produceMessage(KafkaProducer<K, V> producer) throws JsonProcessingException, ExecutionException, InterruptedException {
        for (int i = 0; i < 1_000; i++) {
            sendMessage(producer, ProductSource.getProduct());
        }
    }
}
