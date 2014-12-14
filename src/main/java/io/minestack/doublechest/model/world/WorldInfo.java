package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.type.servertype.ServerType;
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
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("world", world.getName());
        hash.put("version", version.getVersion());
        return hash;
    }
}
