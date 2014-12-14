package io.minestack.doublechest.model.type.servertype;

import io.minestack.doublechest.model.plugin.PluginInfo;
import io.minestack.doublechest.model.type.PluginHolder;
import io.minestack.doublechest.model.world.WorldInfo;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerType extends PluginHolder {

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int ram;

    @Getter
    @Setter
    private int players;

    @Getter
    private ArrayList<WorldInfo> worlds = new ArrayList<>();

    @Override
    public String getKey() {
        return "servertype:"+getName();
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("name", getName());
        hash.put("description", description);
        hash.put("ram", ram);
        hash.put("players", players);
        JSONArray plugins = new JSONArray();
        for (PluginInfo pluginInfo : getPlugins()) {
            plugins.put(pluginInfo.getKey());
        }
        hash.put("plugins", plugins.toString());
        JSONArray worlds = new JSONArray();
        for (WorldInfo worldInfo : this.worlds) {
            worlds.put(worldInfo.getKey());
        }
        hash.put("worlds", worlds);
        return hash;
    }
}
