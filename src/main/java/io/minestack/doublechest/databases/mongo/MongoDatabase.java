package io.minestack.doublechest.databases.mongo;

import com.mongodb.*;
import io.minestack.doublechest.databases.Database;
import io.minestack.doublechest.model.bungee.repository.mongo.MongoBungeeRepository;
import io.minestack.doublechest.model.network.repository.mongo.MongoNetworkRepository;
import io.minestack.doublechest.model.node.repository.mongo.MongoNodeRepository;
import io.minestack.doublechest.model.plugin.repository.mongo.MongoPluginRepository;
import io.minestack.doublechest.model.pluginhandler.bungeetype.repository.mongo.MongoBungeeTypeRepository;
import io.minestack.doublechest.model.pluginhandler.servertype.repository.mongo.MongoServerTypeRepository;
import io.minestack.doublechest.model.server.repository.mongo.MongoServerRepository;
import io.minestack.doublechest.model.world.repository.mongo.MongoWorldRepository;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class MongoDatabase implements Database {

    private final List<ServerAddress> addressList;
    private final String username;
    private final String password;
    private final String database;
    private DB db;

    @Getter
    private final MongoWorldRepository worldRepository;

    @Getter
    private final MongoServerRepository serverRepository;

    @Getter
    private final MongoServerTypeRepository serverTypeRepository;

    @Getter
    private final MongoBungeeTypeRepository bungeeTypeRepository;

    @Getter
    private final MongoPluginRepository pluginRepository;

    @Getter
    private final MongoNodeRepository nodeRepository;

    @Getter
    private final MongoNetworkRepository networkRepository;

    @Getter
    private final MongoBungeeRepository bungeeRepository;

    public MongoDatabase(List<ServerAddress> addressList, String username, String password, String database) {
        this.addressList = addressList;
        this.username = username;
        this.password = password;
        this.database = database;

        worldRepository = new MongoWorldRepository(this);
        serverRepository = new MongoServerRepository(this);
        serverTypeRepository = new MongoServerTypeRepository(this);
        bungeeTypeRepository = new MongoBungeeTypeRepository(this);
        pluginRepository = new MongoPluginRepository(this);
        nodeRepository = new MongoNodeRepository(this);
        networkRepository = new MongoNetworkRepository(this);
        bungeeRepository = new MongoBungeeRepository(this);
    }

    @Override
    public void setupDatabase() {
        MongoClientOptions clientOptions = MongoClientOptions.builder().connectTimeout(30000)
                .heartbeatConnectRetryFrequency(15)
                .heartbeatConnectTimeout(15)
                .heartbeatFrequency(15)
                .heartbeatThreadCount(1)
                .build();
        MongoClient mongoClient;
        if (username != null) {
            MongoCredential credential = MongoCredential.createMongoCRCredential(username, database, password.toCharArray());
            mongoClient = new MongoClient(addressList, Arrays.asList(credential), clientOptions);
        } else {
            mongoClient = new MongoClient(addressList, clientOptions);
        }

        if (addressList.size() > 1) {
            mongoClient.setWriteConcern(WriteConcern.REPLICA_ACKNOWLEDGED);
            mongoClient.setReadPreference(ReadPreference.primaryPreferred());
        }
        db = mongoClient.getDB(database);
    }

    private DB getDatabase() {
        db.requestEnsureConnection();
        return db;
    }

    private DBCollection getCollection(String collectionName) {
        DB database = getDatabase();
        return database.getCollection(collectionName);
    }

    public long count(String collection, DBObject query) {
        DBCollection dbCollection = getCollection(collection);
        return dbCollection.count(query);
    }

    public DBObject findOne(String collection, DBObject query) {
        DBObject dbObject;
        DBCollection dbCollection = getCollection(collection);
        DBCursor dbCursor = dbCollection.find(query).limit(1);
        if (dbCursor.hasNext() == false) {
            return null;
        }
        dbObject = dbCursor.next();
        dbCursor.close();
        return dbObject;
    }

    public DBCursor findMany(String collection) {
        DBCollection dbCollection = getCollection(collection);
        return dbCollection.find();
    }
    public DBCursor findMany(String collection, DBObject query) {
        DBCollection dbCollection = getCollection(collection);
        return dbCollection.find(query);
    }

    public void insert(String collection, DBObject object) {
        DBCollection dbCollection = getCollection(collection);
        dbCollection.insert(object);
    }

    public void updateDocument(String collection, DBObject query, DBObject document) {
        DBCollection dbCollection = getCollection(collection);
        dbCollection.update(query, document);
    }

    public void remove(String collection, DBObject query) {
        DBCollection dbCollection = getCollection(collection);
        dbCollection.remove(query);
    }
}
