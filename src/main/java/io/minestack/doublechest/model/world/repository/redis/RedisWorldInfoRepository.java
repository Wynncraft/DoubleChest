package io.minestack.doublechest.model.world.repository.redis;

import io.minestack.doublechest.databases.redis.RedisCommand;
import io.minestack.doublechest.databases.redis.RedisDatabase;
import io.minestack.doublechest.databases.redis.RedisModelRespository;
import io.minestack.doublechest.model.world.WorldInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;

public class RedisWorldInfoRepository extends RedisModelRespository<WorldInfo> {

    public RedisWorldInfoRepository(RedisDatabase redisDatabase) {
        super(redisDatabase);
    }

    @Override
    public String listKey(int... replace) {
        String key = "servertype:{0}:worlds";
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                key = key.replace("{" + i + "}", replace[i]+"");
            }
        }
        return key;
    }

    @Override
    public WorldInfo getModel(String modelKey) throws Exception {
        HashMap hashMap = getRedisDatabase().executeCommand(new RedisCommand("getWorldInfoModel") {
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
            WorldInfo worldInfo = new WorldInfo();
            worldInfo.fromHash(hashMap);
            return worldInfo;
        }
        return null;
    }

    @Override
    public void saveModel(WorldInfo model) throws Exception {
        getRedisDatabase().executeCommand(new RedisCommand("saveWorldInfoModel") {
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
                transaction.sadd(listKey(model.getServerType().getId()), model.getKey());
            }

            @Override
            public Object response() {
                return null;
            }
        });
    }
}
