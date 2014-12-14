package io.minestack.doublechest.model.type;

import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.plugin.PluginInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public abstract class PluginHolder extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    private ArrayList<PluginInfo> plugins = new ArrayList<>();

}
