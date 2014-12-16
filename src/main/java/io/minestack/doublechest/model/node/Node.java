package io.minestack.doublechest.model.node;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.sql.Timestamp;
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
        return "node:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("name", name);
        hash.put("privateAddress", privateAddress);
        hash.put("ram", ram+"");

        JSONArray publicAddresses = new JSONArray();
        for (NodePublicAddress publicAddress : this.publicAddresses) {
            publicAddresses.put(publicAddress.getKey());
        }
        hash.put("publicAddresses", publicAddresses.toString());
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        setName(hash.get("name"));
        setPrivateAddress(hash.get("privateAddress"));
        setRam(Integer.parseInt("ram"));

        JSONArray publicAddresses = new JSONArray(hash.get("publicAddresses"));
        for (int i = 0; i < publicAddresses.length(); i++) {
            String publicAddressKey = publicAddresses.getString(i);
            NodePublicAddress publicAddress = DoubleChest.INSTANCE.getRedisDatabase().getNodePublicAddressRepository().getModel(publicAddressKey);
            if (publicAddress != null) {
                publicAddress.setNode(this);
                this.publicAddresses.add(publicAddress);
            }
        }
        setUpdated_at(new Timestamp(Long.parseLong("updated_at")));
    }
}
