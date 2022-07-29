package com.frozendo.study.Consumer;

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
import java.util.Objects;

public class ManualCommitConsumer extends BaseConsumer implements Runnable {

    final Logger logger = LoggerFactory.getLogger(ManualCommitConsumer.class);

    public static final String GROUP_NAME = "key-hash-with-manual-commit-group";

    public static void main(String[] args) {
        var consumerQtd = 1;
        if (Objects.nonNull(args) && args.length > 0) {
            consumerQtd = Integer.parseInt(args[0]);
        }
        for (int i = 1; i <= consumerQtd; i++) {
            var consumerInstance = new ManualCommitConsumer();
            consumerInstance.setConsumerNumber(i);
            var thread = new Thread(consumerInstance);
            thread.start();
        }
    }

    public void onMessage() {
        var properties = KafkaProperties.getConsumerDefaultProperties(GROUP_NAME);
        properties.setProperty("enable.auto.commit", "false");
        var mapper = new ObjectMapper();

        try (var consumer = new KafkaConsumer<String, String>(properties)) {
            consumer.subscribe(List.of(TopicName.KEY_HASH_MANUAL_COMMIT_TOPIC.getName()));

            while (true) {
                var listRecords = consumer.poll(Duration.ofSeconds(10));

                for (var record : listRecords) {
                    logger.info("consumer number {}, reading partition {} and offset {}",
                            this.consumerNumber, record.partition(), record.offset());

                    var product = mapper.readValue(record.value(), Product.class);

                    logger.info("Product - {}", product);
                }
                if (!listRecords.isEmpty()) {
                    consumer.commitSync();
                    logger.info("commit execute with success!");
                }
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
