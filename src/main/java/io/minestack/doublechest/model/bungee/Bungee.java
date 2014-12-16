package io.minestack.doublechest.model.bungee;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
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

    @Getter
    @Setter
    private long lastUpdate;

    @Override
    public String getKey() {
        return network.getKey() + ":bungee:" + bungeeType.getId() + ":" + node.getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("bungeetype", bungeeType.getKey());
        hash.put("network", network.getKey());
        hash.put("node", node.getKey());
        hash.put("lastUpdate", lastUpdate+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setBungeeType(DoubleChest.INSTANCE.getRedisDatabase().getBungeeTypeRepository().getModel(hash.get("bungeetype")));
        setNetwork(DoubleChest.INSTANCE.getRedisDatabase().getNetworkRepository().getModel(hash.get("network")));
        setNode(DoubleChest.INSTANCE.getRedisDatabase().getNodeRepository().getModel(hash.get("node")));
        setLastUpdate(Long.parseLong("lastUpdate"));
    }
}
