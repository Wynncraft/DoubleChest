package io.minestack.doublechest.databases.rabbitmq.publishers;

import io.minestack.doublechest.DoubleChest;
import io.minestack.doublechest.databases.rabbitmq.pubsub.PubSubExchanges;
import io.minestack.doublechest.databases.rabbitmq.pubsub.PubSubPublisher;
import org.json.JSONObject;

import java.io.IOException;

public class GlobalMessagePublisher extends PubSubPublisher {

    public GlobalMessagePublisher() throws IOException {
        super(DoubleChest.INSTANCE.getRabbitMQDatabase(), PubSubExchanges.GLOBAL_MESSAGE.name());
    }

    public void sendMessage(String message) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);

        publish(jsonObject);
        close();
    }
}
