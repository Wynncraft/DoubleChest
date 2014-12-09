package io.minestack.doublechest.databases.riak;

import com.basho.riak.client.api.convert.Converter;
import com.basho.riak.client.api.convert.ConverterFactory;
import io.minestack.doublechest.model.Model;

public abstract class RiakModelConverter<T extends Model> extends Converter<T> {

    public RiakModelConverter(Class<T> model) {
        super(model);
        ConverterFactory.getInstance().registerConverterForClass(model, this);
    }
}
