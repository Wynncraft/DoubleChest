package io.minestack.doublechest.model.plugin.repository.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.plugin.Plugin;
import io.minestack.doublechest.model.plugin.PluginConfig;
import io.minestack.doublechest.model.plugin.PluginType;
import io.minestack.doublechest.model.plugin.PluginVersion;
import org.bson.types.ObjectId;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoPluginRepository extends MongoModelRepository<Plugin> {

    public MongoPluginRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<Plugin> getModels() {
        List<Plugin> plugins = new ArrayList<>();
        DBCursor pluginCursor = getDatabase().findMany("plugins");
        while (pluginCursor.hasNext()) {
            plugins.add(getModel((ObjectId) pluginCursor.next().get("_id")));
        }
        return plugins;
    }

    @Override
    public Plugin getModel(ObjectId id) {
        DBObject dbPlugin = getDatabase().findOne("plugins", new BasicDBObject("_id", id));

        if (dbPlugin == null) {
            return null;
        }

        Plugin plugin = new Plugin((ObjectId) dbPlugin.get("_id"), (Date) dbPlugin.get("created_at"));
        plugin.setUpdated_at((Date) dbPlugin.get("updated_at"));
        plugin.setName((String) dbPlugin.get("name"));
        plugin.setDescription((String) dbPlugin.get("description"));
        plugin.setType(PluginType.valueOf((String) dbPlugin.get("type")));
        plugin.setDirectory((String) dbPlugin.get("directory"));

        if (dbPlugin.containsField("versions")) {
            BasicDBList versionList = (BasicDBList) dbPlugin.get("versions");

            for (Object objVersion : versionList) {
                DBObject dbVersion = (DBObject) objVersion;

                PluginVersion pluginVersion = new PluginVersion((ObjectId) dbPlugin.get("_id"), (Date) dbVersion.get("created_at"));
                pluginVersion.setUpdated_at((Date) dbVersion.get("updated_at"));
                pluginVersion.setVersion((String) dbVersion.get("version"));
                pluginVersion.setDescription((String) dbVersion.get("description"));
                pluginVersion.setPlugin(plugin);
                plugin.getVersions().put(pluginVersion.getId(), pluginVersion);
            }
        }

        if (dbPlugin.containsField("configs")) {
            BasicDBList configList = (BasicDBList) dbPlugin.get("configs");

            for (Object objConfig : configList) {
                DBObject dbConfig = (DBObject) objConfig;

                PluginConfig pluginConfig = new PluginConfig((ObjectId) dbPlugin.get("_id"), (Date) dbConfig.get("created_at"));
                pluginConfig.setUpdated_at((Date) dbConfig.get("updated_at"));
                pluginConfig.setName((String) dbConfig.get("name"));
                pluginConfig.setDescription((String) dbConfig.get("description"));
                pluginConfig.setDirectory((String) dbConfig.get("directory"));
                pluginConfig.setPlugin(plugin);
                plugin.getConfigs().put(pluginConfig.getId(), pluginConfig);
            }
        }

        return plugin;
    }

    @Override
    public void saveModel(Plugin model) {
        throw new NotImplementedException();
    }

    @Override
    public void removeModel(Plugin model) {
        throw new NotImplementedException();
    }
}
