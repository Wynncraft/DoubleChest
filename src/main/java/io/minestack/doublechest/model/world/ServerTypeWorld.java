package io.minestack.doublechest.model.world;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.model.Model;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;

public class ServerTypeWorld extends Model {

    @Getter
    @Setter
    private ServerType serverType;

    @Getter
    @Setter
    private World world;

    @Getter
    @Setter
    private WorldVersion version;

    @Getter
    @Setter
    private boolean defaultWorld;

    @Override
    public String getKey() {
        return serverType.getKey()+":world:"+getId();
    }

    @Override
    public HashMap<String, String> toHash() {
        HashMap<String, String> hash = new HashMap<>();
        hash.put("id", getId()+"");
        hash.put("servertyype", serverType.getKey());
        hash.put("world", world.getKey());
        hash.put("version", version.getKey());
        hash.put("defaultWorld", defaultWorld+"");
        hash.put("updated_at", getUpdated_at().getTime()+"");
        return hash;
    }

    @Override
    public void fromHash(HashMap<String, String> hash) throws Exception {
        setId(Integer.parseInt("id"));
        //setServerType(DoubleChest.INSTANCE.getRedisDatabase().getServerTypeRepository().getModel(hash.get("servertype")));
        setWorld(DoubleChest.INSTANCE.getRedisDatabase().getWorldRepository().getModel(hash.get("world")));
        setVersion(DoubleChest.INSTANCE.getRedisDatabase().getWorldVersionRepository().getModel("version"));
        setDefaultWorld(Boolean.parseBoolean(hash.get("defaultWorld")));
        setUpdated_at(new Timestamp(Long.parseLong(hash.get("updated_at"))));
    }
}
