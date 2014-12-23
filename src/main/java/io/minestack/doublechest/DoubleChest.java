package io.minestack.doublechest;

import com.mongodb.ServerAddress;
import io.minestack.doublechest.databases.mongo.MongoDatabase;
import lombok.Getter;

import java.util.List;

public class DoubleChest {

    public static DoubleChest INSTANCE = new DoubleChest();

    @Getter
    private MongoDatabase mongoDatabase;

    public void initMongoDatabase(List<ServerAddress> addressList, String database) {
        initMongoDatabase(addressList, null, null, database);
    }

    public void initMongoDatabase(List<ServerAddress> addressList, String username, String password, String database) {
        mongoDatabase = new MongoDatabase(addressList, username, password, database);
        mongoDatabase.setupDatabase();
    }

}
