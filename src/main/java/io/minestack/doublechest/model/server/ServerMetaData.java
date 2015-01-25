package io.minestack.doublechest.model.server;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class ServerMetaData extends Model {

    @Getter
    @Setter
    private String key;

    @Getter
    @Setter
    private String value;

    public ServerMetaData(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
