package io.minestack.doublechest.model.plugin;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

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

    public PluginVersion(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
