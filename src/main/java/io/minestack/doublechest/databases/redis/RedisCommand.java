package io.minestack.doublechest.databases.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public abstract class RedisCommand {

    public abstract Object command(Jedis jedis) throws JedisException;

}
