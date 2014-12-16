package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
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
        return getPlugin().getKey()+":version:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("plugin", plugin.getKey());
        hash.put("version", version);
        hash.put("description", description);
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        //setPlugin(DoubleChest.INSTANCE.getRedisDatabase().getPluginRepository().getModel(hash.get("plugin")));
        setVersion(hash.get("version"));
        setDescription(hash.get("description"));
        setUpdated_at(new Timestamp(Long.parseLong("updated_at")));
    }
}
