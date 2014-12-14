package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisPluginRepository extends RedisModelRespository<Plugin> {

    public RedisPluginRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public Plugin getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(Plugin model) {

    }
}
