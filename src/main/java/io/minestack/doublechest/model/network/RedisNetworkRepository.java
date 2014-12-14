package io.minestack.doublechest.model.network;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisNetworkRepository extends RedisModelRespository<Network> {

    public RedisNetworkRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public Network getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(Network model) {

    }

    @Override
    public void removeModel(String modelKey) {

    }
}
