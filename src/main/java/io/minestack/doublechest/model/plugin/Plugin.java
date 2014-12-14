package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Plugin extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private PluginType type;

    @Getter
    @Setter
    private String directory;

    @Getter
    private ArrayList<PluginVersion> versions = new ArrayList<>();

    @Getter
    private ArrayList<PluginConfig> configs = new ArrayList<>();

    @Override
    public String getKey() {
        return "plugin:"+name;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("name", name);
        hash.put("description", description);
        hash.put("type", type);
        hash.put("directory", directory);
        JSONArray versions = new JSONArray();
        for (PluginVersion version : this.versions) {
            versions.put(version.getKey());
        }
        hash.put("versions", versions);
        JSONArray configs = new JSONArray();
        for (PluginConfig config : this.configs) {
            configs.put(config.getKey());
        }
        hash.put("configs", configs);
        return hash;
    }
}
