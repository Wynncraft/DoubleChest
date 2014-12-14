package io.minestack.doublechest.model.server;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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

    @Override
    public String getKey() {
        return "server:" + network.getName() + ":" + serverType.getName() + "" + getId();
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("id", getId());
        hash.put("servertype", serverType.getName());
        hash.put("network", network.getName());
        hash.put("lastUpdate", System.currentTimeMillis());
        return hash;
    }
}
