 package com.frozendo.study.producer.config;

import com.frozendo.study.entity.Product;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Map;

public class Base64Serializer implements Serializer<Product> {

    private final Logger logger = LoggerFactory.getLogger(Base64Serializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        logger.info("Init custom serializer with configs {} and isKey = {}", configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, Product data) {
        logger.info("Using custom serializer to send product {}", data);
        return productCustomSerializer(data);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Product data) {
        logger.info("Using custom serializer to send product {} with headers {}", data, headers);
        return productCustomSerializer(data);
    }

    @Override
    public void close() {
        logger.info("Close custom serializer");
    }

    private byte[] productCustomSerializer(Product data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(data);
            oos.flush();
            return Base64.getEncoder().encode(bos.toByteArray());
        } catch (IOException e) {
            logger.error("Error on serializer in base64 - {}", e.getMessage());
            return new byte[]{};
        }
    }

}
