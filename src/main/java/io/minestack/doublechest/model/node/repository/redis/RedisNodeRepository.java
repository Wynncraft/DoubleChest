package io.minestack.doublechest.model.node.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.node.Node;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisNodeRepository extends RedisModelRespository<Node> {

    public RedisNodeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        return "nodes";
    }

    @Override
    public Node getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getNodeModel") {
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
            Node node = new Node();
            node.fromHash(hashMap);
            return node;
        }
        return null;
    }

    public void removeModel(Node node) throws Exception {
        String listKey = listKey();
        getRedisDatabase().executeCommand(new RedisCommand("removeNodeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, node.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(node.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, node.getKey());
                transaction.del(node.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    @Override
    public void saveModel(Node model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveNodeModel") {
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
