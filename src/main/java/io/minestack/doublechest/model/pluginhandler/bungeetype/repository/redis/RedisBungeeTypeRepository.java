package io.minestack.doublechest.model.pluginhandler.bungeetype.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisBungeeTypeRepository extends RedisModelRespository<BungeeType> {

    public RedisBungeeTypeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        return "bungeetypes";
    }

    @Override
    public BungeeType getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getBungeeTypeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{modelKey};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(modelKey);
            }

            @Override
            public void command(Transaction transaction) {
                getResponses().put("hash", transaction.hgetAll(modelKey));
            }

            @Override
            public Object response() {
                return getResponses().get("hash").get();
            }
        }, HashMap.class);
        if (hashMap != null) {
            BungeeType bungeeType = new BungeeType();
            bungeeType.fromHash(hashMap);
            return bungeeType;
        }
        return null;
    }

    public void removeModel(BungeeType bungeeType) throws Exception {
        String listKey = listKey();
        getRedisDatabase().executeCommand(new RedisCommand("removeBungeeTypeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, bungeeType.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(bungeeType.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, bungeeType.getKey());
                transaction.del(bungeeType.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(BungeeType model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveBungeeTypeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[0];
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(model.getKey()) == false;
            }

            @Override
            public void command(Transaction transaction) {
                transaction.hmset(model.getKey(), model.toHash());
                transaction.sadd(listKey(), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
