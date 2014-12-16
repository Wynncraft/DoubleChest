package io.minestack.doublechest.model.network;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.node.NetworkNode;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.sql.Timestamp;
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
    private ArrayList<NetworkNode> nodes = new ArrayList<>();

    @Getter
    private ArrayList<NetworkServerType> serverTypes = new ArrayList<>();

    @Override
    public String getKey() {
        return "network:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("name", name);
        hash.put("description", description);
        JSONArray nodes = new JSONArray();
        for (NetworkNode networkNode : this.nodes) {
            nodes.put(networkNode.getKey());
        }
        hash.put("nodes", nodes.toString());
        JSONArray serverTypes = new JSONArray();
        for (NetworkServerType networkServerType : this.serverTypes) {
            serverTypes.put(networkServerType.getKey());
        }
        hash.put("serverTypes", serverTypes.toString());
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        setName(hash.get("name"));
        setDescription(hash.get("description"));

        JSONArray nodes = new JSONArray(hash.get("nodes"));
        for (int i = 0; i < nodes.length(); i++) {
            String nodeKey = nodes.getString(i);
            NetworkNode networkNode = DoubleChest.INSTANCE.getRedisDatabase().getNodeInfoRepository().getModel(nodeKey);
            if (networkNode != null) {
                this.nodes.add(networkNode);
            }
        }

        JSONArray serverTypes = new JSONArray(hash.get("serverTypes"));
        for (int i = 0; i < serverTypes.length(); i++) {
            String serverTypeKey = serverTypes.getString(i);
            NetworkServerType networkServerType = DoubleChest.INSTANCE.getRedisDatabase().getServerTypeInfoRepository().getModel(serverTypeKey);
            if (networkServerType != null) {
                this.serverTypes.add(networkServerType);
            }
        }
        setUpdated_at(new Timestamp(Long.parseLong("updated_at")));
    }
}
