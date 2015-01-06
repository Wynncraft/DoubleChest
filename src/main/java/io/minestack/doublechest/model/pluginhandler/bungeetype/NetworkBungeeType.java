package io.minestack.doublechest.model.pluginhandler.bungeetype;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NetworkBungeeType extends Model {

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private BungeeType bungeeType;

    @Getter
    @Setter
    private int amount;

    @Getter
    private Map<ObjectId, NetworkBungeeTypeAddress> addresses = new HashMap<>();

    public NetworkBungeeType(ObjectId id, Date created_at) {
        super(id, created_at);
    }

}
