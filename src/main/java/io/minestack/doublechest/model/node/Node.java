package io.minestack.doublechest.model.node;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Node extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String privateAddress;

    @Getter
    @Setter
    private int ram;

    @Getter
    private ArrayList<NodePublicAddress> publicAddresses = new ArrayList<>();

    @Override
    public String getKey() {
        return "node:"+name;
    }

    @Override
    public HashMap<String, String> toHash() {
        return null;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setName(hash.get("name"));
        setPrivateAddress(hash.get("privateAddress"));
        setRam(Integer.parseInt("ram"));

        JSONArray publicAddresses = new JSONArray(hash.get("publicAddresses"));
        for (int i = 0; i < publicAddresses.length(); i++) {
            String publicAddressKey = publicAddresses.getString(i);
            NodePublicAddress publicAddress = DoubleChest.INSTANCE.getRedisDatabase().getNodePublicAddressRepository().getModel(publicAddressKey);
            if (publicAddress != null) {
                this.publicAddresses.add(publicAddress);
            }
        }
    }
}
