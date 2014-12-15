package io.minestack.doublechest.model.server.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerTypeInfo;
import io.minestack.doublechest.model.server.Server;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getServerModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{modelKey};
            }

            @Override
            public boolean conditional(Jedis jedis) throws JedisException {
                return jedis.exists(modelKey);
            }

            @Override
            public void command(Transaction transaction) throws JedisException {
                getResponses().put("hash", transaction.hgetAll(modelKey));
            }

            @Override
            public Object response() throws JedisException {
                return getResponses().get("hash").get();
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
        getRedisDatabase().executeCommand(new RedisCommand("saveServerModel") {
            @Override
            public String[] keysToWatch() {
                return new String[0];
            }

            @Override
            public boolean conditional(Jedis jedis) throws JedisException {
                return jedis.exists(model.getKey()) == false;
            }

            @Override
            public void command(Transaction transaction) throws JedisException {
                transaction.hmset(model.getKey(), model.toHash());
                transaction.sadd(listKey(model.getNetwork().getName(), model.getServerType().getName()), model.getKey());
            }

            @Override
            public Object response() throws JedisException {
                return null;
            }
        });
    }

    public void removeModel(Server server, Network network, ServerType serverType) {
        String listKey = listKey(network.getName(), serverType.getName());
        getRedisDatabase().executeCommand(new RedisCommand("removeServerModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, server.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) throws JedisException {
                return jedis.exists(listKey) && jedis.exists(server.getKey());
            }

            @Override
            public void command(Transaction transaction) throws JedisException {
                transaction.srem(listKey, server.getKey());
                transaction.del(server.getKey());
            }

            @Override
            public Object response() throws JedisException {
                return null;
            }
        });
    }

    public void removeModel(String serverKey, Network network, ServerType serverType) {
        String listKey = listKey(network.getName(), serverType.getName());
        getRedisDatabase().executeCommand(new RedisCommand("removeServerModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, serverKey};
            }

            @Override
            public boolean conditional(Jedis jedis) throws JedisException {
                return jedis.exists(listKey) && jedis.exists(serverKey);
            }

            @Override
            public void command(Transaction transaction) throws JedisException {
                transaction.srem(listKey, serverKey);
                transaction.del(serverKey);
            }

            @Override
            public Object response() throws JedisException {
                return null;
            }
        });
    }

    public void removeTimedOut(Network network) {
        for (ServerTypeInfo serverTypeInfo : network.getServerTypes()) {
            String listKey = listKey(network.getName(), serverTypeInfo.getServerType().getName());
            getRedisDatabase().executeCommand(new RedisCommand("removeTimedOutServers") {
                @Override
                public String[] keysToWatch() {
                    return new String[]{listKey};
                }

                @Override
                public boolean conditional(Jedis jedis) throws JedisException {
                    return jedis.exists(listKey);
                }

                @Override
                public void command(Transaction transaction) throws JedisException {
                    getResponses().put("serverKeys", transaction.smembers(listKey));
                }

                @Override
                public Object response() throws JedisException {
                    for (String serverKey : (Set<String>) getResponses().get("serverTypes").get()) {
                        getRedisDatabase().executeCommand(new RedisCommand("getTimedOutServer") {
                            @Override
                            public String[] keysToWatch() {
                                return new String[]{serverKey};
                            }

                            @Override
                            public boolean conditional(Jedis jedis) throws JedisException {
                                return jedis.exists(serverKey);
                            }

                            @Override
                            public void command(Transaction transaction) throws JedisException {
                                getResponses().put("lastUpdate", transaction.hget(serverKey, "lastUpdate"));
                            }

                            @Override
                            public Object response() throws JedisException {
                                Long lastUpdate = Long.parseLong((String)getResponses().get("lastUpdate").get());
                                if (lastUpdate + 30000 < System.currentTimeMillis()) {
                                    removeModel(serverKey, network, serverTypeInfo.getServerType());
                                }
                                return null;
                            }
                        });
                    }
                    return null;
                }
            });
        }
    }

    public int getNextNumber(Network network, ServerType serverType) {
        int nextNum = 1;
        String listKey = listKey(network.getName(), serverType.getName());

        List list = getRedisDatabase().executeCommand(new RedisCommand("getNextNumber") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey};
            }

            @Override
            public boolean conditional(Jedis jedis) throws JedisException {
                return jedis.exists(listKey);
            }

            @Override
            public void command(Transaction transaction) throws JedisException {
                getResponses().put("sortedKeys", transaction.sort(listKey, new SortingParams().by("*->id")));
            }

            @Override
            public Object response() throws JedisException {
                return getResponses().get("sortedKeys").get();
            }
        }, List.class);

        while (list.contains(network.getName()+":server:"+serverType.getName()+":"+nextNum)) {
            nextNum += 1;
        }

        return nextNum;
    }
}
