package io.minestack.doublechest;

import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import lombok.Getter;

public class DoubleChest {

    public static DoubleChest INSTANCE = new DoubleChest();

    @Getter
    private MySQLDatabase mySQLDatabase;

    public void initMySQLDatabase(String userName, String password, String database, String address, int port) {
        mySQLDatabase = new MySQLDatabase(userName, password, database, address, port);
        mySQLDatabase.setupDatabase();
    }

}
