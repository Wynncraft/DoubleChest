package io.minestack.doublechest.model.network;

import io.minestack.doublechest.DoubleChest;
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
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("name", name);
        hash.put("description", description);
        JSONArray nodes = new JSONArray();
        for (NodeInfo nodeInfo : this.nodes) {
            nodes.put(nodeInfo.getKey());
        }
        hash.put("nodes", nodes.toString());
        JSONArray serverTypes = new JSONArray();
        for (ServerTypeInfo serverTypeInfo : this.serverTypes) {
            serverTypes.put(serverTypeInfo.getKey());
        }
        hash.put("serverTypes", serverTypes.toString());
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setName(hash.get("name"));
        setDescription(hash.get("description"));

        JSONArray nodes = new JSONArray(hash.get("nodes"));
        for (int i = 0; i < nodes.length(); i++) {
            String nodeKey = nodes.getString(i);
            NodeInfo nodeInfo = DoubleChest.INSTANCE.getRedisDatabase().getNodeInfoRepository().getModel(nodeKey);
            if (nodeInfo != null) {
                this.nodes.add(nodeInfo);
            }
        }

        JSONArray serverTypes = new JSONArray(hash.get("serverTypes"));
        for (int i = 0; i < serverTypes.length(); i++) {
            String serverTypeKey = serverTypes.getString(i);
            ServerTypeInfo serverTypeInfo = DoubleChest.INSTANCE.getRedisDatabase().getServerTypeInfoRepository().getModel(serverTypeKey);
            if (serverTypeInfo != null) {
                this.serverTypes.add(serverTypeInfo);
            }
        }
    }
}
