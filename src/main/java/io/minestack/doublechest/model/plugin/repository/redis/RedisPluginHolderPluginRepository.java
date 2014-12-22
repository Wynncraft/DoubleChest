package io.minestack.doublechest.model.plugin.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.plugin.PluginHolderPlugin;
import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisPluginHolderPluginRepository extends RedisModelRespository<PluginHolderPlugin> {

    public RedisPluginHolderPluginRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "pluginholder:{0}:{1}:plugins";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public PluginHolderPlugin getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getPluginHolderPluginModel") {
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
            PluginHolderPlugin pluginHolderPlugin = new PluginHolderPlugin();
            pluginHolderPlugin.fromHash(hashMap);
            return pluginHolderPlugin;
        }
        return null;
    }

    public void removeModel(PluginHolderPlugin pluginHolderPlugin, PluginHolder pluginHolder) throws Exception {
        String listKey = listKey(pluginHolder instanceof BungeeType ? 0 : 1, pluginHolderPlugin.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removePluginHolderPluginModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, pluginHolderPlugin.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(pluginHolderPlugin.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, pluginHolderPlugin.getKey());
                transaction.del(pluginHolderPlugin.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(PluginHolderPlugin model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("savePluginHolderPluginModel") {
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
                transaction.sadd(listKey(model.getPluginHolder() instanceof BungeeType ? 0 : 1, model.getPluginHolder().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
