package io.minestack.doublechest.model.bungee.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.bungee.Bungee;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.NetworkNode;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Set;

@Log4j2
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

    public void removeModel(Bungee bungee, BungeeType bungeeType, Network network) throws Exception {
        String listKey = listKey(network.getId(), bungeeType.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removeBungeeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, bungee.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(bungee.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, bungee.getKey());
                transaction.del(bungee.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
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

    public void removeModel(Bungee bungee, Network network, BungeeType bungeeType) throws Exception {
        String listKey = listKey(network.getId(), bungeeType.getId());
        getRedisDatabase().executeCommand(new RedisCommand("removeBungeeModel") {
            @Override
            public String[] keysToWatch() {
                return new String[]{listKey, bungee.getKey()};
            }

            @Override
            public boolean conditional(Jedis jedis) {
                return jedis.exists(listKey) && jedis.exists(bungee.getKey());
            }

            @Override
            public void command(Transaction transaction) {
                transaction.srem(listKey, bungee.getKey());
                transaction.del(bungee.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }

    public void removeTimedOut(Network network) throws Exception {
        for (NetworkNode networkNode : network.getNodes()) {
            BungeeType bungeeType = networkNode.getBungeeType();
            if (bungeeType == null) {
                continue;
            }
            String listKey = listKey(network.getId(), bungeeType.getId());
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
                    getResponses().put("bungeeKeys", transaction.smembers(listKey));
                }

                @Override
                public Object response() {
                    for (String bungeeKey : (Set<String>) getResponses().get("bungeeKeys").get()) {
                        try {
                            Bungee bungee = getModel(bungeeKey);
                            if (bungee != null) {
                                if (bungee.getUpdated_at().after(new Timestamp(System.currentTimeMillis() - 30000)) == false) {
                                    removeModel(bungee, network, bungeeType);
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
}
