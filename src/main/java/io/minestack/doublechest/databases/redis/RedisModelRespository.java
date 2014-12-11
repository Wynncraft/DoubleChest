package io.minestack.doublechest.databases.redis;

import io.minestack.doublechest.model.Model;

public abstract class RedisModelRespository<T extends Model> {

    public abstract T getModel(T model);

    public abstract void saveModel(T model);
}
