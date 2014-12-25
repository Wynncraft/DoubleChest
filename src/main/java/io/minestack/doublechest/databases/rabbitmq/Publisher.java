package io.minestack.doublechest.databases.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.io.IOException;

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
            log.info("Connecting to Queue " + queueName);
            channel.queueDeclarePassive(queueName);
        }

        if (exchangeName != null) {
            log.info("Connecting to Exchange " + exchangeName);
            channel.exchangeDeclarePassive(exchangeName);
        }
    }

    public void publish(JSONObject message) throws IOException {
        if (exchangeName == null && queueName != null) {
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toString().getBytes());
        } else if (exchangeName != null && queueName == null) {
            channel.basicPublish(exchangeName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.toString().getBytes());
        } else if (exchangeName != null) {
            channel.basicPublish(exchangeName, queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toString().getBytes());
        }
        channel.close();
        connection.close();
    }
}
