package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;

public class PluginVersion extends Model {

    @Getter
    @Setter
    private Plugin plugin;

    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String description;

}
