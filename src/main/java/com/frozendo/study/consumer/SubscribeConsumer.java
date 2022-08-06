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

public class SubscribeConsumer extends BaseConsumer {

    final Logger logger = LoggerFactory.getLogger(SubscribeConsumer.class);

    public static final String GROUP_NAME = "round-robin-group";

    public static void main(String[] args) {
            var consumerInstance = new SubscribeConsumer();
            consumerInstance.setConsumerNumber(1);
            consumerInstance.onMessage();
    }

    public void onMessage() {
        var properties = KafkaProperties.getConsumerDefaultProperties(GROUP_NAME);
        var mapper = new ObjectMapper();

        try (var consumer = new KafkaConsumer<String, String>(properties)) {
            consumer.subscribe(List.of(TopicName.ROUND_ROBIN_TOPIC.getName()));

            var noMessageCount = 0;
            var consumerRunning = true;

            while (consumerRunning) {
                var listRecords = consumer.poll(Duration.ofSeconds(10));

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

                consumerRunning = noMessageCount < 5;
            }
        } catch (JsonMappingException e) {
            logger.error("Error to parse json - {}", e.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("Error to process json - {}", e.getMessage());
        }
    }

}
