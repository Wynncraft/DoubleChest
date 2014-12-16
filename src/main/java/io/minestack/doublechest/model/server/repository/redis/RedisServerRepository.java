package io.minestack.doublechest.model.server.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import io.minestack.doublechest.model.server.Server;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Log4j2
public class RedisServerRepository extends RedisModelRespository<Server> {

    public RedisServerRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "network:{0}:servertype:{1}:servers";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i] + "");
            }
        }
        return key;
    }

    @Override
    public Server getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getServerModel") {
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
            Server server = new Server();
            server.fromHash(hashMap);
            return server;
        }
        return null;
    }

    @Override
    public void saveModel(Server model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveServerModel") {
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
                transaction.sadd(listKey(model.getNetwork().getId(), model.getServerType().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    public void removeModel(Server server, Network network, ServerType serverType) throws Exception {
        String listKey = listKey(network.getId(), serverType.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removeServerModel") {
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

    public void removeTimedOut(Network network) throws Exception {
        for (NetworkServerType networkServerType : network.getServerTypes()) {
            String listKey = listKey(network.getId(), networkServerType.getServerType().getId());
            getRedisDatabase().executeCommand(new RedisCommand("removeTimedOutServers") {
                @Override
                public String[] keysToWatch() {
                    return new String[]{listKey};
                }

                @Override
                public boolean conditional(Jedis jedis) {
                    return jedis.exists(listKey);
                }

                @Override
                public void command(Transaction transaction) {
                    getResponses().put("serverKeys", transaction.smembers(listKey));
                }

                @Override
                public Object response() {
                    for (String serverKey : (Set<String>) getResponses().get("serverKeys").get()) {
                        try {
                            Server server = getModel(serverKey);
                            if (server != null) {
                                if (server.getUpdated_at().after(new Timestamp(System.currentTimeMillis() - 30000)) == false) {
                                    removeModel(server, network, networkServerType.getServerType());
                                }
                            }
                        } catch (Exception e) {
                            log.error("Threw a Exception in RedisServerRepository::removeTimedOut::RedisCommand::response, full stack trace follows: ", e);
                        }
                    }
                    return null;
                }
            });
        }
    }

    public int getNextNumber(Network network, ServerType serverType) throws Exception {
        int nextNum = 1;
        String listKey = listKey(network.getId(), serverType.getId());

        List list = getRedisDatabase().executeCommand(new RedisCommand("getNextNumber") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey);
            }

            @Override
            public void command(Transaction transaction) {
                getResponses().put("sortedKeys", transaction.sort(listKey, new SortingParams().by("*->id")));
            }

            @Override
            public Object response() {
                return getResponses().get("sortedKeys").get();
            }
        }, List.class);

        if (list == null) {
            return -1;
        }

        while (list.contains(network.getName()+":server:"+serverType.getName()+":"+nextNum)) {
            nextNum += 1;
        }

        return nextNum;
    }
}
