package io.minestack.doublechest.model.plugin.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RedisPluginRepository extends RedisModelRespository<Plugin> {

    public RedisPluginRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        return "plugins";
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Plugin> getModels() throws Exception {
        ArrayList<Plugin> plugins = new ArrayList<>();

        Set<String> pluginKeys = getRedisDatabase().executeCommand(new RedisCommand("") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey());
            }

            @Override
            public void command(Transaction transaction) {
                getResponses().put("pluginKeys", transaction.smembers(listKey()));
            }

            @Override
            public Object response() {
                return getResponses().get("pluginKeys").get();
            }
        }, Set.class);

        for (String pluginKey : pluginKeys) {
            Plugin plugin = getModel(pluginKey);
            if (plugin != null) {
                plugins.add(plugin);
            }
        }

        return plugins;
    }

    @Override
    public Plugin getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getPluginModel") {
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
            Plugin plugin = new Plugin();
            plugin.fromHash(hashMap);
            return plugin;
        }
        return null;
    }

    public void removeModel(Plugin plugin) throws Exception {
        String listKey = listKey();
        getRedisDatabase().executeCommand(new RedisCommand("removePluginModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, plugin.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(plugin.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, plugin.getKey());
                transaction.del(plugin.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(Plugin model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("savePluginModel") {
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
