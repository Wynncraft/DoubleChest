package io.minestack.doublechest.databases.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.minestack.doublechest.databases.rabbitmq.ExchangeType;
import io.minestack.doublechest.databases.rabbitmq.RabbitMQDatabase;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

@Log4j2
public class PubSubPublisher {

    private final Connection connection;
    private final String exchangeName;
    private Channel channel;

    public PubSubPublisher(RabbitMQDatabase rabbitMQDatabase, String exchangeName) throws IOException {
        connection = rabbitMQDatabase.getConnection();
        channel = connection.createChannel();
        this.exchangeName = exchangeName;
        publisherSetup();
    }

    public void publisherSetup() throws IOException {
        try {
            log.info("Connecting to PubSub Exchange " + exchangeName);
            channel.exchangeDeclarePassive(exchangeName);
        } catch (IOException e) {
            channel = connection.createChannel();
            log.info("Creating PubSub Exchange " + exchangeName);
            HashMap<String, Object> args = new HashMap<>();
            args.put("x-expires", 1800000);//expire exchange after 30 minutes of no activity
            channel.exchangeDeclare(exchangeName, ExchangeType.FANOUT.getValue(), false, false, false, args);
        }
    }

    public void publish(JSONObject message) throws IOException {
        channel.basicPublish(exchangeName, "", null, message.toString().getBytes());
    }

    public void close() throws IOException {
        channel.close();
        connection.close();
    }
}
