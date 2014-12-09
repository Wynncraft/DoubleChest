package io.minestack.doublechest.databases.riak;

import com.basho.riak.client.api.annotations.RiakKey;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import io.minestack.doublechest.model.Model;

import java.util.concurrent.ExecutionException;


public class RiakModelRepository<T extends Model> {

    private final String bucketName;
    private final RiakDatabase database;
    private final RiakModelConverter modelConverter;

    public RiakModelRepository(String bucketName, RiakDatabase database, RiakModelConverter modelConverter) {
        this.bucketName = bucketName;
        this.database = database;
        this.modelConverter = modelConverter;
    }

    public T getModel(String key, Class<T> clazz) throws ExecutionException, InterruptedException {
        Namespace ns = new Namespace(bucketName);
        Location location = new Location(ns, key);

        FetchValue fetchValue = new FetchValue.Builder(location).build();
        FetchValue.Response response = database.getClient().execute(fetchValue);
        return response.getValue(clazz);
    }

    public void saveModel(T model) throws ExecutionException, InterruptedException {
        Namespace ns = new Namespace(bucketName);
        Location location = new Location(ns, model.getClass().getAnnotation(RiakKey.class).toString());

        StoreValue storeValue = new StoreValue.Builder(model).withLocation(location).build();
        database.getClient().execute(storeValue).getGeneratedKey();
    }
}
