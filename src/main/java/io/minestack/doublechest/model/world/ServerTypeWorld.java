package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

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

    public ServerTypeWorld(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
