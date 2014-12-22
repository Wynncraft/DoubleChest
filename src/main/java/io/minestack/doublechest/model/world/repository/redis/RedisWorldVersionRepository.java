package io.minestack.doublechest.model.world.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.world.World;
import io.minestack.doublechest.model.world.WorldVersion;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisWorldVersionRepository extends RedisModelRespository<WorldVersion> {

    public RedisWorldVersionRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "world:{0}:versions";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public WorldVersion getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getWorldVersionModel") {
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
            WorldVersion worldVersion = new WorldVersion();
            worldVersion.fromHash(hashMap);
            return worldVersion;
        }
        return null;
    }

    public void removeModel(WorldVersion worldVersion, World world) throws Exception {
        String listKey = listKey(world.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removeWorldVersionModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, worldVersion.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(worldVersion.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, worldVersion.getKey());
                transaction.del(worldVersion.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(WorldVersion model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveWorldVersionModel") {
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
                transaction.sadd(listKey(model.getWorld().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
