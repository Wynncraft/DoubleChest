package io.minestack.doublechest.databases.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.minestack.doublechest.databases.Database;

import java.io.IOException;
import java.util.List;

public class RabbitMQDatabase implements Database {

    private final List<Address> addressList;
    private final String username;
    private final String password;
    private ConnectionFactory factory;

    public RabbitMQDatabase(List<Address> addressList, String username, String password) {
        this.addressList = addressList;
        this.username = username;
        this.password = password;
    }

    @Override
    public void setupDatabase() {
        factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        //factory.setRequestedHeartbeat(25);
        factory.setConnectionTimeout(5000);
        factory.setNetworkRecoveryInterval(0);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
    }

    public Connection getConnection() throws IOException {
        return factory.newConnection(addressList.toArray(new Address[addressList.size()]));
    }
}
