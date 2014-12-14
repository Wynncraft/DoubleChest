package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class PluginInfo extends Model {

    @Getter
    @Setter
    private Plugin plugin;

    @Getter
    @Setter
    private PluginVersion version;

    @Getter
    @Setter
    private PluginConfig config;

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("plugin", plugin);
        hash.put("version", version);
        hash.put("config", config);
        return hash;
    }
}
