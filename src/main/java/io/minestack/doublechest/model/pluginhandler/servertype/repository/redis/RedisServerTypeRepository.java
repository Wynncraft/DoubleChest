package io.minestack.doublechest.model.pluginhandler.servertype.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisServerTypeRepository extends RedisModelRespository<ServerType> {

    public RedisServerTypeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        return "servertypes";
    }

    @Override
    public ServerType getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getServerTypeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{modelKey};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(modelKey);
            }

            @Override
            public void command(Transaction transaction) {
                getResponses().put("hash", transaction.hgetAll(modelKey));
            }

            @Override
            public Object response() {
                return getResponses().get("hash").get();
            }
        }, HashMap.class);
        if (hashMap != null) {
            ServerType serverType = new ServerType();
            serverType.fromHash(hashMap);
            return serverType;
        }
        return null;
    }

    public void removeModel(ServerType serverType) throws Exception {
        String listKey = listKey();
        getRedisDatabase().executeCommand(new RedisCommand("removeServerTypeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, serverType.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(serverType.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, serverType.getKey());
                transaction.del(serverType.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(ServerType model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveServerTypeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[0];
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(model.getKey()) == false;
            }

            @Override
            public void command(Transaction transaction) {
                transaction.hmset(model.getKey(), model.toHash());
                transaction.sadd(listKey(), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
