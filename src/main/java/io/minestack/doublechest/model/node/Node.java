package io.minestack.doublechest.model.node;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.bungee.Bungee;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.server.Server;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Node extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String privateAddress;

    @Getter
    @Setter
    private int ram;

    @Getter
    private Map<ObjectId, NodePublicAddress> publicAddresses = new HashMap<>();

    public Node(ObjectId id, Date created_at) {
        super(id, created_at);
    }

    public int getFreeRam() {
        int freeRam = ram;

        for (Server server : DoubleChest.INSTANCE.getMongoDatabase().getServerRepository().getNodeServers(this)) {
            freeRam -= server.getServerType().getRam();
        }

        for (Bungee bungee : DoubleChest.INSTANCE.getMongoDatabase().getBungeeRepository().getNodeBungees(this)) {
            freeRam -= bungee.getBungeeType().getRam();
        }

        return freeRam;
    }

    public boolean canFitBungee(BungeeType bungeeType) {
        return getFreeRam() >= bungeeType.getRam();
    }

    public boolean canFitServer(ServerType serverType) {
        return getFreeRam() >= serverType.getRam();
    }
}
