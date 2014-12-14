package io.minestack.doublechest.model.type.servertype;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class ServerTypeInfo extends Model {

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private int amount;

    @Getter
    @Setter
    private boolean defaultType;

    @Override
    public String getKey() {
        return getNetwork().getKey()+":servertype:"+serverType.getName();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("network", network.getKey());
        hash.put("servertype", serverType.getKey());
        hash.put("amount", amount+"");
        hash.put("default", defaultType+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setNetwork(DoubleChest.INSTANCE.getRedisDatabase().getNetworkRepository().getModel(hash.get("network")));
        setServerType(DoubleChest.INSTANCE.getRedisDatabase().getServerTypeRepository().getModel(hash.get("servertype")));
        setAmount(Integer.parseInt(hash.get("amount")));
        setDefaultType(Boolean.parseBoolean(hash.get("default")));
    }
}
