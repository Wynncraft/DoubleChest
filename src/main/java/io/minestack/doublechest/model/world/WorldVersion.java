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
        return getWorld().getKey()+":version:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("world", world.getKey());
        hash.put("version", version);
        hash.put("description", description);
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        //setWorld(DoubleChest.INSTANCE.getRedisDatabase().getWorldRepository().getModel(hash.get("world")));
        setVersion(hash.get("version"));
        setDescription(hash.get("description"));
    }
}
