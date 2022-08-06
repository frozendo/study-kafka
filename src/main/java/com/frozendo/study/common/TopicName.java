package com.frozendo.study.common;

public enum TopicName {

    ROUND_ROBIN_TOPIC("round-robin-topic-example"),
    KEY_HASH_MANUAL_COMMIT_TOPIC("key-hash-with-manual-commit-example"),
    PARTITION_DIRECT_SEND_TOPIC("partition-direct-send-example");

    private String name;

    TopicName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
