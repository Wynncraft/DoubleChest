package io.minestack.doublechest.model.node;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisNodePublicAddressRepository extends RedisModelRespository<NodePublicAddress> {

    public RedisNodePublicAddressRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public NodePublicAddress getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(NodePublicAddress model) {

    }
}
