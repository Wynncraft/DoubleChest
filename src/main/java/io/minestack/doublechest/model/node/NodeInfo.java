package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.bungeetype.BungeeType;
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
        return null;
    }

    @Override
    public HashMap<String, Object> toHash() {
        return null;
    }
}
