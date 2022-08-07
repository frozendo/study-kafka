package com.frozendo.study.consumer.config;

import com.frozendo.study.entity.Product;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Map;

public class Base64Deserializer implements Deserializer<Product> {

    private final Logger logger = LoggerFactory.getLogger(Base64Deserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        logger.info("Init custom deserializer with configs {} and isKey = {}", configs, isKey);
    }

    @Override
    public Product deserialize(String topic, byte[] data) {
        logger.info("Using custom serializer to receive bytes {}", data);
        return productCustomDeserializer(data);
    }

    @Override
    public Product deserialize(String topic, Headers headers, byte[] data) {
        logger.info("Using custom serializer to receive bytes {} with headers {}", data, headers);
        return productCustomDeserializer(data);
    }

    @Override
    public void close() {
        logger.info("Close custom serializer");
    }

    private Product productCustomDeserializer(byte[] data) {
        try {
            byte[] base64decodedBytes = Base64.getDecoder().decode(data);
            InputStream in = new ByteArrayInputStream(base64decodedBytes);
            ObjectInputStream obin = new ObjectInputStream(in);
            return (Product) obin.readObject();
        } catch (ClassNotFoundException | IOException e) {
            logger.error("Error on deserializer base64 - {}", e.getMessage());
            return null;
        }
    }
}
