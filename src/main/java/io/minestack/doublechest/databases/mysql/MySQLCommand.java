package io.minestack.doublechest.databases.mysql;

import java.sql.Connection;

public abstract class MySQLCommand {

    public abstract Object command(Connection connection);

}
