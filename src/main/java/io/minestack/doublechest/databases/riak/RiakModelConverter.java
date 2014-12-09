package io.minestack.doublechest.databases.riak;

import com.basho.riak.client.api.convert.Converter;
import com.basho.riak.client.core.util.BinaryValue;
import io.minestack.doublechest.model.Model;

import java.lang.reflect.Type;

public abstract class RiakModelConverter<T extends Model> extends Converter<T> {

    public RiakModelConverter(Type type) {
        super(type);
    }

    @Override
    public abstract T toDomain(BinaryValue binaryValue, String s);

    @Override
    public abstract ContentAndType fromDomain(T t);
}
