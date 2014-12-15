package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

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
        hash.put("name", name);
        hash.put("description", description);
        hash.put("directory", directory);
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        setId(Integer.parseInt(hash.get("id")));
        setName(hash.get("name"));
        setDescription(hash.get("description"));
        setDirectory(hash.get("directory"));
    }
}
