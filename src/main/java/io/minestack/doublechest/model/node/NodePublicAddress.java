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
        return node.getKey()+":public_address:"+publicAddress;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("node", node.getName());
        hash.put("publicAddress", publicAddress);
        return hash;
    }
}
