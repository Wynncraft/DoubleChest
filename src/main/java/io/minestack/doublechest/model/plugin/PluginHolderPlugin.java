package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import lombok.Getter;
import lombok.Setter;

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

}
