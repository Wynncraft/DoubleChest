package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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

}
