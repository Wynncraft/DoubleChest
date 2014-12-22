package io.minestack.doublechest.model.node.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.NetworkNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisNetworkNodeRepository extends RedisModelRespository<NetworkNode> {

    public RedisNetworkNodeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "network:{0}:nodes";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public NetworkNode getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getNetworkNodeModel") {
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
            NetworkNode networkNode = new NetworkNode();
            networkNode.fromHash(hashMap);
            return networkNode;
        }
        return null;
    }

    public void removeModel(NetworkNode networkNode, Network network) throws Exception {
        String listKey = listKey(network.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removeNetworkNodeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, networkNode.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(networkNode.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, networkNode.getKey());
                transaction.del(networkNode.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(NetworkNode model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveNetworkNodeModel") {
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
