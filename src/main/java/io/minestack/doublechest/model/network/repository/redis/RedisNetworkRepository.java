package io.minestack.doublechest.model.network.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.network.Network;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisNetworkRepository extends RedisModelRespository<Network> {

    public RedisNetworkRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        return "networks";
    }

    @Override
    public Network getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getNetworkModel") {
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
            Network network = new Network();
            network.fromHash(hashMap);
            return network;
        }
        return null;
    }

    public void removeModel(Network network) throws Exception {
        String listKey = listKey();
        getRedisDatabase().executeCommand(new RedisCommand("removeNetworkModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, network.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(network.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, network.getKey());
                transaction.del(network.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(Network model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveNetworkModel") {
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
