package io.minestack.doublechest.model.servertype;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.plugin.PluginInfo;
import io.minestack.doublechest.model.world.WorldInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerType extends Model {

    @Getter
    @Setter
    private String name;

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
    private ArrayList<PluginInfo> plugins = new ArrayList<>();

    @Getter
    private ArrayList<WorldInfo> worlds = new ArrayList<>();

    @Override
    public String getKey() {
        return "servertype:"+name;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("name", name);
        hash.put("description", description);
        hash.put("ram", ram);
        hash.put("players", players);
        hash.put("plugins", plugins);
        hash.put("worlds", worlds);
        return hash;
    }
}
