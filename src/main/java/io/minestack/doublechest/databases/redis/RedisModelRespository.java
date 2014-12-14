package io.minestack.doublechest.databases.redis;

import io.minestack.doublechest.model.Model;
import lombok.Getter;

public abstract class RedisModelRespository<T extends Model> {

    @Getter
    private final RedisDatabase redisDatabase;

    public RedisModelRespository(RedisDatabase redisDatabase) {
        this.redisDatabase = redisDatabase;
    }

    public abstract String listKey(String... replace);

    public abstract T getModel(String modelKey);

    public abstract void saveModel(T model);

}
