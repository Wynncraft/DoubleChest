package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class PluginVersion extends Model {

    @Getter
    @Setter
    private Plugin plugin;

    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String description;

    @Override
    public String getKey() {
        return plugin.getKey()+":version:"+version;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("version", version);
        hash.put("description", description);
        return hash;
    }
}
