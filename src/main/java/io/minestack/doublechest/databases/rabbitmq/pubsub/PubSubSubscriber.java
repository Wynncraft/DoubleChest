package io.minestack.doublechest.databases.rabbitmq.pubsub;

import com.rabbitmq.client.*;
import io.minestack.doublechest.databases.rabbitmq.RabbitMQDatabase;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashMap;

@Log4j2
public abstract class PubSubSubscriber {

    private final Connection connection;
    private final String exchangeName;

    @Getter
    private Channel channel;

    private SubscriberConsumer consumer;
    private boolean stop = false;

    public PubSubSubscriber(RabbitMQDatabase rabbitMQDatabase, String exchangeName) throws IOException {
        connection = rabbitMQDatabase.getConnection();
        channel = connection.createChannel();
        this.exchangeName = exchangeName;
        consumerSetup();
    }

    private void consumerSetup() throws IOException {
        try {
            log.info("Connecting to Exchange "+exchangeName);
            channel.exchangeDeclarePassive(exchangeName);
        } catch (IOException e) {
            channel = connection.createChannel();
            log.info("Creating Exchange "+exchangeName);
            HashMap<String, Object> args = new HashMap<>();
            args.put("x-expires", 1800000);//expire exchange after 30 minutes of no activity
            channel.exchangeDeclare(exchangeName, "fanout", false, false, false, args);
        }

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");

        consumer = new SubscriberConsumer(channel);
        HashMap<String, Object> args = new HashMap<>();
        channel.basicConsume(queueName, true, args, consumer);
    }

    public void stopSubscribing() {
        try {
            stop = true;
            channel.basicCancel(consumer.getConsumerTag());
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void messageDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException;

    class SubscriberConsumer extends DefaultConsumer {

        public SubscriberConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleCancel(String consumerTag) throws IOException {
            if (!stop) {
                consumerSetup();
            }
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            messageDelivery(consumerTag, envelope, properties, body);
        }
    }
}
