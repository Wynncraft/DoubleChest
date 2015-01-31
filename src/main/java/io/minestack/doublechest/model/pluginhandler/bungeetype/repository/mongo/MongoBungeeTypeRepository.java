package io.minestack.doublechest.model.pluginhandler.bungeetype.repository.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.plugin.PluginHolderPlugin;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import org.bson.types.ObjectId;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoBungeeTypeRepository extends MongoModelRepository<BungeeType> {

    public MongoBungeeTypeRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<BungeeType> getModels() {
        List<BungeeType> bungeeTypes = new ArrayList<>();
        DBCursor serverTypeCursor = getDatabase().findMany("bungeetypes");

        while (serverTypeCursor.hasNext()) {
            bungeeTypes.add(getModel((ObjectId) serverTypeCursor.next().get("_id")));
        }

        return bungeeTypes;
    }

    @Override
    public BungeeType getModel(ObjectId id) {
        DBObject dbBungeeType = getDatabase().findOne("bungeetypes", new BasicDBObject("_id", id));

        if (dbBungeeType == null) {
            return null;
        }

        BungeeType bungeeType = new BungeeType((ObjectId) dbBungeeType.get("_id"), (Date) dbBungeeType.get("created_at"));
        bungeeType.setUpdated_at((Date) dbBungeeType.get("updated_at"));
        bungeeType.setName((String) dbBungeeType.get("name"));
        bungeeType.setDescription((String) dbBungeeType.get("description"));
        bungeeType.setRam(Integer.parseInt((String) dbBungeeType.get("ram")));

        if (dbBungeeType.containsField("plugins")) {
            BasicDBList pluginList = (BasicDBList) dbBungeeType.get("plugins");
            for (Object objPluginHolderPlugin : pluginList) {
                DBObject dbPluginHolderPlugin = (DBObject) objPluginHolderPlugin;

                PluginHolderPlugin pluginHolderPlugin = new PluginHolderPlugin((ObjectId) dbPluginHolderPlugin.get("_id"), (Date) dbPluginHolderPlugin.get("created_at"));
                pluginHolderPlugin.setUpdated_at((Date) dbPluginHolderPlugin.get("updated_at"));
                pluginHolderPlugin.setPlugin(getDatabase().getPluginRepository().getModel(new ObjectId((String) dbPluginHolderPlugin.get("plugin_id"))));
                if (dbPluginHolderPlugin.containsField("pluginversion_id")) {
                    pluginHolderPlugin.setVersion(pluginHolderPlugin.getPlugin().getVersions().get(new ObjectId((String) dbPluginHolderPlugin.get("pluginversion_id"))));
                }
                if (dbPluginHolderPlugin.containsField("pluginconfig_id")) {
                    pluginHolderPlugin.setConfig(pluginHolderPlugin.getPlugin().getConfigs().get(new ObjectId((String) dbPluginHolderPlugin.get("pluginconfig_id"))));
                }

                bungeeType.getPlugins().add(pluginHolderPlugin);
            }
        }

        return bungeeType;
    }

    @Override
    public void saveModel(BungeeType model) {
        throw new NotImplementedException();
    }

    @Override
    public void insertModel(BungeeType model) {
        throw new NotImplementedException();
    }

    @Override
    public void removeModel(BungeeType model) {
        throw new NotImplementedException();
    }
}
