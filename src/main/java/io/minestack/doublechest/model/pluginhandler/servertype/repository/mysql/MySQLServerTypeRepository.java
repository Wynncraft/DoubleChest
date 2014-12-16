package io.minestack.doublechest.model.pluginhandler.servertype.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLServerTypeRepository extends MySQLModelRepository<ServerType> {

    public MySQLServerTypeRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<ServerType> getModels() throws SQLException {
        ArrayList<ServerType> serverTypes = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeansInfo(connection, "select id, name, description, ram, players from servertypes", ServerType.class);
            }
        }, ArrayList.class);

        for (ServerType serverType : serverTypes) {
            serverType.getWorlds().addAll(getMySQLDatabase().getWorldInfoRepostiroy().getWorldInfoForServerType(serverType));
            serverType.getPlugins().addAll(getMySQLDatabase().getPluginHolderPluginRepository().getPluginHolderPluginForPluginHolder(serverType));
        }

        return serverTypes;
    }

    @Override
    public ServerType getModel(int modelId) throws SQLException {
        ServerType serverType = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeanInfo(connection, "select id, name, description, ram, players from servertypes where id='"+modelId+"'", ServerType.class);
            }
        }, ServerType.class);

        if (serverType != null) {
            serverType.getWorlds().addAll(getMySQLDatabase().getWorldInfoRepostiroy().getWorldInfoForServerType(serverType));
            serverType.getPlugins().addAll(getMySQLDatabase().getPluginHolderPluginRepository().getPluginHolderPluginForPluginHolder(serverType));
        }

        return serverType;
    }
}
