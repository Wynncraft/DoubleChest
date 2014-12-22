package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

public class NodePublicAddress extends Model {

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private String publicAddress;


}
