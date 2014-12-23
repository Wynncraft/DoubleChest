package io.minestack.doublechest.model.pluginhandler.bungeetype;

import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class BungeeType extends PluginHolder {

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int ram;

    public BungeeType(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
