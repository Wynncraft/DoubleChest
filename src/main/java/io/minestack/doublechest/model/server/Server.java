package io.minestack.doublechest.model.server;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
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
        return network.getKey() + ":server:" + serverType.getId() + ":" + getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("servertype", serverType.getKey());
        hash.put("network", network.getKey());
        hash.put("node", node.getKey());
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        setServerType(DoubleChest.INSTANCE.getRedisDatabase().getServerTypeRepository().getModel(hash.get("servertype")));
        setNetwork(DoubleChest.INSTANCE.getRedisDatabase().getNetworkRepository().getModel(hash.get("network")));
        setNode(DoubleChest.INSTANCE.getRedisDatabase().getNodeRepository().getModel(hash.get("node")));
        setUpdated_at(new Timestamp(Long.parseLong("updated_at")));
    }
}
