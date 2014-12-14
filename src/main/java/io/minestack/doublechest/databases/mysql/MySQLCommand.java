package io.minestack.doublechest.databases.mysql;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class MySQLCommand {

    public abstract Object command(Connection connection) throws SQLException;

}
