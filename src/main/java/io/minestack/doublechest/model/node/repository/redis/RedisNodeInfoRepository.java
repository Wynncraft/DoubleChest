package io.minestack.doublechest.model.node.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.node.NodeInfo;

public class RedisNodeInfoRepository extends RedisModelRespository<NodeInfo> {

    public RedisNodeInfoRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public NodeInfo getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(NodeInfo model) {

    }
}
