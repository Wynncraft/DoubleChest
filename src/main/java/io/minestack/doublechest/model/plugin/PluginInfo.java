package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.DoubleChest;
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
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("pluginHolder", pluginHolder.getKey());
        hash.put("plugin", plugin.getKey());
        hash.put("version", version.getKey());
        hash.put("config", config.getKey());
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) {
        String pluginHolderKey = hash.get("pluginHolder");
        if (pluginHolderKey.contains(":servertype:")) {
            setPluginHolder(DoubleChest.INSTANCE.getRedisDatabase().getServerTypeRepository().getModel(pluginHolderKey));
        } else if (pluginHolderKey.contains(":bungeetype:")) {
            setPluginHolder(DoubleChest.INSTANCE.getRedisDatabase().getBungeeTypeRepository().getModel(pluginHolderKey));
        }
        setPlugin(DoubleChest.INSTANCE.getRedisDatabase().getPluginRepository().getModel(hash.get("plugin")));
        setVersion(DoubleChest.INSTANCE.getRedisDatabase().getPluginVersionRepository().getModel(hash.get("version")));
        setConfig(DoubleChest.INSTANCE.getRedisDatabase().getPluginConfigRepository().getModel(hash.get("config")));
    }
}
