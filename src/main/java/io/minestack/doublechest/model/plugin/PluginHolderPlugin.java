package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;

public class PluginHolderPlugin extends Model {

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
        return pluginHolder.getKey()+":plugin:"+plugin.getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("pluginHolder", pluginHolder.getKey());
        hash.put("plugin", plugin.getKey());
        hash.put("version", version.getKey());
        if (config != null) {
            hash.put("config", config.getKey());
        }
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        String pluginHolderKey = hash.get("pluginHolder");
        /*if (pluginHolderKey.contains(":servertype:")) {
            setPluginHolder(DoubleChest.INSTANCE.getRedisDatabase().getServerTypeRepository().getModel(pluginHolderKey));
        } else if (pluginHolderKey.contains(":bungeetype:")) {
            setPluginHolder(DoubleChest.INSTANCE.getRedisDatabase().getBungeeTypeRepository().getModel(pluginHolderKey));
        }*/
        setPlugin(DoubleChest.INSTANCE.getRedisDatabase().getPluginRepository().getModel(hash.get("plugin")));
        setVersion(DoubleChest.INSTANCE.getRedisDatabase().getPluginVersionRepository().getModel(hash.get("version")));
        if (hash.containsKey("config")) {
            setConfig(DoubleChest.INSTANCE.getRedisDatabase().getPluginConfigRepository().getModel(hash.get("config")));
        }
        setUpdated_at(new Timestamp(Long.parseLong("updated_at")));
    }
}
