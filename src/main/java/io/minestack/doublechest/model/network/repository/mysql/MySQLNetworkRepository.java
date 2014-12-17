package io.minestack.doublechest.model.network.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.network.Network;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Log4j2
public class MySQLNetworkRepository extends MySQLModelRepository<Network> {

    public MySQLNetworkRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Network> getModels() throws SQLException {
        ArrayList<Network> networks = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeansInfo(connection, "select id, name, description, updated_at from networks", Network.class);
            }
        }, ArrayList.class);

        for (Network network : networks) {
            network.getNodes().addAll(getMySQLDatabase().getNetworkNodeRepository().getNetworkNodeForNetwork(network));
            network.getServerTypes().addAll(getMySQLDatabase().getNetworkServerTypeRepostiroy().getNetworkServerTypesForNetwork(network));
        }

        return networks;
    }

    @Override
    public Network getModel(long modelId) throws SQLException {
        Network network = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {

                return getMySQLDatabase().getBeanInfo(connection, "select id, name, description, updated_at from networks where id='"+modelId+"'", Network.class);
            }
        }, Network.class);

        if (network != null) {
            network.getNodes().addAll(getMySQLDatabase().getNetworkNodeRepository().getNetworkNodeForNetwork(network));
            network.getServerTypes().addAll(getMySQLDatabase().getNetworkServerTypeRepostiroy().getNetworkServerTypesForNetwork(network));
        }

        return network;
    }
}
