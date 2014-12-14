package io.minestack.doublechest.model.node;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;

public class RedisNodeRepository extends RedisModelRespository<Node> {

    public RedisNodeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public Node getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(Node model) {

    }

    @Override
    public void removeModel(String modelKey) {

    }
}
