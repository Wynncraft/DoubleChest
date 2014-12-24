package io.minestack.doublechest.model.node.repository.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.mongo.MongoModelRepository;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.node.NodePublicAddress;
import org.bson.types.ObjectId;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoNodeRepository extends MongoModelRepository<Node> {

    public MongoNodeRepository(MongoDatabase database) {
        super(database);
    }

    @Override
    public List<Node> getModels() {
        List<Node> nodes = new ArrayList<>();

        DBCursor nodeCursor = getDatabase().findMany("nodes");

        while (nodeCursor.hasNext()) {
            nodes.add(getModel((ObjectId) nodeCursor.next().get("_id")));
        }

        return nodes;
    }

    @Override
    public Node getModel(ObjectId id) {
        DBObject dbNode = getDatabase().findOne("nodes", new BasicDBObject("_id", id));

        if (dbNode == null) {
            return null;
        }

        Node node = new Node((ObjectId) dbNode.get("_id"), (Date) dbNode.get("created_at"));
        node.setUpdated_at((Date) dbNode.get("updated_at"));
        node.setName((String) dbNode.get("name"));
        node.setPrivateAddress((String) dbNode.get("privateAddress"));
        node.setRam((int) dbNode.get("ram"));

        if (dbNode.containsField("publicaddresses")) {
            BasicDBList publicAddressList = (BasicDBList) dbNode.get("publicaddresses");

            for (Object objPublicAddress : publicAddressList) {
                DBObject dbPublicAddress = (DBObject) objPublicAddress;

                NodePublicAddress nodePublicAddress = new NodePublicAddress((ObjectId) dbPublicAddress.get("_id"), (Date) dbPublicAddress.get("created_at"));
                nodePublicAddress.setUpdated_at((Date) dbPublicAddress.get("updated_at"));
                nodePublicAddress.setPublicAddress((String) dbPublicAddress.get("publicAddress"));
                nodePublicAddress.setNode(node);
                node.getPublicAddresses().put(nodePublicAddress.getId(), nodePublicAddress);
            }
        }

        return node;
    }

    @Override
    public void saveModel(Node model) {
        throw new NotImplementedException();
    }

    @Override
    public void removeModel(Node model) {
        throw new NotImplementedException();
    }
}
