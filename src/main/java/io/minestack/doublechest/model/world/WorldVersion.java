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
        return getWorld().getKey()+":version:"+version;
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("version", version);
        hash.put("description", description);
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setVersion(hash.get("version"));
        setDescription(hash.get("description"));
    }
}
