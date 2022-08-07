package com.frozendo.study.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frozendo.study.common.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

public abstract class BaseConsumer<K, V> implements Runnable {

    private final Logger baseLogger = LoggerFactory.getLogger(BaseConsumer.class);

    protected ObjectMapper mapper = new ObjectMapper();

    protected Integer consumerNumber;

    protected String groupName;

    protected abstract void startListenerTopic(KafkaConsumer<K, V> kafkaConsumer);

    protected abstract void consumeMessages(ConsumerRecord<K, V> messageRecord) throws JsonProcessingException;

    @Override
    public void run() {
        this.onMessage();
    }

    protected KafkaConsumer<K, V> getConsumer() {
        return new KafkaConsumer<>(getProperties());
    }

    protected void initConsumer(int consumerNumber, String groupName) {
        this.consumerNumber = consumerNumber;
        this.groupName = groupName;
        var thread = new Thread(this);
        thread.start();
    }

    protected void manualCommitOffset(KafkaConsumer<K, V> kafkaConsumer) {
        baseLogger.info("Auto commit used");
    }

    protected Properties getProperties() {
        return KafkaProperties.getConsumerDefaultProperties(this.groupName);
    }

    protected void onMessage() {
        try (var kafkaConsumer = getConsumer()) {
            startListenerTopic(kafkaConsumer);

            var noMessageCount = 0;
            var consumerRunning = true;

            while (consumerRunning) {
                var listRecords = kafkaConsumer.poll(Duration.ofSeconds(10));

                if (listRecords.isEmpty()) {
                    noMessageCount++;
                } else {
                    noMessageCount = 0;
                }

                for (var messageRecord : listRecords) {
                    consumeMessages(messageRecord);
                }
                if (!listRecords.isEmpty()) {
                    manualCommitOffset(kafkaConsumer);
                }

                consumerRunning = noMessageCount < 5;
            }
        } catch (JsonMappingException e) {
            baseLogger.error("Error to parse json - {}", e.getMessage());
        } catch (JsonProcessingException e) {
            baseLogger.error("Error to process json - {}", e.getMessage());
        }
    }
}
