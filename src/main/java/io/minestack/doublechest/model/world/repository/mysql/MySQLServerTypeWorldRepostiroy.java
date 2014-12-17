package io.minestack.doublechest.model.world.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.pluginhandler.servertype.ServerType;
import io.minestack.doublechest.model.world.World;
import io.minestack.doublechest.model.world.ServerTypeWorld;
import io.minestack.doublechest.model.world.WorldVersion;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
public class MySQLServerTypeWorldRepostiroy extends MySQLModelRepository<ServerTypeWorld> {

    public MySQLServerTypeWorldRepostiroy(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<ServerTypeWorld> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<ServerTypeWorld> serverTypeWorlds = getMySQLDatabase().getBeansInfo(connection, "select id, defaultWorld, updated_at from servertype_worlds", ServerTypeWorld.class);

                for (ServerTypeWorld serverTypeWorld : serverTypeWorlds) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select servertype_id, world_id, worldversion_id from servertype_worlds where id='"+ serverTypeWorld.getId()+"'");
                    try {
                        ServerType serverType = getMySQLDatabase().getServerTypeRepository().getModel((long) relations.get("servertype_id"));
                        World world = getMySQLDatabase().getWorldRepository().getModel((long) relations.get("world_id"));
                        WorldVersion worldVersion = getMySQLDatabase().getWorldVersionRepository().getModel((long) relations.get("worldversion_id"));

                        serverTypeWorld.setServerType(serverType);
                        serverTypeWorld.setWorld(world);
                        serverTypeWorld.setVersion(worldVersion);
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLWorldInfoRepostiroy::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }
                return serverTypeWorlds;
            }
        }, ArrayList.class);
    }

    @Override
    public ServerTypeWorld getModel(long modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ServerTypeWorld serverTypeWorld;

                serverTypeWorld = getMySQLDatabase().getBeanInfo(connection, "select id, defaultWorld, updated_at from servertype_worlds where id='"+modelId+"'", ServerTypeWorld.class);

                if (serverTypeWorld != null) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select servertype_id, world_id, worldversion_id from servertype_worlds where id='"+modelId+"'");
                    try {
                        ServerType serverType = getMySQLDatabase().getServerTypeRepository().getModel((long) relations.get("servertype_id"));
                        World world = getMySQLDatabase().getWorldRepository().getModel((long) relations.get("world_id"));
                        WorldVersion worldVersion = getMySQLDatabase().getWorldVersionRepository().getModel((long) relations.get("worldversion_id"));

                        serverTypeWorld.setServerType(serverType);
                        serverTypeWorld.setWorld(world);
                        serverTypeWorld.setVersion(worldVersion);
                        return serverTypeWorld;
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLWorldInfoRepostiroy::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                    return null;
                }

                return null;
            }
        }, ServerTypeWorld.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ServerTypeWorld> getWorldInfoForServerType(ServerType serverType) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<ServerTypeWorld> serverTypeWorlds = getMySQLDatabase().getBeansInfo(connection, "select id, defaultWorld, updated_at from servertype_worlds where servertype_id='"+serverType.getId()+"'", ServerTypeWorld.class);

                for (ServerTypeWorld serverTypeWorld : serverTypeWorlds) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select world_id, worldversion_id from servertype_worlds where id='"+ serverTypeWorld.getId()+"'");
                    try {
                        World world = getMySQLDatabase().getWorldRepository().getModel((long) relations.get("world_id"));
                        WorldVersion worldVersion = getMySQLDatabase().getWorldVersionRepository().getModel((long) relations.get("worldversion_id"));

                        serverTypeWorld.setServerType(serverType);
                        serverTypeWorld.setWorld(world);
                        serverTypeWorld.setVersion(worldVersion);
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLWorldInfoRepostiroy::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }
                return serverTypeWorlds;
            }
        }, ArrayList.class);
    }
}
