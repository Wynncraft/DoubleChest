package io.minestack.doublechest.databases.mysql;

import io.minestack.doublechest.model.Model;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class MySQLModelRepository<T extends Model> {

    @Getter
    private final MySQLDatabase mySQLDatabase;

    public MySQLModelRepository(MySQLDatabase mySQLDatabase, String tableName) throws SQLException {
        this.mySQLDatabase = mySQLDatabase;
        mySQLDatabase.executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                if (mySQLDatabase.isTable(connection, tableName) == false) {
                    createTable(connection, tableName);
                }
                return 0;
            }
        });
    }

    public abstract void createTable(Connection connection, String tableName);

    public abstract ArrayList<T> getModels();

    public abstract T getModel(int modelId);

    public abstract void saveModel(T model);

}
