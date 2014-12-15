package io.minestack.doublechest.model.pluginhandler.servertype;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.plugin.PluginInfo;
import io.minestack.doublechest.model.pluginhandler.PluginHolder;
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
        return "servertype:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("name", getName());
        hash.put("description", description);
        hash.put("ram", ram+"");
        hash.put("players", players+"");
        JSONArray plugins = new JSONArray();
        for (PluginInfo pluginInfo : getPlugins()) {
            plugins.put(pluginInfo.getKey());
        }
        hash.put("plugins", plugins.toString());
        JSONArray worlds = new JSONArray();
        for (WorldInfo worldInfo : this.worlds) {
            worlds.put(worldInfo.getKey());
        }
        hash.put("worlds", worlds.toString());
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt("id"));
        setName(hash.get("name"));
        setDescription(hash.get("description"));
        setRam(Integer.parseInt(hash.get("ram")));
        setPlayers(Integer.parseInt(hash.get("players")));
        JSONArray plugins = new JSONArray(hash.get("plugins"));
        for (int i = 0; i < plugins.length(); i++) {
            String pluginKey = plugins.getString(i);
            PluginInfo pluginInfo = DoubleChest.INSTANCE.getRedisDatabase().getPluginInfoRepository().getModel(pluginKey);
            if (pluginInfo != null) {
                getPlugins().add(pluginInfo);
            }
        }
        JSONArray worlds = new JSONArray(hash.get("worlds"));
        for (int i = 0; i < worlds.length(); i++) {
            String worldKey = worlds.getString(i);
            WorldInfo worldInfo = DoubleChest.INSTANCE.getRedisDatabase().getWorldInfoRepository().getModel(worldKey);
            if (worldInfo != null) {
                this.worlds.add(worldInfo);
            }
        }
    }
}
