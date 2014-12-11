package io.minestack.doublechest.model.server;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;

public class Server extends Model {

    @Getter
    @Setter
    private int number;

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private Node node;
}
