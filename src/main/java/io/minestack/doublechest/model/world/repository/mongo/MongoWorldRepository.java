package io.minestack.doublechest.model.world.repository.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.world.World;
import io.minestack.doublechest.model.world.WorldVersion;
import org.bson.types.ObjectId;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoWorldRepository extends MongoModelRepository<World> {

    public MongoWorldRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<World> getModels() {
        List<World> worlds = new ArrayList<>();
        DBCursor worldsCursor = getDatabase().findMany("worlds");
        while (worldsCursor.hasNext()) {
            worlds.add(getModel((ObjectId) worldsCursor.next().get("_id")));
        }
        return worlds;
    }

    @Override
    public World getModel(ObjectId id) {
        DBObject dbWorld = getDatabase().findOne("worlds", new BasicDBObject("_id", id));

        World world = new World((ObjectId) dbWorld.get("_id"), (Date) dbWorld.get("created_at"));
        world.setUpdated_at((Date) dbWorld.get("updated_at"));
        world.setName((String) dbWorld.get("name"));
        world.setDescription((String) dbWorld.get("description"));
        world.setDirectory((String) dbWorld.get("directory"));

        if (dbWorld.containsField("versions")) {
            BasicDBList versionList = (BasicDBList) dbWorld.get("versions");
            for (Object objVersion : versionList) {
                DBObject dbVersion = (DBObject) objVersion;
                WorldVersion version = new WorldVersion((ObjectId) dbVersion.get("_id"), (Date) dbVersion.get("created_at"));
                version.setUpdated_at((Date) dbVersion.get("updated_at"));
                version.setVersion((String) dbVersion.get("version"));
                version.setDescription((String) dbVersion.get("description"));
                version.setWorld(world);
                world.getVersions().add(version);
            }
        }

        return world;
    }

    @Override
    public void saveModel(World model) {
        throw new NotImplementedException();
    }
}
