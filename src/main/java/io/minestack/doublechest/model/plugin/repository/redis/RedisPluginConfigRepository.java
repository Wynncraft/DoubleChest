package io.minestack.doublechest.model.plugin.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.plugin.Plugin;
import io.minestack.doublechest.model.plugin.PluginConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisPluginConfigRepository extends RedisModelRespository<PluginConfig> {

    public RedisPluginConfigRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "plugin:{0}:configs";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public PluginConfig getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getPluginConfigModel") {
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
            PluginConfig pluginConfig = new PluginConfig();
            pluginConfig.fromHash(hashMap);
            return pluginConfig;
        }
        return null;
    }

    public void removeModel(PluginConfig pluginConfig, Plugin plugin) throws Exception {
        String listKey = listKey(plugin.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removePluginConfigModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, pluginConfig.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(pluginConfig.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, pluginConfig.getKey());
                transaction.del(pluginConfig.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(PluginConfig model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("savePluginConfigModel") {
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
