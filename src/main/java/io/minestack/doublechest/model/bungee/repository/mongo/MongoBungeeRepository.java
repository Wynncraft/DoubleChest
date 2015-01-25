package io.minestack.doublechest.model.bungee.repository.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.bungee.Bungee;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.node.NodePublicAddress;
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
        bungee.setPublicAddress(bungee.getNode().getPublicAddresses().get(new ObjectId((String) dbBungee.get("node_public_address_id"))));
        bungee.setContainerId((String) dbBungee.get("container"));

        return bungee;
    }

    public List<Bungee> getNodeBungees(Node node) {
        List<Bungee> bungees = new ArrayList<>();

        DBCursor bungeesCursor = getDatabase().findMany("bungees", new BasicDBObject("node_id", node.getId().toString()));
        while (bungeesCursor.hasNext()) {
            bungees.add(getModel((ObjectId) bungeesCursor.next().get("_id")));
        }

        return bungees;
    }

    public List<Bungee> getNetworkBungees(Network network) {
        List<Bungee> bungees = new ArrayList<>();

        DBCursor bungeesCursor = getDatabase().findMany("bungees", new BasicDBObject("network_id", network.getId().toString()));
        while (bungeesCursor.hasNext()) {
            bungees.add(getModel((ObjectId) bungeesCursor.next().get("_id")));
        }

        return bungees;
    }

    public Bungee getNetworkNodeAddressBungee(Network network, Node node, NodePublicAddress publicAddress) {
        DBObject query = new BasicDBObject("network_id", network.getId().toString());
        query.put("node_id", node.getId().toString());
        query.put("node_public_address_id", publicAddress.getId().toString());

        DBObject dbObject = getDatabase().findOne("bungees", query);
        if (dbObject == null) {
            return null;
        }
        return getModel((ObjectId) dbObject.get("_id"));
    }

    @Override
    public void saveModel(Bungee model) {
        BasicDBObject dbBungee = new BasicDBObject();
        dbBungee.put("updated_at", model.getUpdated_at());
        dbBungee.put("container", model.getContainerId());

        getDatabase().updateDocument("bungees", new BasicDBObject("_id", model.getId()), new BasicDBObject("$set", dbBungee));
    }

    @Override
    public void insertModel(Bungee model) {
        BasicDBObject dbBungee = new BasicDBObject();
        dbBungee.put("_id", model.getId());
        dbBungee.put("created_at", model.getCreated_at());
        dbBungee.put("updated_at", model.getUpdated_at());
        dbBungee.put("network_id", model.getNetwork().getId().toString());
        dbBungee.put("node_id", model.getNode().getId().toString());
        dbBungee.put("node_public_address_id", model.getPublicAddress().getId().toString());
        dbBungee.put("bungee_type_id", model.getBungeeType().getId().toString());
        dbBungee.put("container", "NULL");

        getDatabase().insert("bungees", dbBungee);
    }

    @Override
    public void removeModel(Bungee model) {
        getDatabase().remove("bungees", new BasicDBObject("_id", model.getId()));
    }
}
