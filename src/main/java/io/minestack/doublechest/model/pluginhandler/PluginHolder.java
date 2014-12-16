package io.minestack.doublechest.model.pluginhandler;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.plugin.PluginHolderPlugin;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public abstract class PluginHolder extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    private ArrayList<PluginHolderPlugin> plugins = new ArrayList<>();

}
