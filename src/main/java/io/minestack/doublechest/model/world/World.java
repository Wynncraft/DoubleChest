package io.minestack.doublechest.model.world;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.sql.Timestamp;
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
        return "world:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("name", name);
        hash.put("description", description);
        hash.put("directory", directory);
        JSONArray versions = new JSONArray();
        for (WorldVersion version : this.versions) {
            versions.put(version.getKey());
        }
        hash.put("versions", versions.toString());
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt(hash.get("id")));
        setName(hash.get("name"));
        setDescription(hash.get("description"));
        setDirectory(hash.get("directory"));
        JSONArray versions = new JSONArray(hash.get("versions"));
        for (int i = 0; i < versions.length(); i++) {
            String version = versions.getString(i);
            WorldVersion worldVersion = DoubleChest.INSTANCE.getRedisDatabase().getWorldVersionRepository().getModel(version);
            if (worldVersion != null) {
                worldVersion.setWorld(this);
                this.versions.add(worldVersion);
            }
        }
        setUpdated_at(new Timestamp(Long.parseLong(hash.get("updated_at"))));
    }
}
