package io.minestack.doublechest.model.world;

import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    public String getKey() {
        return "world:"+name;
    }

    @Override
    public HashMap<String, Object> toHash() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("id", getId());
        hash.put("name", name);
        hash.put("description", description);
        hash.put("directory", directory);
        JSONArray versions = new JSONArray();
        for (WorldVersion version : this.versions) {
            versions.put(version.getKey());
        }
        hash.put("versions", versions.toString());
        return hash;
    }
}
