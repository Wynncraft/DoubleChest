package io.minestack.doublechest.model.node;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import lombok.Getter;
import lombok.Setter;

public class NetworkNode extends Model {

    @Getter
    @Setter
    private Node node;

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private BungeeType bungeeType;

    @Getter
    @Setter
    private NodePublicAddress nodePublicAddress;

}
