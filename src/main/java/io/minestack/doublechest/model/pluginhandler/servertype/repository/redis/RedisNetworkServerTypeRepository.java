package io.minestack.doublechest.model.pluginhandler.servertype.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisNetworkServerTypeRepository extends RedisModelRespository<NetworkServerType> {

    public RedisNetworkServerTypeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "network:{0}:servertypes";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public NetworkServerType getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getServerTypeInfoModel") {
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
            NetworkServerType networkServerType = new NetworkServerType();
            networkServerType.fromHash(hashMap);
            return networkServerType;
        }
        return null;
    }

    @Override
    public void saveModel(NetworkServerType model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveServerTypeInfoModel") {
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
                transaction.sadd(listKey(model.getNetwork().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
