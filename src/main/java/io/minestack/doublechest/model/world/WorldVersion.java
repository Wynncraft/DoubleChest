package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

public class WorldVersion extends Model {

    @Getter
    @Setter
    private World world;

    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String description;

    public WorldVersion(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
