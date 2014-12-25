package io.minestack.doublechest.databases.mongo;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@RequiredArgsConstructor
public abstract class MongoModelRepository<T extends Model> {

    @Getter
    private final MongoDatabase database;

    public abstract List<T> getModels();

    public abstract T getModel(ObjectId id);

    public abstract void saveModel(T model);

    public abstract void insertModel(T model);

    public abstract void removeModel(T model);
}
