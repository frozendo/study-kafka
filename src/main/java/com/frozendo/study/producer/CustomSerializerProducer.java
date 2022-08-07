package com.frozendo.study.producer;

import com.frozendo.study.common.KafkaProperties;
import com.frozendo.study.common.TopicName;
import com.frozendo.study.entity.Product;
import com.frozendo.study.producer.config.Base64Serializer;
import com.frozendo.study.producer.config.BaseProducer;
import com.frozendo.study.producer.config.ProducerAsyncCallback;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CustomSerializerProducer extends BaseProducer<String, Product> {

    private final Callback callback = new ProducerAsyncCallback();

    public static void main(String[] args) throws InterruptedException {
        var customSerializerProducer = new CustomSerializerProducer();
        customSerializerProducer.executeProducer();
    }

    @Override
    protected Properties getProperties() {
        var properties = KafkaProperties.getProducerDefaultProperties();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Base64Serializer.class);
        return properties;
    }

    @Override
    protected String getTopicName() {
        return TopicName.CUSTOM_SERIALIZER_TOPIC.getName();
    }

    @Override
    protected void sendMessage(KafkaProducer<String, Product> kafkaProducer, Product product)  {
        var productRecord = new ProducerRecord<>(TopicName.CUSTOM_SERIALIZER_TOPIC.getName(), product.code(), product);

        kafkaProducer.send(productRecord, callback);
    }

    @Override
    protected KafkaProducer<String, Product> getKafkaProducer() {
        return new KafkaProducer<>(getProperties());
    }
}
