package io.minestack.doublechest.databases.redis;

import io.minestack.doublechest.databases.Database;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Log4j2
public class RedisDatabase implements Database{

    private final HostAndPort jedisHost;
    private JedisPool pool;

    public RedisDatabase(HostAndPort jedisHost) {
        this.jedisHost = jedisHost;
    }

    @Override
    public void setupDatabase() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        config.setMaxTotal(20);
        config.setMaxIdle(50);

        pool = new JedisPool(config, jedisHost.getHost(), jedisHost.getPort());
    }

    public Object executeCommand(RedisCommand command) throws Exception {
        return executeCommand(command, Object.class);
    }

    public <T> T executeCommand(RedisCommand command, Class<T> resultClass) throws Exception {
        Jedis jedis = pool.getResource();
        Object result = command.command(jedis);
        pool.returnResource(jedis);
        return resultClass.cast(result);
    }

}
