package io.minestack.doublechest;

import com.mongodb.ServerAddress;
import com.rabbitmq.client.Address;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import io.minestack.doublechest.databases.rabbitmq.RabbitMQDatabase;
import lombok.Getter;

import java.util.List;

public class DoubleChest {

    public static DoubleChest INSTANCE = new DoubleChest();

    @Getter
    private MongoDatabase mongoDatabase;

    @Getter
    private RabbitMQDatabase rabbitMQDatabase;

    public void initRabbitMQDatabase(List<Address> addressList, String username, String password) {
        rabbitMQDatabase = new RabbitMQDatabase(addressList, username, password);
        rabbitMQDatabase.setupDatabase();
    }

    public void initMongoDatabase(List<ServerAddress> addressList, String database) {
        initMongoDatabase(addressList, null, null, database);
    }

    public void initMongoDatabase(List<ServerAddress> addressList, String username, String password, String database) {
        mongoDatabase = new MongoDatabase(addressList, username, password, database);
        mongoDatabase.setupDatabase();
    }

}
