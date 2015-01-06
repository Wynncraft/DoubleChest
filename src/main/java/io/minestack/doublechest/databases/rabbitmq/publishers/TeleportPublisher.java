package io.minestack.doublechest.databases.rabbitmq.publishers;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.databases.rabbitmq.pubsub.PubSubExchanges;
import io.minestack.doublechest.databases.rabbitmq.pubsub.PubSubPublisher;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.server.Server;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;import java.lang.String;

public class TeleportPublisher extends PubSubPublisher {

    public TeleportPublisher() throws IOException {
        super(DoubleChest.INSTANCE.getRabbitMQDatabase(), PubSubExchanges.TELEPORT.name());
    }

    public void teleportPlayer(ServerType serverType, String... playerNames) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("serverType", serverType.getId().toString());
        if (playerNames.length == 1) {
            jsonObject.put("player", playerNames[0]);
        } else {
            jsonObject.put("players", new JSONArray(playerNames));
        }

        publish(jsonObject);
        close();
    }

    public void teleportPlayer(Server server, String... playerNames) throws IOException{
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("server", server.getId().toString());
        if (playerNames.length == 1) {
            jsonObject.put("player", playerNames[0]);
        } else {
            jsonObject.put("players", new JSONArray(playerNames));
        }

        publish(jsonObject);
        close();
    }
}
