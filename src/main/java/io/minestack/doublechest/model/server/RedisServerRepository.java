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

public class RedisServerRepository extends RedisModelRespository<Server> {

    public RedisServerRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
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
                String key = "server:" + model.getNetwork().getName() + ":" + model.getServerType().getName() + "" + model.getNumber();
                jedis.hset(key, "number", model.getNumber() + "");
                jedis.hset(key, "servertype", model.getServerType().getName());
                jedis.hset(key, "network", model.getNetwork().getName());
                jedis.hset(key, "lastUpdate", System.currentTimeMillis() + "");

                if (jedis.sismember("allservers:" + model.getNetwork().getName() + ":" + model.getServerType().getName(), model.getNumber() + "") == false) {
                    jedis.sadd("allservers:" + model.getNetwork().getName() + ":" + model.getServerType().getName(), model.getNumber() + "");
                }
                return 0;
            }
        });
    }

    public int getNextNumber(ServerType serverType, Network network) throws Exception {
        int nextNum = 1;

        List<String> list = getRedisDatabase().executeCommand(new RedisCommand() {
            @Override
            public Object command(Jedis jedis) throws JedisException {
                return jedis.sort("allservers:" + network.getName() + ":" + serverType.getName(), new SortingParams().by("server:" + network.getName() + ":" + serverType.getName() + ":*->number"));
            }
        }, List.class);

        while (list.contains(network.getName() + ":" + serverType.getName() + ":" + nextNum)) {
            nextNum += 1;
        }

        return nextNum;
    }
}
