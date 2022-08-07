 package com.frozendo.study.producer;

import com.frozendo.study.entity.Product;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class Base64Serializer implements Serializer<Product> {

    private final Logger logger = LoggerFactory.getLogger(Base64Serializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        logger.info("Use custom serializer with configs {} nad isKey = {}", configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, Product data) {
        return productCustomSerializer(data);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Product data) {
        return productCustomSerializer(data);
    }

    @Override
    public void close() {
        logger.info("Close custom serializer");
    }

    private byte[] productCustomSerializer(Product data) {
        var base64 = Base64.getEncoder().encodeToString(data.toString().getBytes(StandardCharsets.UTF_8));
        return base64.getBytes(StandardCharsets.UTF_8);
    }

}
