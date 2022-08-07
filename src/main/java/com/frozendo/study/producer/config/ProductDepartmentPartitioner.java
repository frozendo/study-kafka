package com.frozendo.study.producer.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ProductDepartmentPartitioner implements Partitioner {

    final Logger logger = LoggerFactory.getLogger(ProductDepartmentPartitioner.class);

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        var keyValue = (String) key;
        var partitionValue = switch (keyValue) {
            case "Electronic" -> 0;
            case "Furniture" -> 1;
            case "Home Appliance" -> 2;
            default -> 3;
        };
        logger.info("Send message to partition {}", partitionValue);
        return partitionValue;
    }

    @Override
    public void close() {
        logger.info("Close custom partitioner!");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        logger.info("Execute configure with size {} and details {}", configs.size(), configs);
    }
}
