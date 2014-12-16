package io.minestack.doublechest.model.node.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.node.Node;
import io.minestack.doublechest.model.node.NodePublicAddress;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
public class MySQLNodePublicAddressRepository extends MySQLModelRepository<NodePublicAddress> {

    public MySQLNodePublicAddressRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<NodePublicAddress> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<NodePublicAddress> publicAddresses = getMySQLDatabase().getBeansInfo(connection, "select id, publicAddress, updated_at from node_public_addresses", NodePublicAddress.class);

                for (NodePublicAddress publicAddress : publicAddresses) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select node_id from node_public_addresses where id='"+publicAddress.getId()+"'");
                    try {
                        publicAddress.setNode(getMySQLDatabase().getNodeRepository().getModel((int) relations.get("node_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in NodePublicAddress::getModels::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return publicAddresses;
            }
        }, ArrayList.class);
    }

    @Override
    public NodePublicAddress getModel(int modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                NodePublicAddress publicAddress = getMySQLDatabase().getBeanInfo(connection, "select id, publicAddress, updated_at from node_public_addresses where id='"+modelId+"'", NodePublicAddress.class);

                if (publicAddress != null) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select node_id from node_public_addresses where id='"+modelId+"'");

                    try {
                        publicAddress.setNode(getMySQLDatabase().getNodeRepository().getModel((int) relations.get("node_id")));
                        return publicAddress;
                    } catch (SQLException e) {
                        log.error("Threw a Exception in NodePublicAddress::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }

                    return null;
                }

                return null;
            }
        }, NodePublicAddress.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<NodePublicAddress> getNodePublicAddressesForNode(Node node) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<NodePublicAddress> publicAddresses = getMySQLDatabase().getBeansInfo(connection, "select id, publicAddress, updated_at from node_public_addresses where node_id='"+node.getId()+"'", NodePublicAddress.class);

                for (NodePublicAddress publicAddress : publicAddresses) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select node_id from node_public_addresses where id='"+publicAddress.getId()+"'");
                    try {
                        publicAddress.setNode(getMySQLDatabase().getNodeRepository().getModel((int) relations.get("node_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in NodePublicAddress::getModels::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return publicAddresses;
            }
        }, ArrayList.class);
    }
}
