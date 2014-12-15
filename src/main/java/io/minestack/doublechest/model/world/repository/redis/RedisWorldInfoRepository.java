package io.minestack.doublechest.model.world.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.world.WorldInfo;

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
