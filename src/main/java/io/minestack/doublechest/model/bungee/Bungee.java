package io.minestack.doublechest.model.bungee;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import lombok.Getter;
import lombok.Setter;

public class Bungee extends Model {

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private BungeeType bungeeType;

    @Getter
    @Setter
    private Node node;

}
