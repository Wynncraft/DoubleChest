package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class World extends Model {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String directory;

    @Getter
    private ArrayList<WorldVersion> versions = new ArrayList<>();

    public World(ObjectId id, Date created_at) {
        super(id, created_at);
    }
}
