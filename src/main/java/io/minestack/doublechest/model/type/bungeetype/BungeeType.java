package io.minestack.doublechest.model.type.bungeetype;

import io.minestack.doublechest.model.plugin.PluginInfo;
import io.minestack.doublechest.model.type.PluginHolder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.HashMap;

public class BungeeType extends PluginHolder {

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int ram;

    @Override
    public String getKey() {
        return "bungeetype:"+getName();
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("name", getName());
        hash.put("description", description);
        hash.put("ram", ram);
        JSONArray plugins = new JSONArray();
        for (PluginInfo pluginInfo : getPlugins()) {
            plugins.put(pluginInfo.getKey());
        }
        hash.put("plugins", plugins.toString());
        return hash;
    }
}
