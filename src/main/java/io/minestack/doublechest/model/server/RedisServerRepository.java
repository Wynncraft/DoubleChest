package io.minestack.doublechest.model.server;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.servertype.ServerType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;

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
        return null;
    }

    @Override
    public void saveModel(Server model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {

                for (Map.Entry<String, Object> hashFields : model.toHash().entrySet()) {
                    jedis.hset(model.getKey(), hashFields.getKey(), hashFields.getValue().toString());
                }

                if (jedis.sismember(listKey(model.getNetwork().getName(), model.getServerType().getName()), model.getId() + "") == false) {
                    jedis.sadd(listKey(model.getNetwork().getName(), model.getServerType().getName()), model.getId() + "");
                }
                return 0;
            }
        });
    }

    @Override
    public void removeModel(String modelKey) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                return jedis.del(modelKey);
            }
        });
    }

    public int getNextNumber(Network network, ServerType serverType) throws Exception {
        int nextNum = 1;

        List list = getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                return jedis.sort(listKey(network.getName(), serverType.getName()), new SortingParams().by(network.getName() + ":servers:" + serverType.getName() + ":*->id"));
            }
        }, List.class);

        while (list.contains(nextNum+"")) {
            nextNum += 1;
        }

        return nextNum;
    }
}
