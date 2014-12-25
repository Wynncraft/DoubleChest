package io.minestack.doublechest.databases.rabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.HashMap;

@Log4j2
public abstract class WorkerQueue {

    private final Connection connection;
    private final String queueName;
    private Channel channel;
    private WorkerQueueConsumer consumer;
    private boolean stop = false;

    public WorkerQueue(RabbitMQDatabase rabbitMQDatabase, String queueName) throws IOException {
        connection = rabbitMQDatabase.getConnection();
        connection.addShutdownListener(this::shutdownListener);

        channel = connection.createChannel();

        this.queueName = queueName;

        consumerSetup();
    }

    private void consumerSetup() throws IOException {
        try {
            log.info("Connecting to Queue "+queueName);
            channel.queueDeclarePassive(queueName);
        } catch (IOException e) {
            channel = connection.createChannel();
            log.info("Creating Queue "+ queueName);
            HashMap<String, Object> args = new HashMap<>();
            args.put("x-ha-policy", "all");
            channel.queueDeclare(queueName, true, false, true, args);
        }
        channel.basicQos(1);
        consumer = new WorkerQueueConsumer(channel);
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-cancel-on-ha-failover", true);
        channel.basicConsume(queueName, false, args, consumer);
    }

    public void stopWorking() {
        try {
            stop = true;
            channel.basicCancel(consumer.getConsumerTag());
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void shutdownListener(ShutdownSignalException e);

    public abstract void messageDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body);

    class WorkerQueueConsumer extends DefaultConsumer {

        public WorkerQueueConsumer(Channel channel) {
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
