package io.minestack.doublechest.databases.rabbitmq.worker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import io.minestack.doublechest.databases.rabbitmq.RabbitMQDatabase;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

@Log4j2
public class WorkerPublisher {

    private final Connection connection;
    private final String queueName;
    private Channel channel;

    public WorkerPublisher(RabbitMQDatabase rabbitMQDatabase, String queueName) throws IOException {
        connection = rabbitMQDatabase.getConnection();
        channel = connection.createChannel();
        this.queueName = queueName;
        publisherSetup();
    }

    public void publisherSetup() throws IOException {

        try {
            log.info("Connecting to Worker Queue " + queueName);
            channel.queueDeclarePassive(queueName);
        } catch (IOException e) {
            if (channel.isOpen()) {
                channel.close();
            }
            channel = connection.createChannel();
            log.info("Creating Worker Queue " + queueName);
            HashMap<String, Object> args = new HashMap<>();
            args.put("x-ha-policy", "all");
            args.put("x-expires", 1800000);//expire queue after 30 minutes of no activity
            channel.queueDeclare(queueName, false, false, false, args);
        }
    }

    public void publish(JSONObject message) throws IOException {
        channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toString().getBytes());
    }

    public void close() throws IOException {
        channel.close();
        connection.close();
    }
}
