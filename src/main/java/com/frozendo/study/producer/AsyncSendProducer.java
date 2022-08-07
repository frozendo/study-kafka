package com.frozendo.study.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.entity.Product;
import com.frozendo.study.producer.config.BaseProducer;
import com.frozendo.study.producer.config.ProducerAsyncCallback;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class AsyncSendProducer extends BaseProducer<String, String> {

    private final Callback callback = new ProducerAsyncCallback();

    public static void main(String[] args) throws InterruptedException {
        var asyncSendProducer = new AsyncSendProducer();
        asyncSendProducer.executeProducer();
    }

    @Override
    protected String getTopicName() {
        return TopicName.ASYNC_SEND_TOPIC.getName();
    }

    @Override
    protected void sendMessage(KafkaProducer<String, String> kafkaProducer, Product product) throws JsonProcessingException {
        var json = mapper.writeValueAsString(product);
        var productRecord = new ProducerRecord<>(TopicName.ASYNC_SEND_TOPIC.getName(), product.code(), json);

        kafkaProducer.send(productRecord, callback);
    }
}
