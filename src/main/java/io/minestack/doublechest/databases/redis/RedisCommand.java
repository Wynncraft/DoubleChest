package io.minestack.doublechest.databases.redis;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public abstract class RedisCommand {

    @Getter
    private HashMap<String, Response> responses = new HashMap<>();

    @Getter
    private final String commandName;

    protected RedisCommand(String commandName) {
        this.commandName = commandName;
    }


    public abstract String[] keysToWatch();

    public abstract boolean conditional(Jedis jedis);

    public abstract void command(Transaction transaction);

    public abstract Object response();
}
