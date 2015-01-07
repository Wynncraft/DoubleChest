package io.minestack.doublechest.model.network.repository.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.network.NetworkForcedHost;
import io.minestack.doublechest.model.node.NetworkNode;
import io.minestack.doublechest.model.pluginhandler.bungeetype.NetworkBungeeType;
import io.minestack.doublechest.model.pluginhandler.bungeetype.NetworkBungeeTypeAddress;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import org.bson.types.ObjectId;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
                networkServerType.setManualStart((boolean) dbServerType.get("manualStart"));
                networkServerType.setAmount(Integer.parseInt((String) dbServerType.get("amount")));
                networkServerType.setNetwork(network);
                network.getServerTypes().put(networkServerType.getServerType().getId(), networkServerType);
            }
        }

        if (dbNetwork.containsField("nodes")) {
            BasicDBList nodeList = (BasicDBList) dbNetwork.get("nodes");

            for (Object objNode : nodeList) {
                DBObject dbNode = (DBObject) objNode;

                NetworkNode networkNode = new NetworkNode((ObjectId) dbNode.get("_id"), (Date) dbNode.get("created_at"));
                networkNode.setUpdated_at((Date) dbNode.get("updated_at"));
                networkNode.setNode(getDatabase().getNodeRepository().getModel(new ObjectId((String) dbNode.get("node_id"))));
                networkNode.setNetwork(network);
                network.getNodes().put(networkNode.getNode().getId(), networkNode);
            }
        }

        if (dbNetwork.containsField("bungeetypes")) {
            BasicDBList bungeeTypeList = (BasicDBList) dbNetwork.get("bungeetypes");

            for (Object objBungeeType : bungeeTypeList) {
                DBObject dbBungeeType = (DBObject) objBungeeType;

                NetworkBungeeType networkBungeeType = new NetworkBungeeType((ObjectId) dbBungeeType.get("_id"), (Date) dbBungeeType.get("created_at"));
                networkBungeeType.setAmount(Integer.parseInt((String) dbBungeeType.get("amount")));
                networkBungeeType.setNetwork(network);
                networkBungeeType.setBungeeType(DoubleChest.INSTANCE.getMongoDatabase().getBungeeTypeRepository().getModel(new ObjectId((String) dbBungeeType.get("bungee_type_id"))));

                BasicDBList addressList = (BasicDBList) dbBungeeType.get("addresses");

                for (Object objAddress : addressList) {
                    DBObject dbAddress = (DBObject) objAddress;

                    NetworkBungeeTypeAddress networkBungeeTypeAddress = new NetworkBungeeTypeAddress((ObjectId) dbAddress.get("_id"), (Date) dbAddress.get("created_at"));
                    networkBungeeTypeAddress.setNetworkBungeeType(networkBungeeType);
                    networkBungeeTypeAddress.setNode(network.getNodes().get(new ObjectId((String) dbAddress.get("node_id"))).getNode());
                    networkBungeeTypeAddress.setPublicAddress(networkBungeeTypeAddress.getNode().getPublicAddresses().get(new ObjectId((String) dbAddress.get("node_public_address_id"))));

                    networkBungeeType.getAddresses().put(networkBungeeTypeAddress.getId(), networkBungeeTypeAddress);
                }

                network.getBungeeTypes().put(networkBungeeType.getBungeeType().getId(), networkBungeeType);
            }
        }

        if (dbNetwork.containsField("forcedHosts")) {
            BasicDBList forcedHostList = (BasicDBList) dbNetwork.get("forcedHosts");

            for (Object objForcedHost : forcedHostList) {
                DBObject dbForcedHost = (DBObject) objForcedHost;

                NetworkForcedHost networkForcedHost = new NetworkForcedHost((ObjectId) dbForcedHost.get("_id"), (Date) dbForcedHost.get("created_at"));
                networkForcedHost.setHost((String) dbForcedHost.get("host"));
                networkForcedHost.setNetwork(network);
                if (dbForcedHost.containsField("server_type_id")) {
                    networkForcedHost.setServerType(network.getServerTypes().get(new ObjectId((String) dbForcedHost.get("server_type_id"))).getServerType());
                }

                network.getForcedHosts().put(networkForcedHost.getHost(), networkForcedHost);
            }
        }

        return network;
    }

    @Override
    public void saveModel(Network model) {
        throw new NotImplementedException();
    }

    @Override
    public void insertModel(Network model) {
        throw new NotImplementedException();
    }

    @Override
    public void removeModel(Network model) {
        throw new NotImplementedException();
    }
}
