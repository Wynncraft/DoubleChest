package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private Map<ObjectId, PluginVersion> versions = new HashMap<>();

    @Getter
    private Map<ObjectId, PluginConfig> configs = new HashMap<>();

    public Plugin(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
