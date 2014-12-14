package io.minestack.doublechest.model.bungee;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.bungeetype.BungeeType;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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

    @Override
    public String getKey() {
        return network.getName() + ":bungee:" + bungeeType.getName() + ":" + node.getName();
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("bungeetype", bungeeType.getName());
        hash.put("network", network.getName());
        hash.put("node", node.getName());
        hash.put("lastUpdate", System.currentTimeMillis());
        return hash;
    }
}
