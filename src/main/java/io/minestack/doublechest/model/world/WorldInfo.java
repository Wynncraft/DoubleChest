package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class WorldInfo extends Model {

    @Getter
    @Setter
    private World world;

    @Getter
    @Setter
    private WorldVersion version;

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("world", world);
        hash.put("version", version);
        return hash;
    }
}
