package io.minestack.doublechest.model.pluginhandler.servertype;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.plugin.PluginHolderPlugin;
import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import io.minestack.doublechest.model.world.ServerTypeWorld;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.sql.Timestamp;
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
    private ArrayList<ServerTypeWorld> worlds = new ArrayList<>();

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
        for (PluginHolderPlugin pluginHolderPlugin : getPlugins()) {
            plugins.put(pluginHolderPlugin.getKey());
        }
        hash.put("plugins", plugins.toString());
        JSONArray worlds = new JSONArray();
        for (ServerTypeWorld serverTypeWorld : this.worlds) {
            worlds.put(serverTypeWorld.getKey());
        }
        hash.put("worlds", worlds.toString());
        hash.put("updated_at", getUpdated_at().getTime()+"");
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
            PluginHolderPlugin pluginHolderPlugin = DoubleChest.INSTANCE.getRedisDatabase().getPluginInfoRepository().getModel(pluginKey);
            if (pluginHolderPlugin != null) {
                pluginHolderPlugin.setPluginHolder(this);
                getPlugins().add(pluginHolderPlugin);
            }
        }
        JSONArray worlds = new JSONArray(hash.get("worlds"));
        for (int i = 0; i < worlds.length(); i++) {
            String worldKey = worlds.getString(i);
            ServerTypeWorld serverTypeWorld = DoubleChest.INSTANCE.getRedisDatabase().getWorldInfoRepository().getModel(worldKey);
            if (serverTypeWorld != null) {
                serverTypeWorld.setServerType(this);
                this.worlds.add(serverTypeWorld);
            }
        }
        setUpdated_at(new Timestamp(Long.parseLong("updated_at")));
    }
}
