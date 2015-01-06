package io.minestack.doublechest.databases.rabbitmq.publishers;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.databases.rabbitmq.worker.WorkerPublisher;
import io.minestack.doublechest.databases.rabbitmq.worker.WorkerQueues;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.NodePublicAddress;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;
import org.json.JSONObject;

import java.io.IOException;

public class BungeeCreatePublisher extends WorkerPublisher {

    public BungeeCreatePublisher() throws IOException {
        super(DoubleChest.INSTANCE.getRabbitMQDatabase(), WorkerQueues.BUNGEE_BUILD.name());
    }

    public void createBungee(BungeeType bungeeType, Network network, NodePublicAddress nodePublicAddress) throws IOException {
        JSONObject message = new JSONObject();
        message.put("network", network.getId().toString());
        message.put("bungeeType", bungeeType);
        message.put("node", nodePublicAddress.getNode().getId().toString());
        message.put("publicAddress", nodePublicAddress.getId().toString());

        publish(message);
        close();
    }
}
