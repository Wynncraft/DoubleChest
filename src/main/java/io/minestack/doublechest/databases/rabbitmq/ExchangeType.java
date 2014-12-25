package io.minestack.doublechest.databases.rabbitmq;

import lombok.Getter;

public enum ExchangeType {

    DIRECT("direct"),
    TOPIC("topic"),
    HEADERS("headers"),
    FANOUT("fanout");

    @Getter
    private final String value;

    ExchangeType(String value) {
        this.value = value;
    }
}
