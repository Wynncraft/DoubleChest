package io.minestack.doublechest.model.type.servertype;

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
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("servertype", serverType.getName());
        hash.put("amount", amount);
        hash.put("default", defaultType);
        return hash;
    }
}
