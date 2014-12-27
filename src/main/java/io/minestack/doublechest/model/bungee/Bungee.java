package io.minestack.doublechest.model.bungee;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.node.NodePublicAddress;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class Bungee extends Model {

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private BungeeType bungeeType;

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private NodePublicAddress publicAddress;

    @Getter
    @Setter
    private String containerId;

    public Bungee(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
