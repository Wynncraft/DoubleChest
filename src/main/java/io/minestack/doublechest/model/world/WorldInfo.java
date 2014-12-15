package io.minestack.doublechest.model.world;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class WorldInfo extends Model {

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private World world;

    @Getter
    @Setter
    private WorldVersion version;

    @Override
    public String getKey() {
        return serverType.getKey()+":world:"+getWorld().getName();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("servertyype", serverType.getKey());
        hash.put("world", world.getKey());
        hash.put("version", version.getKey());
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setServerType(DoubleChest.INSTANCE.getRedisDatabase().getServerTypeRepository().getModel(hash.get("servertype")));
        setWorld(DoubleChest.INSTANCE.getRedisDatabase().getWorldRepository().getModel(hash.get("world")));
        setVersion(DoubleChest.INSTANCE.getRedisDatabase().getWorldVersionRepository().getModel("version"));
    }
}
