package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class NodePublicAddress extends Model {

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private String publicAddress;


    public NodePublicAddress(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
