package io.minestack.doublechest.model.plugin.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.plugin.PluginInfo;

public class RedisPluginInfoRepository extends RedisModelRespository<PluginInfo> {

    public RedisPluginInfoRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public PluginInfo getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(PluginInfo model) {

    }
}
