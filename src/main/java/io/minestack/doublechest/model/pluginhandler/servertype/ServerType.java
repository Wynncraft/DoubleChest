package io.minestack.doublechest.model.pluginhandler.servertype;

import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import io.minestack.doublechest.model.world.ServerTypeWorld;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class ServerType extends PluginHolder {

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int ram;

    @Getter
    @Setter
    private int players;

    @Getter
    private ArrayList<ServerTypeWorld> worlds = new ArrayList<>();

}
