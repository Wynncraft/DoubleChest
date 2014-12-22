package io.minestack.doublechest.model.pluginhandler.servertype;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.network.Network;
import lombok.Getter;
import lombok.Setter;

public class NetworkServerType extends Model {

    @Getter
    @Setter
    private Network network;

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private int amount;

    @Getter
    @Setter
    private boolean defaultServerType;

}
