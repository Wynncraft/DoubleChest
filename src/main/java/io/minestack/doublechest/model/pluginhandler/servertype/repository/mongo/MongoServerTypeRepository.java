package io.minestack.doublechest.model.pluginhandler.servertype.repository.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.plugin.PluginHolderPlugin;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.world.ServerTypeWorld;
import org.bson.types.ObjectId;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoServerTypeRepository extends MongoModelRepository<ServerType> {

    public MongoServerTypeRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<ServerType> getModels() {
        List<ServerType> serverTypes = new ArrayList<>();
        DBCursor serverTypeCursor = getDatabase().findMany("servertypes");

        while (serverTypeCursor.hasNext()) {
            serverTypes.add(getModel((ObjectId) serverTypeCursor.next().get("_id")));
        }

        return serverTypes;
    }

    @Override
    public ServerType getModel(ObjectId id) {
        DBObject dbServerType = getDatabase().findOne("servertypes", new BasicDBObject("_id", id));

        if (dbServerType == null) {
            return null;
        }

        ServerType serverType = new ServerType((ObjectId) dbServerType.get("_id"), (Date) dbServerType.get("created_at"));
        serverType.setUpdated_at((Date) dbServerType.get("updated_at"));
        serverType.setName((String) dbServerType.get("name"));
        serverType.setDescription((String) dbServerType.get("description"));
        serverType.setPlayers(Integer.parseInt((String) dbServerType.get("players")));
        serverType.setRam(Integer.parseInt((String) dbServerType.get("ram")));

        if (dbServerType.containsField("plugins")) {
            BasicDBList pluginList = (BasicDBList) dbServerType.get("plugins");
            for (Object objPluginHolderPlugin : pluginList) {
                DBObject dbPluginHolderPlugin = (DBObject) objPluginHolderPlugin;

                PluginHolderPlugin pluginHolderPlugin = new PluginHolderPlugin((ObjectId) dbPluginHolderPlugin.get("_id"), (Date) dbPluginHolderPlugin.get("created_at"));
                pluginHolderPlugin.setUpdated_at((Date) dbPluginHolderPlugin.get("updated_at"));
                pluginHolderPlugin.setPlugin(getDatabase().getPluginRepository().getModel(new ObjectId((String) dbPluginHolderPlugin.get("plugin_id"))));
                pluginHolderPlugin.setVersion(pluginHolderPlugin.getPlugin().getVersions().get(new ObjectId((String) dbPluginHolderPlugin.get("pluginversion_id"))));
                if (dbPluginHolderPlugin.containsField("pluginconfig_id")) {
                    pluginHolderPlugin.setConfig(pluginHolderPlugin.getPlugin().getConfigs().get(new ObjectId((String) dbPluginHolderPlugin.get("pluginconfig_id"))));
                }
            }
        }

        if (dbServerType.containsField("worlds")) {
            BasicDBList worldList = (BasicDBList) dbServerType.get("worlds");
            for (Object objServerTypeWorld : worldList) {
                DBObject dbServerTypeWorld = (DBObject) objServerTypeWorld;

                ServerTypeWorld serverTypeWorld = new ServerTypeWorld((ObjectId) dbServerTypeWorld.get("_id"), (Date) dbServerTypeWorld.get("created_at"));
                serverTypeWorld.setWorld(getDatabase().getWorldRepository().getModel(new ObjectId((String) dbServerTypeWorld.get("world_id"))));
                serverTypeWorld.setVersion(serverTypeWorld.getWorld().getVersions().get(new ObjectId((String) dbServerTypeWorld.get("worldversion_id"))));
                serverTypeWorld.setDefaultWorld((boolean) dbServerTypeWorld.get("defaultWorld"));
            }
        }

        return serverType;
    }

    public ServerType getModel(String typeName) {
        DBObject query = new BasicDBObject("name", typeName);
        DBObject dbServerType = getDatabase().findOne("servertypes", query);
        if (dbServerType == null) {
            return null;
        }
        return getModel((ObjectId) dbServerType.get("_id"));
    }

    @Override
    public void saveModel(ServerType model) {
        throw new NotImplementedException();
    }

    @Override
    public void insertModel(ServerType model) {
        throw new NotImplementedException();
    }

    @Override
    public void removeModel(ServerType model) {
        throw new NotImplementedException();
    }
}
