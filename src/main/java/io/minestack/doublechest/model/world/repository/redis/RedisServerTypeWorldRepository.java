package io.minestack.doublechest.model.world.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.world.ServerTypeWorld;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisServerTypeWorldRepository extends RedisModelRespository<ServerTypeWorld> {

    public RedisServerTypeWorldRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "servertype:{0}:worlds";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public ServerTypeWorld getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getServerTypeWorldModel") {
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
            ServerTypeWorld serverTypeWorld = new ServerTypeWorld();
            serverTypeWorld.fromHash(hashMap);
            return serverTypeWorld;
        }
        return null;
    }

    public void removeModel(ServerTypeWorld serverTypeWorld, ServerType serverType) throws Exception {
        String listKey = listKey(serverType.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removeServerTypeWorldModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, serverTypeWorld.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(serverTypeWorld.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, serverTypeWorld.getKey());
                transaction.del(serverTypeWorld.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(ServerTypeWorld model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveServerTypeWorldModel") {
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
                transaction.sadd(listKey(model.getServerType().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
