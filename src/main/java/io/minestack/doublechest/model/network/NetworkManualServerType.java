package io.minestack.doublechest.model.network;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class NetworkManualServerType extends Model{

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private int port;

    public NetworkManualServerType(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
