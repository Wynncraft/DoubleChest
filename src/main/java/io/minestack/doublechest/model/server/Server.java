package io.minestack.doublechest.model.server;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.type.servertype.ServerType;
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
        return network.getName() + ":server:" + serverType.getName() + ":" + getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("servertype", serverType.getKey());
        hash.put("network", network.getKey());
        hash.put("node", node.getKey());
        hash.put("lastUpdate", System.currentTimeMillis()+60000+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setId(Integer.parseInt(hash.get("id")));
        setServerType(DoubleChest.INSTANCE.getRedisDatabase().getServerTypeRepository().getModel(hash.get("servertype")));
        setNetwork(DoubleChest.INSTANCE.getRedisDatabase().getNetworkRepository().getModel(hash.get("network")));
        setNode(DoubleChest.INSTANCE.getRedisDatabase().getNodeRepository().getModel(hash.get("node")));
    }
}
