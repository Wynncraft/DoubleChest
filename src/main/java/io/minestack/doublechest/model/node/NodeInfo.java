package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.type.bungeetype.BungeeType;
import io.minestack.doublechest.model.network.Network;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class NodeInfo extends Model {

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

    @Override
    public String getKey() {
        return getNetwork().getKey()+":node:"+node.getName();
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("node", node.getName());
        hash.put("network", network.getName());
        if (bungeeType != null) {
            hash.put("bungeeType", bungeeType.getName());
            hash.put("publicAddress", nodePublicAddress.getPublicAddress());
        }
        return hash;
    }
}
