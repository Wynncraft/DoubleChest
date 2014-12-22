package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;

public class ServerTypeWorld extends Model {

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private World world;

    @Getter
    @Setter
    private WorldVersion version;

    @Getter
    @Setter
    private boolean defaultWorld;

}
