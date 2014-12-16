package io.minestack.doublechest.model.pluginhandler.bungeetype.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.pluginhandler.bungeetype.BungeeType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLBungeeTypeRepository extends MySQLModelRepository<BungeeType> {

    public MySQLBungeeTypeRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<BungeeType> getModels() throws SQLException {
        ArrayList<BungeeType> bungeeTypes = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeansInfo(connection, "select id, name, description, ram, updated_at from bungeetypes", BungeeType.class);
            }
        }, ArrayList.class);

        for (BungeeType bungeeType : bungeeTypes) {
            bungeeType.getPlugins().addAll(getMySQLDatabase().getPluginHolderPluginRepository().getPluginHolderPluginForPluginHolder(bungeeType));
        }

        return bungeeTypes;
    }

    @Override
    public BungeeType getModel(int modelId) throws SQLException {
        BungeeType bungeeType = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeanInfo(connection, "select id, name, description, ram, updated_at from bungeetypes where id='"+modelId+"'", BungeeType.class);
            }
        }, BungeeType.class);

        bungeeType.getPlugins().addAll(getMySQLDatabase().getPluginHolderPluginRepository().getPluginHolderPluginForPluginHolder(bungeeType));

        return bungeeType;
    }
}
