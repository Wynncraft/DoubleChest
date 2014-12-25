package io.minestack.doublechest.databases.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

@Log4j2
public class Publisher {

    private final Connection connection;
    private final String queueName;
    private final String exchangeName;
    private final ExchangeType exchangeType;
    private Channel channel;

    public Publisher(RabbitMQDatabase rabbitMQDatabase, String queueName) throws IOException {
        connection = rabbitMQDatabase.getConnection();
        channel = connection.createChannel();
        this.queueName = queueName;
        this.exchangeName = null;
        this.exchangeType = null;
        publisherSetup();
    }

    public Publisher(RabbitMQDatabase rabbitMQDatabase, String queueName, String exchangeName, ExchangeType exchangeType) throws IOException {
        connection = rabbitMQDatabase.getConnection();
        channel = connection.createChannel();
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        publisherSetup();
    }

    public Publisher(RabbitMQDatabase rabbitMQDatabase, String exchangeName, ExchangeType exchangeType) throws IOException {
        connection = rabbitMQDatabase.getConnection();
        channel = connection.createChannel();
        this.queueName = null;
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        publisherSetup();
    }

    public void publisherSetup() throws IOException {
        if (queueName != null) {
            try {
                log.info("Connecting to Queue " + queueName);
                channel.queueDeclarePassive(queueName);
            } catch (IOException e) {
                channel = connection.createChannel();
                log.info("Creating Queue " + queueName);
                HashMap<String, Object> args = new HashMap<>();
                args.put("x-ha-policy", "all");
                args.put("x-expires", 1800000);//expire queue after 30 minutes of no activity
                channel.queueDeclare(queueName, false, false, false, args);
            }
        }

        if (exchangeName != null) {
            try {
                log.info("Connecting to Exchange "+exchangeName);
                channel.exchangeDeclarePassive(exchangeName);
            } catch (IOException e) {
                channel = connection.createChannel();
                log.info("Creating Exchange "+exchangeName);
                HashMap<String, Object> args = new HashMap<>();
                args.put("x-expires", 1800000);//expire exchange after 30 minutes of no activity
                channel.exchangeDeclare(exchangeName, exchangeType.getValue(), false, false, false, args);
            }
        }
    }

    public void publish(JSONObject message) throws IOException {
        if (exchangeName == null && queueName != null) {
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toString().getBytes());
        }
        if (exchangeName != null && queueName == null) {
            channel.basicPublish(exchangeName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.toString().getBytes());
        }
        if (exchangeName != null && queueName != null) {
            channel.basicPublish(exchangeName, queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toString().getBytes());
        }
        channel.close();
        connection.close();
    }
}
