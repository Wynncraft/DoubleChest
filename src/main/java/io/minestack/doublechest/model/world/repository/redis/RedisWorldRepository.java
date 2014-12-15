package io.minestack.doublechest.model.world.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.world.World;

public class RedisWorldRepository extends RedisModelRespository<World> {

    public RedisWorldRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public World getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(World model) {

    }
}
