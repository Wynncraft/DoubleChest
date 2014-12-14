package io.minestack.doublechest.model.bungee;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.plugin.PluginInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

public class Bungee extends Model {

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
    private ArrayList<PluginInfo> plugins = new ArrayList<>();

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public HashMap<String, Object> toHash() {
        return null;
    }
}
