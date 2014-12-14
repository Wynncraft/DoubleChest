package io.minestack.doublechest.model.type.servertype;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisServerTypeRepository extends RedisModelRespository<ServerType> {

    public RedisServerTypeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public ServerType getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(ServerType model) {

    }
}
