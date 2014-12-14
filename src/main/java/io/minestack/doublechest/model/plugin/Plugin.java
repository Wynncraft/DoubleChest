package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.DoubleChest;
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
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("name", name);
        hash.put("description", description);
        hash.put("type", type.name());
        hash.put("directory", directory);
        JSONArray versions = new JSONArray();
        for (PluginVersion version : this.versions) {
            versions.put(version.getKey());
        }
        hash.put("versions", versions.toString());
        JSONArray configs = new JSONArray();
        for (PluginConfig config : this.configs) {
            configs.put(config.getKey());
        }
        hash.put("configs", configs.toString());
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setName(hash.get("name"));
        setDescription(hash.get("description"));
        setType(PluginType.valueOf(hash.get("type")));
        setDirectory(hash.get("directory"));

        JSONArray versions = new JSONArray(hash.get("versions"));
        for (int i = 0; i < versions.length(); i++) {
            String versionKey = versions.getString(i);
            PluginVersion version = DoubleChest.INSTANCE.getRedisDatabase().getPluginVersionRepository().getModel(versionKey);
            if (version != null) {
                this.versions.add(version);
            }
        }

        JSONArray configs = new JSONArray(hash.get("configs"));
        for (int i = 0; i < configs.length(); i++) {
            String configKey = configs.getString(i);
            PluginConfig config = DoubleChest.INSTANCE.getRedisDatabase().getPluginConfigRepository().getModel(configKey);
            if (config != null) {
                this.configs.add(config);
            }
        }
    }
}
