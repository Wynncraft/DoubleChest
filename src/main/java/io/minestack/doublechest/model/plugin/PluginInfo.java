package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.type.PluginHolder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class PluginInfo extends Model {

    @Getter
    @Setter
    private PluginHolder pluginHolder;

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
        return pluginHolder.getKey()+":plugin:"+plugin.getName();
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("pluginHolder", pluginHolder.getName());
        hash.put("plugin", plugin.getName());
        hash.put("version", version.getVersion());
        hash.put("config", config.getName());
        return hash;
    }
}
