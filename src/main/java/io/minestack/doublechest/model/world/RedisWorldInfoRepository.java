package io.minestack.doublechest.model.world;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisWorldInfoRepository extends RedisModelRespository<WorldInfo> {

    public RedisWorldInfoRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public WorldInfo getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(WorldInfo model) {

    }
}
