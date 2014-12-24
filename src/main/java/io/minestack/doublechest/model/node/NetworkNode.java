package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class NetworkNode extends Model {

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private BungeeType bungeeType;

    @Getter
    @Setter
    private NodePublicAddress nodePublicAddress;

    public NetworkNode(ObjectId id, Date created_at) {
        super(id, created_at);
    }

}
