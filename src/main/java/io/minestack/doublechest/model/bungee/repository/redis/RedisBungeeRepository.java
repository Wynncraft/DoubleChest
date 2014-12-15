package io.minestack.doublechest.model.bungee.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.bungee.Bungee;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import io.minestack.doublechest.model.server.Server;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisBungeeRepository extends RedisModelRespository<Bungee> {

    public RedisBungeeRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "network:{0}:bungeetype:{1}:bungees";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public Bungee getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getBungeeModel") {
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
            Bungee bungee = new Bungee();
            bungee.fromHash(hashMap);
            return bungee;
        }
        return null;
    }

    @Override
    public void saveModel(Bungee model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveBungeeModel") {
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
                transaction.sadd(listKey(model.getNetwork().getId(), model.getBungeeType().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    public void removeModel(Server server, Network network, BungeeType bungeeType) throws Exception {
        String listKey = listKey(network.getId(), bungeeType.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removeBungeeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, server.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(server.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, server.getKey());
                transaction.del(server.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
