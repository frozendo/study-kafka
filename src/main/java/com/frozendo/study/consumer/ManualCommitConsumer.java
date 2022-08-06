package com.frozendo.study.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.entity.Product;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ManualCommitConsumer extends BaseConsumer implements Runnable {

    final Logger logger = LoggerFactory.getLogger(ManualCommitConsumer.class);

    public static final String GROUP_NAME = "key-hash-with-manual-commit-group";

    public static void main(String[] args) {
        var count = 1;
        while (count <= 3) {
            var consumerInstance = new ManualCommitConsumer();
            consumerInstance.setConsumerNumber(count);
            var thread = new Thread(consumerInstance);
            thread.start();
            count++;
        }
    }

    public void onMessage() {
        var properties = KafkaProperties.getConsumerDefaultProperties(GROUP_NAME);
        properties.setProperty("enable.auto.commit", "false");
        var mapper = new ObjectMapper();

        try (var consumer = new KafkaConsumer<String, String>(properties)) {
            consumer.subscribe(List.of(TopicName.KEY_HASH_MANUAL_COMMIT_TOPIC.getName()));

            var noMessageCount = 0;
            var consumerRunning = true;

            while (consumerRunning) {
                var listRecords = consumer.poll(Duration.ofSeconds(30));

                if (listRecords.isEmpty()) {
                    noMessageCount++;
                } else {
                    noMessageCount = 0;
                }

                for (var messageRecord : listRecords) {
                    logger.info("consumer number {}, reading partition {} and offset {}",
                            this.consumerNumber, messageRecord.partition(), messageRecord.offset());

                    var product = mapper.readValue(messageRecord.value(), Product.class);

                    logger.info("Product - {}", product);
                }
                if (!listRecords.isEmpty()) {
                    consumer.commitSync();
                    logger.info("commit execute with success!");
                }

                consumerRunning = noMessageCount < 5;
            }
        } catch (JsonMappingException e) {
            logger.error("Error to parse json - {}", e.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("Error to process json - {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        this.onMessage();
    }
}
