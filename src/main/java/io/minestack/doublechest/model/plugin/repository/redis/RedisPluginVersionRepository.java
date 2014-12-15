package io.minestack.doublechest.model.plugin.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.plugin.PluginVersion;

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
