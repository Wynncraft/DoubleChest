package io.minestack.doublechest.model.network;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.node.NodeInfo;
import io.minestack.doublechest.model.servertype.ServerTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

public class Network extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    private ArrayList<NodeInfo> nodes = new ArrayList<>();

    @Getter
    private ArrayList<ServerTypeInfo> serverTypes = new ArrayList<>();

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public HashMap<String, Object> toHash() {
        return null;
    }
}
