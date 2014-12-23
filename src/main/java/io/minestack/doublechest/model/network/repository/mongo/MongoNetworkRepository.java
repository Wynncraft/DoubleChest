package io.minestack.doublechest.model.network.repository.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.NetworkNode;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoNetworkRepository extends MongoModelRepository<Network> {

    public MongoNetworkRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<Network> getModels() {
        List<Network> networks = new ArrayList<>();

        DBCursor networkCursor = getDatabase().findMany("networks");
        while (networkCursor.hasNext()) {
            networks.add(getModel((ObjectId) networkCursor.next().get("_id")));
        }

        return networks;
    }

    @Override
    public Network getModel(ObjectId id) {
        DBObject dbNetwork = getDatabase().findOne("networks", new BasicDBObject("_id", id));
        if (dbNetwork == null) {
            return null;
        }

        Network network = new Network((ObjectId) dbNetwork.get("_id"), (Date) dbNetwork.get("created_at"));
        network.setUpdated_at((Date) dbNetwork.get("updated_at"));
        network.setName((String) dbNetwork.get("name"));
        network.setDescription((String) dbNetwork.get("description"));

        if (dbNetwork.containsField("servertypes")) {
            BasicDBList serverTypeList = (BasicDBList) dbNetwork.get("servertypes");

            for (Object objServerType : serverTypeList) {
                DBObject dbServerType = (DBObject) objServerType;

                NetworkServerType networkServerType = new NetworkServerType((ObjectId) dbServerType.get("_id"), (Date) dbServerType.get("created_at"));
                networkServerType.setUpdated_at((Date) dbServerType.get("updated_at"));
                networkServerType.setServerType(getDatabase().getServerTypeRepository().getModel(new ObjectId((String) dbServerType.get("server_type_id"))));
                networkServerType.setDefaultServerType((boolean) dbServerType.get("defaultServerType"));
                networkServerType.setAmount((int) dbServerType.get("amount"));
                networkServerType.setNetwork(network);
                network.getServerTypes().put(networkServerType.getId(), networkServerType);
            }
        }

        if (dbNetwork.containsField("nodes")) {
            BasicDBList nodeList = (BasicDBList) dbNetwork.get("nodes");

            for (Object objNode : nodeList) {
                DBObject dbNode = (DBObject) objNode;

                NetworkNode networkNode = new NetworkNode((ObjectId) dbNetwork.get("_id"), (Date) dbNode.get("created_at"));
                networkNode.setUpdated_at((Date) dbNode.get("updated_at"));
                networkNode.setNode(getDatabase().getNodeRepository().getModel(new ObjectId((String) dbNode.get("node_id"))));
                networkNode.setNodePublicAddress(networkNode.getNode().getPublicAddresses().get(new ObjectId((String) dbNode.get("node_public_address_id"))));
                networkNode.setBungeeType(getDatabase().getBungeeTypeRepository().getModel(new ObjectId((String) dbNode.get("bungee_type_id"))));
            }
        }

        return network;
    }

    @Override
    public void saveModel(Network model) {

    }
}
