package io.minestack.doublechest.model.node.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.network.Network;
import io.minestack.doublechest.model.node.NetworkNode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class MySQLNetworkNodeRepository extends MySQLModelRepository<NetworkNode> {

    public MySQLNetworkNodeRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<NetworkNode> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<NetworkNode> networkNodes = getMySQLDatabase().getBeansInfo(connection, "select id, updated_at from network_nodes", NetworkNode.class);

                for (NetworkNode networkNode : networkNodes) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select network_id, node_id, node_public_address_id, bungee_type_id where id='"+networkNode.getId()+"'");
                    try {
                        networkNode.setNetwork(getMySQLDatabase().getNetworkRepository().getModel((int) relations.get("network_id")));
                        networkNode.setNode(getMySQLDatabase().getNodeRepository().getModel((int) relations.get("node_id")));
                        if (relations.get("node_public_address_id") != null) {
                            networkNode.setNodePublicAddress(getMySQLDatabase().getNodePublicAddressRepository().getModel((int) relations.get("node_public_address_id")));
                            networkNode.setBungeeType(getMySQLDatabase().getBungeeTypeRepository().getModel((int) relations.get("bungee_type_id")));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                return networkNodes;
            }
        }, ArrayList.class);
    }

    @Override
    public NetworkNode getModel(int modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                NetworkNode networkNode = getMySQLDatabase().getBeanInfo(connection, "select id, updated_at from network_nodes where id='"+modelId+"'", NetworkNode.class);

                if (networkNode != null) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select network_id, node_id, node_public_address_id, bungee_type_id where id='"+modelId+"'");

                    try {
                        networkNode.setNetwork(getMySQLDatabase().getNetworkRepository().getModel((int) relations.get("network_id")));
                        networkNode.setNode(getMySQLDatabase().getNodeRepository().getModel((int) relations.get("node_id")));

                        if (relations.get("node_public_address_id") != null) {
                            networkNode.setNodePublicAddress(getMySQLDatabase().getNodePublicAddressRepository().getModel((int) relations.get("node_public_address_id")));
                            networkNode.setBungeeType(getMySQLDatabase().getBungeeTypeRepository().getModel((int) relations.get("bungee_type_id")));
                        }

                        return networkNode;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                return null;
            }
        }, NetworkNode.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<NetworkNode> getNetworkNodeForNetwork(Network network) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<NetworkNode> networkNodes = getMySQLDatabase().getBeansInfo(connection, "select id, updated_at from network_nodes", NetworkNode.class);

                for (NetworkNode networkNode : networkNodes) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select node_id, node_public_address_id, bungee_type_id where id='"+networkNode.getId()+"'");
                    try {
                        networkNode.setNetwork(network);
                        networkNode.setNode(getMySQLDatabase().getNodeRepository().getModel((int) relations.get("node_id")));
                        if (relations.get("node_public_address_id") != null) {
                            networkNode.setNodePublicAddress(getMySQLDatabase().getNodePublicAddressRepository().getModel((int) relations.get("node_public_address_id")));
                            networkNode.setBungeeType(getMySQLDatabase().getBungeeTypeRepository().getModel((int) relations.get("bungee_type_id")));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                return networkNodes;
            }
        }, ArrayList.class);
    }
}
