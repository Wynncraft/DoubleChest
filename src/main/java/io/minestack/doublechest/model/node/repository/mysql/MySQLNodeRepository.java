package io.minestack.doublechest.model.node.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.node.Node;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Log4j2
public class MySQLNodeRepository extends MySQLModelRepository<Node> {

    public MySQLNodeRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Node> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<Node> nodes = getMySQLDatabase().getBeansInfo(connection, "select id, name, privateAddress, ram, updated_at from nodes", Node.class);

                for (Node node : nodes) {
                    try {
                        node.getPublicAddresses().addAll(getMySQLDatabase().getNodePublicAddressRepository().getNodePublicAddressesForNode(node));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLNodeRepository::getModels::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }
                return nodes;
            }
        }, ArrayList.class);
    }

    @Override
    public Node getModel(long modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                Node node = getMySQLDatabase().getBeanInfo(connection, "select id, name, privateAddress, ram, updated_at from nodes where id='"+modelId+"'", Node.class);
                if (node != null) {
                    try {
                        node.getPublicAddresses().addAll(getMySQLDatabase().getNodePublicAddressRepository().getNodePublicAddressesForNode(node));
                        return node;
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLNodeRepository::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                    return null;
                }
                return null;
            }
        }, Node.class);
    }
}
