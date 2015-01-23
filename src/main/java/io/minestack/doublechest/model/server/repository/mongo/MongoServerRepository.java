package io.minestack.doublechest.model.server.repository.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.server.Server;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoServerRepository extends MongoModelRepository<Server> {

    public MongoServerRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<Server> getModels() {
        List<Server> servers = new ArrayList<>();

        DBCursor serversCursor = getDatabase().findMany("servers");
        while (serversCursor.hasNext()) {
            servers.add(getModel((ObjectId) serversCursor.next().get("_id")));
        }

        return servers;
    }

    @Override
    public Server getModel(ObjectId id) {
        DBObject dbServer = getDatabase().findOne("servers", new BasicDBObject("_id", id));

        if (dbServer == null) {
            return null;
        }

        Server server = new Server((ObjectId) dbServer.get("_id"), (Date) dbServer.get("created_at"));
        server.setUpdated_at((Date) dbServer.get("updated_at"));
        server.setNetwork(getDatabase().getNetworkRepository().getModel(new ObjectId((String) dbServer.get("network_id"))));
        String node = (String) dbServer.get("node_id");
        if (node != null) {
            server.setNode(getDatabase().getNodeRepository().getModel(new ObjectId(node)));
        }
        server.setServerType(getDatabase().getServerTypeRepository().getModel(new ObjectId((String) dbServer.get("server_type_id"))));
        server.setContainerId((String) dbServer.get("container"));
        server.setPort((int) dbServer.get("port"));
        server.setPlayers((int) dbServer.get("players"));
        server.setNumber((int) dbServer.get("number"));

        return server;
    }

    public List<Server> getNodeServers(Node node) {
        List<Server> servers = new ArrayList<>();

        DBCursor serversCursor = getDatabase().findMany("servers", new BasicDBObject("node_id", node.getId().toString()));
        while (serversCursor.hasNext()) {
            servers.add(getModel((ObjectId) serversCursor.next().get("_id")));
        }

        return servers;
    }

    public int getNextNumber(Network network, ServerType serverType) {
        int number = 1;

        List<Integer> numbers = new ArrayList<>();

        BasicDBObject query = new BasicDBObject("network_id", network.getId().toString());
        query.put("server_type_id", serverType.getId().toString());

        DBCursor serversCursor = getDatabase().findMany("servers", query);
        while (serversCursor.hasNext()) {
            int serverNumber = (int) serversCursor.next().get("number");
            numbers.add(serverNumber);
        }

        while (numbers.contains(number)) {
            number += 1;
        }

        return number;
    }

    public List<Server> getNetworkServers(Network network) {
        List<Server> servers = new ArrayList<>();

        BasicDBObject query = new BasicDBObject("network_id", network.getId().toString());
        query.put("number", new BasicDBObject("$gt", 0));

        DBCursor serversCursor = getDatabase().findMany("servers", query);
        while (serversCursor.hasNext()) {
            servers.add(getModel((ObjectId) serversCursor.next().get("_id")));
        }

        return servers;
    }

    public List<Server> getNetworkServerTypeServers(Network network, ServerType serverType) {
        List<Server> servers = new ArrayList<>();

        BasicDBObject query = new BasicDBObject("network_id", network.getId().toString());
        query.put("server_type_id", serverType.getId().toString());

        DBCursor serversCursor = getDatabase().findMany("servers", query);
        while (serversCursor.hasNext()) {
            servers.add(getModel((ObjectId) serversCursor.next().get("_id")));
        }

        return servers;
    }

    public Server getNetworkServerTypeServerNumber(Network network, ServerType serverType, int number) {
        List<Server> servers = new ArrayList<>();

        BasicDBObject query = new BasicDBObject("network_id", network.getId().toString());
        query.put("server_type_id", serverType.getId().toString());
        query.put("number", number);

        DBObject dbServer = getDatabase().findOne("servers", query);
        if (dbServer == null) {
            return null;
        }
        return getModel((ObjectId) dbServer.get("_id"));
    }

    public List<Server> getServerTypeServers(ServerType serverType) {
        List<Server> servers = new ArrayList<>();

        DBCursor serversCursor = getDatabase().findMany("servers", new BasicDBObject("server_type_id", serverType.getId().toString()));
        while (serversCursor.hasNext()) {
            servers.add(getModel((ObjectId) serversCursor.next().get("_id")));
        }

        return servers;
    }

    @Override
    public void saveModel(Server model) {
        BasicDBObject dbServer = new BasicDBObject();
        dbServer.put("updated_at", model.getUpdated_at());
        if (model.getNode() != null) {
            dbServer.put("node_id", model.getNode().getId().toString());
        }
        dbServer.put("port", model.getPort());
        dbServer.put("container", model.getContainerId());
        dbServer.put("players", model.getPlayers());
        dbServer.put("number", model.getNumber());
        getDatabase().updateDocument("servers", new BasicDBObject("_id", model.getId()), new BasicDBObject("$set", dbServer));
    }

    @Override
    public void insertModel(Server model) {
        BasicDBObject dbServer = new BasicDBObject();
        dbServer.put("_id", model.getId());
        dbServer.put("created_at", model.getCreated_at());
        dbServer.put("updated_at", model.getUpdated_at());
        dbServer.put("network_id", model.getNetwork().getId().toString());
        if (model.getNode() != null) {
            dbServer.put("node_id", model.getNode().getId().toString());
        } else {
            dbServer.put("node_id", null);
        }
        dbServer.put("server_type_id", model.getServerType().getId().toString());
        dbServer.put("port", model.getPort());
        dbServer.put("container", model.getContainerId());
        dbServer.put("players", model.getPlayers());
        dbServer.put("number", model.getNumber());

        getDatabase().insert("servers", dbServer);
    }

    @Override
    public void removeModel(Server model) {
        getDatabase().remove("servers", new BasicDBObject("_id", model.getId()));
    }
}
