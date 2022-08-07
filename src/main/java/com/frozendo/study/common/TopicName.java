package com.frozendo.study.common;

public enum TopicName {

    ASYNC_SEND_TOPIC("async-send-example"),
    CUSTOM_PARTITIONER_TOPIC("custom-partitioner-example"),
    CUSTOM_SERIALIZER_TOPIC("custom-serializer-example"),
    KEY_HASH_TOPIC("key-hash-example"),
    PARTITION_DIRECT_SEND_TOPIC("partition-direct-send-example"),
    ROUND_ROBIN_TOPIC("round-robin-topic-example");

    private String name;

    TopicName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
