package io.minestack.doublechest;

import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import lombok.Getter;
import redis.clients.jedis.HostAndPort;

public class DoubleChest {

    public static DoubleChest INSTANCE;

    @Getter
    private RedisDatabase redisDatabase;

    @Getter
    private MySQLDatabase mySQLDatabase;

    public DoubleChest() {
        DoubleChest.INSTANCE = this;
    }

    public void initRedisDatabase(HostAndPort jedisHost) {
        redisDatabase = new RedisDatabase(jedisHost);
    }

    public void initMySQLDatabase(String userName, String password, String database, String address, int port) {
        mySQLDatabase = new MySQLDatabase(userName, password, database, address, port);
    }

}
