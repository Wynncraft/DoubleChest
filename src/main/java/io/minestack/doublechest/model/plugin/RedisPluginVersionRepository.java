package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisPluginVersionRepository extends RedisModelRespository<PluginVersion> {

    public RedisPluginVersionRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public PluginVersion getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(PluginVersion model) {

    }
}
