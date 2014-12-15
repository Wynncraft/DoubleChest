package io.minestack.doublechest.model.pluginhandler.servertype.repository.redis;

import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerTypeInfo;

public class RedisServerTypeInfoRepository extends RedisModelRespository<ServerTypeInfo> {

    public RedisServerTypeInfoRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        return null;
    }

    @Override
    public ServerTypeInfo getModel(String modelKey) {
        return null;
    }

    @Override
    public void saveModel(ServerTypeInfo model) {

    }
}
