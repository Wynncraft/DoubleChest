package io.minestack.doublechest.model.pluginhandler.servertype.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.pluginhandler.servertype.NetworkServerType;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
public class MySQLNetworkServerTypeRepostiroy extends MySQLModelRepository<NetworkServerType> {

    public MySQLNetworkServerTypeRepostiroy(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<NetworkServerType> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<NetworkServerType> networkServerTypes = getMySQLDatabase().getBeansInfo(connection, "select id, amount, defaultServerType, updated_at from network_servertypes", NetworkServerType.class);

                for (NetworkServerType networkServerType : networkServerTypes) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select network_id, server_type_id from network_servertypes where id='"+networkServerType.getId()+"'");
                    try {
                        networkServerType.setNetwork(getMySQLDatabase().getNetworkRepository().getModel((int) relations.get("network_id")));
                        networkServerType.setServerType(getMySQLDatabase().getServerTypeRepository().getModel((int) relations.get("server_type_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLNetworkServerTypeRepostiroy::getModels::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return networkServerTypes;
            }
        }, ArrayList.class);
    }

    @Override
    public NetworkServerType getModel(int modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                NetworkServerType networkServerType = getMySQLDatabase().getBeanInfo(connection, "select id, amount, defaultServerType, updated_at from network_servertypes where id='"+modelId+"'", NetworkServerType.class);

                if (networkServerType != null) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select network_id, server_type_id from network_servertypes where id='"+modelId+"'");
                    try {
                        networkServerType.setNetwork(getMySQLDatabase().getNetworkRepository().getModel((int) relations.get("network_id")));
                        networkServerType.setServerType(getMySQLDatabase().getServerTypeRepository().getModel((int) relations.get("server_type_id")));
                        return networkServerType;
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLNetworkServerTypeRepostiroy::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                    return null;
                }
                return null;
            }
        }, NetworkServerType.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<NetworkServerType> getNetworkServerTypesForNetwork(Network network) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<NetworkServerType> networkServerTypes = getMySQLDatabase().getBeansInfo(connection, "select id, amount, defaultServerType, updated_at from network_servertypes", NetworkServerType.class);

                for (NetworkServerType networkServerType : networkServerTypes) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select server_type_id from network_servertypes where id='"+networkServerType.getId()+"'");
                    try {
                        networkServerType.setNetwork(network);
                        networkServerType.setServerType(getMySQLDatabase().getServerTypeRepository().getModel((int) relations.get("server_type_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLNetworkServerTypeRepostiroy::getNetworkServerTypesForNetwork::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return networkServerTypes;
            }
        }, ArrayList.class);
    }
}
