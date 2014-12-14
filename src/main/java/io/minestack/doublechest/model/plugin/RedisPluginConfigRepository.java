package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisPluginConfigRepository extends RedisModelRespository<PluginConfig> {

    public RedisPluginConfigRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public PluginConfig getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(PluginConfig model) {

    }
}
