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
        return null;
    }

    @Override
    public HashMap<String, Object> toHash() {
        return null;
    }
}
