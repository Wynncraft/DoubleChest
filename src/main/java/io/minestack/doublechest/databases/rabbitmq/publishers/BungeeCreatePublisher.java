package io.minestack.doublechest.databases.rabbitmq.publishers;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.databases.rabbitmq.worker.WorkerPublisher;
import io.minestack.doublechest.databases.rabbitmq.worker.WorkerQueues;
import io.minestack.doublechest.model.bungee.Bungee;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.NodePublicAddress;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

public class BungeeCreatePublisher extends WorkerPublisher {

    public BungeeCreatePublisher() throws IOException {
        super(DoubleChest.INSTANCE.getRabbitMQDatabase(), WorkerQueues.BUNGEE_BUILD.name());
    }

    public void createBungee(BungeeType bungeeType, Network network, NodePublicAddress nodePublicAddress) throws IOException {
        if (bungeeType != null && network != null && nodePublicAddress != null) {
            Bungee bungee = new Bungee(new ObjectId(), new Date(System.currentTimeMillis()));
            bungee.setBungeeType(bungeeType);
            bungee.setNetwork(network);
            bungee.setNode(nodePublicAddress.getNode());
            bungee.setPublicAddress(nodePublicAddress);
            bungee.setUpdated_at(new Date(System.currentTimeMillis() + 300000));
            DoubleChest.INSTANCE.getMongoDatabase().getBungeeRepository().insertModel(bungee);

            JSONObject message = new JSONObject();
            message.put("bungee", bungee.getId().toString());
            publish(message);
        }
        close();
    }
}
