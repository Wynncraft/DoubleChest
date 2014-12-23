package io.minestack.doublechest.model.bungee.repository.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.bungee.Bungee;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoBungeeRepository extends MongoModelRepository<Bungee> {

    public MongoBungeeRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<Bungee> getModels() {
        List<Bungee> bungees = new ArrayList<>();

        DBCursor serversCursor = getDatabase().findMany("bungees");
        while (serversCursor.hasNext()) {
            bungees.add(getModel((ObjectId) serversCursor.next().get("_id")));
        }

        return bungees;
    }

    @Override
    public Bungee getModel(ObjectId id) {
        DBObject dbBungee = getDatabase().findOne("bungees", new BasicDBObject("_id", id));

        if (dbBungee == null) {
            return null;
        }

        Bungee bungee = new Bungee((ObjectId) dbBungee.get("_id"), (Date) dbBungee.get("created_at"));
        bungee.setUpdated_at((Date) dbBungee.get("updated_at"));
        bungee.setNetwork(getDatabase().getNetworkRepository().getModel(new ObjectId((String) dbBungee.get("network_id"))));
        bungee.setNode(getDatabase().getNodeRepository().getModel(new ObjectId((String) dbBungee.get("node_id"))));
        bungee.setBungeeType(getDatabase().getBungeeTypeRepository().getModel(new ObjectId((String) dbBungee.get("bungee_type_id"))));

        return bungee;
    }

    @Override
    public void saveModel(Bungee model) {
        if (model.getId() == null) {
            BasicDBObject dbBungee = new BasicDBObject();
            dbBungee.put("created_at", model.getCreated_at());
            dbBungee.put("updated_at", model.getUpdated_at());
            dbBungee.put("network_id", model.getNetwork().getId().toString());
            dbBungee.put("node_id", model.getNode().getId().toString());
            dbBungee.put("bungee_type_id", model.getBungeeType().getId().toString());

            getDatabase().insert("bungees", dbBungee);
        } else {
            BasicDBObject dbBungee = new BasicDBObject();
            dbBungee.put("updated_at", model.getUpdated_at());

            getDatabase().updateDocument("bungees", new BasicDBObject("_id", model.getId()), dbBungee);
        }
    }
}
