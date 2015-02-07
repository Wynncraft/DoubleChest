package io.minestack.doublechest.databases.rabbitmq.publishers;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.databases.rabbitmq.worker.WorkerPublisher;
import io.minestack.doublechest.databases.rabbitmq.worker.WorkerQueues;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.server.Server;
import io.minestack.doublechest.model.server.ServerMetaData;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class ServerCreatePublisher extends WorkerPublisher {

    public ServerCreatePublisher() throws IOException {
        super(DoubleChest.INSTANCE.getRabbitMQDatabase(), WorkerQueues.SERVER_BUILD.name());
    }

    public void createServer(ServerType serverType, Network network) throws IOException {
        if (serverType != null && network != null) {
            Server server = new Server(new ObjectId(), new Date(System.currentTimeMillis()));
            server.setNetwork(network);
            server.setServerType(serverType);
            server.setUpdated_at(new Date(System.currentTimeMillis() + 300000));
            DoubleChest.INSTANCE.getMongoDatabase().getServerRepository().insertModel(server);

            JSONObject message = new JSONObject();
            message.put("server", server.getId().toString());
            publish(message);
        }
        close();
    }

    public void createServer(ServerType serverType, Network network, HashMap<String, ServerMetaData> metaData) throws IOException {
        if (serverType != null && network != null) {
            Server server = new Server(new ObjectId(), new Date(System.currentTimeMillis()));
            server.setNetwork(network);
            server.setServerType(serverType);
            server.setUpdated_at(new Date(System.currentTimeMillis() + 300000));
            server.getMetaData().putAll(metaData);

            DoubleChest.INSTANCE.getMongoDatabase().getServerRepository().insertModel(server);

            JSONObject message = new JSONObject();
            message.put("server", server.getId().toString());
            publish(message);
        }
        close();
    }
}
