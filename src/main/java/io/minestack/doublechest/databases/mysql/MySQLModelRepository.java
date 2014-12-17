package io.minestack.doublechest.databases.mysql;

import io.minestack.doublechest.model.Model;
import lombok.Getter;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class MySQLModelRepository<T extends Model> {

    @Getter
    private final MySQLDatabase mySQLDatabase;

    public MySQLModelRepository(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    public abstract ArrayList<T> getModels() throws SQLException;

    public abstract T getModel(long modelId) throws SQLException;

}
