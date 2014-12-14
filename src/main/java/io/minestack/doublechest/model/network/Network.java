package io.minestack.doublechest.model.network;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.node.NodeInfo;
import io.minestack.doublechest.model.type.servertype.ServerTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

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
        return "network:"+name;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("name", name);
        hash.put("description", description);
        JSONArray nodes = new JSONArray();
        for (NodeInfo nodeInfo : this.nodes) {
            nodes.put(nodeInfo.getKey());
        }
        hash.put("nodes", nodes);
        JSONArray serverTypes = new JSONArray();
        for (ServerTypeInfo serverTypeInfo : this.serverTypes) {
            serverTypes.put(serverTypeInfo.getKey());
        }
        hash.put("serverTypes", serverTypes);
        return hash;
    }
}
