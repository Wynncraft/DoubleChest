package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class WorldVersion extends Model {

    @Getter
    @Setter
    private World world;

    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String description;

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("version", version);
        hash.put("description", description);
        return hash;
    }
}
