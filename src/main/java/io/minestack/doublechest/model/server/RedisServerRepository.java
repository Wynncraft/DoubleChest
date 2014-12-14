package io.minestack.doublechest.model.server;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.type.servertype.ServerType;
import io.minestack.doublechest.model.type.servertype.ServerTypeInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.List;

public class RedisServerRepository extends RedisModelRespository<Server> {

    public RedisServerRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(String... replace) {
        String key = "{0}:servers:{1}";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]);
            }
        }
        return key;
    }

    @Override
    public Server getModel(String modelKey) {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                if (jedis.exists(modelKey)) {
                    return jedis.hgetAll(modelKey);
                }
                return null;
            }
        }, HashMap.class);
        if (hashMap != null) {
            Server server = new Server();
            server.fromHash(hashMap);
            return server;
        }
        return null;
    }

    @Override
    public void saveModel(Server model) {
        getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                jedis.hmset(model.getKey(), model.toHash());

                if (jedis.sismember(listKey(model.getNetwork().getName(), model.getServerType().getName()), model.getKey()) == false) {
                    jedis.sadd(listKey(model.getNetwork().getName(), model.getServerType().getName()), model.getKey());
                }
                return 0;
            }
        });
    }

    public void removeModel(Server server, Network network, ServerType serverType) {
        getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                jedis.srem(listKey(network.getName(), serverType.getName()), server.getKey());
                return jedis.del(server.getKey());
            }
        });
    }

    public void removeModel(String serverKey, Network network, ServerType serverType) {
        getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                jedis.srem(listKey(network.getName(), serverType.getName()), serverKey);
                return jedis.del(serverKey);
            }
        });
    }

    public void removeTimedOut(Network network) {
        getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                for (ServerTypeInfo serverTypeInfo : network.getServerTypes()) {
                    for (String serverKey : jedis.smembers(listKey(network.getName(), serverTypeInfo.getServerType().getName()))) {
                        Long lastUpdate = Long.parseLong(jedis.hget(serverKey, "lastUpdate"));
                        if (lastUpdate + 30000 < System.currentTimeMillis()) {
                            removeModel(serverKey, network, serverTypeInfo.getServerType());
                        }
                    }
                }
                return null;
            }
        });
    }

    public int getNextNumber(Network network, ServerType serverType) {
        int nextNum = 1;

        List list = getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                return jedis.sort(listKey(network.getName(), serverType.getName()), new SortingParams().by("*->id"));
            }
        }, List.class);

        while (list.contains(network.getName()+":server:"+serverType.getName()+":"+nextNum)) {
            nextNum += 1;
        }

        return nextNum;
    }
}
