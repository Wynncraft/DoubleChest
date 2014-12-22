package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;

public class PluginConfig extends Model {

    @Getter
    @Setter
    private Plugin plugin;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String directory;

    @Override
    public String getKey() {
        return getPlugin().getKey()+":config:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("plugin", plugin.getKey());
        hash.put("name", name);
        hash.put("description", description);
        hash.put("directory", directory);
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setId(Integer.parseInt(hash.get("id")));
        //setPlugin(DoubleChest.INSTANCE.getRedisDatabase().getPluginRepository().getModel(hash.get("plugin")));
        setName(hash.get("name"));
        setDescription(hash.get("description"));
        setDirectory(hash.get("directory"));
        setUpdated_at(new Timestamp(Long.parseLong(hash.get("updated_at"))));
    }
}
