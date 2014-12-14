package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

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
    public HashMap<String, Object> toHash() {
        return null;
    }
}
