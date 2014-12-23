package io.minestack.doublechest.model.pluginhandler.servertype;

import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import io.minestack.doublechest.model.world.ServerTypeWorld;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

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

    public ServerType(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
