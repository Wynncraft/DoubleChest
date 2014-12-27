package io.minestack.doublechest.model.server;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class Server extends Model {

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private int port;

    @Getter
    @Setter
    private int number;

    @Getter
    @Setter
    private String containerId;

    @Getter
    @Setter
    private int players;

    public Server(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
