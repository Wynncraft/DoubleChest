package io.minestack.doublechest.model.world.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.world.WorldVersion;

public class RedisWorldVersionRepository extends RedisModelRespository<WorldVersion> {

    public RedisWorldVersionRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public WorldVersion getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(WorldVersion model) {

    }
}
