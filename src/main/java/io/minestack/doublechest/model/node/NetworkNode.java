package io.minestack.doublechest.model.node;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;

public class NetworkNode extends Model {

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
        return getNetwork().getKey()+":node:"+node.getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("node", node.getKey());
        hash.put("network", network.getKey());
        if (bungeeType != null) {
            hash.put("bungeeType", bungeeType.getKey());
            hash.put("publicAddress", nodePublicAddress.getKey());
        }
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt("id"));
        setNode(DoubleChest.INSTANCE.getRedisDatabase().getNodeRepository().getModel(hash.get("node")));
        //setNetwork(DoubleChest.INSTANCE.getRedisDatabase().getNetworkRepository().getModel(hash.get("network")));
        if (hash.containsKey("bungeeType")) {
            setBungeeType(DoubleChest.INSTANCE.getRedisDatabase().getBungeeTypeRepository().getModel(hash.get("bungeeType")));
            setNodePublicAddress(DoubleChest.INSTANCE.getRedisDatabase().getNodePublicAddressRepository().getModel(hash.get("publicAddress")));
        }
        setUpdated_at(new Timestamp(Long.parseLong(hash.get("updated_at"))));
    }
}
