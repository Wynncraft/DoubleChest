package io.minestack.doublechest.model.type.bungeetype;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisBungeeTypeRepository extends RedisModelRespository<BungeeType> {

    public RedisBungeeTypeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public BungeeType getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(BungeeType model) {

    }
}
