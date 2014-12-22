package io.minestack.doublechest.model.plugin.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.plugin.Plugin;
import io.minestack.doublechest.model.plugin.PluginVersion;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisPluginVersionRepository extends RedisModelRespository<PluginVersion> {

    public RedisPluginVersionRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "plugin:{0}:versions";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public PluginVersion getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getPluginVersionModel") {
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
            PluginVersion pluginVersion = new PluginVersion();
            pluginVersion.fromHash(hashMap);
            return pluginVersion;
        }
        return null;
    }

    public void removeModel(PluginVersion pluginVersion, Plugin plugin) throws Exception {
        String listKey = listKey(plugin.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removePluginVersionModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, pluginVersion.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(pluginVersion.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, pluginVersion.getKey());
                transaction.del(pluginVersion.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(PluginVersion model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("savePluginVersionModel") {
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
                transaction.sadd(listKey(model.getPlugin().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
