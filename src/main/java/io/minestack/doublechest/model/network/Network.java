package io.minestack.doublechest.model.network;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.node.NetworkNode;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.pluginhandler.bungeetype.NetworkBungeeType;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Network extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    private Map<ObjectId, NetworkNode> nodes = new HashMap<>();

    @Getter
    private Map<ObjectId, NetworkServerType> serverTypes = new HashMap<>();

    @Getter
    private Map<ObjectId, NetworkBungeeType> bungeeTypes = new HashMap<>();


    public Network(ObjectId id, Date created_at) {
        super(id, created_at);
    }

    public Node findNodeForServer(ServerType serverType) {
        Node node = null;

        for (NetworkNode networkNode : nodes.values()) {
            if (networkNode.getNode().canFitServer(serverType)) {
                if (node == null) {
                    node = networkNode.getNode();
                    continue;
                }
                if (networkNode.getNode().getFreeRam() > node.getFreeRam()) {
                    node = networkNode.getNode();
                }
            }
        }

        return node;
    }
}
