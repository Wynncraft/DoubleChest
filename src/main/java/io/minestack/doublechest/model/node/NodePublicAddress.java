package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class NodePublicAddress extends Model {

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private String publicAddress;

    @Override
    public String getKey() {
        return node.getKey()+":public_address:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", node.getId()+"");
        hash.put("node", node.getKey());
        hash.put("publicAddress", publicAddress);
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        //setNode(DoubleChest.INSTANCE.getRedisDatabase().getNodeRepository().getModel(hash.get("node")));
        setPublicAddress(hash.get("publicAddress"));
    }
}
