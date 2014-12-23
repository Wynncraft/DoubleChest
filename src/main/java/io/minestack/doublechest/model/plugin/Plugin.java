package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

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

    public Plugin(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
