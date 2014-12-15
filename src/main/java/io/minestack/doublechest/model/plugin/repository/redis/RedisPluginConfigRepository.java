package io.minestack.doublechest.model.plugin.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.plugin.PluginConfig;

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
