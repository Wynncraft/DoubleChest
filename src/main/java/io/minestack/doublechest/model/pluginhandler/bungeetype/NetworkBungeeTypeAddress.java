package io.minestack.doublechest.model.pluginhandler.bungeetype;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.node.NodePublicAddress;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class NetworkBungeeTypeAddress extends Model {

    @Getter
    @Setter
    private NetworkBungeeType networkBungeeType;

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private NodePublicAddress publicAddress;

    public NetworkBungeeTypeAddress(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}