package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

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

    public Node(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
