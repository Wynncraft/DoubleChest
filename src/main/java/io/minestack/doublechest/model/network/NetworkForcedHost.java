package io.minestack.doublechest.model.network;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class NetworkForcedHost extends Model {

    @Getter
    @Setter
    private String host;

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private NetworkManualServerType manualServerType;

    @Getter
    @Setter
    private Network network;

    public NetworkForcedHost(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
