package com.frozendo.study.common;

public enum TopicName {

    ROUND_ROBIN_TOPIC("round-robin-topic-example");

    private String name;

    TopicName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
