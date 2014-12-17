package io.minestack.doublechest.model.world.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.world.World;
import io.minestack.doublechest.model.world.WorldVersion;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
public class MySQLWorldVersionRepository extends MySQLModelRepository<WorldVersion> {

    public MySQLWorldVersionRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<WorldVersion> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<WorldVersion> worldVersions = getMySQLDatabase().getBeansInfo(connection, "select id, version, description, updated_at from world_versions", WorldVersion.class);

                for (WorldVersion worldVersion : worldVersions) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select world_id from world_versions where id='"+worldVersion.getId()+"'");

                    try {
                        World world = getMySQLDatabase().getWorldRepository().getModel((long)relations.get("world_id"));
                        worldVersion.setWorld(world);
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLWorldVersionRepository::getModels::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return worldVersions;
            }
        }, ArrayList.class);
    }

    @Override
    public WorldVersion getModel(long modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                WorldVersion worldVersion = getMySQLDatabase().getBeanInfo(connection, "select id, version, description, updated_at from world_versions where id='"+modelId+"'", WorldVersion.class);

                Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select world_id from world_versions where id='"+modelId+"'");

                if (worldVersion != null) {
                    try {
                        World world = getMySQLDatabase().getWorldRepository().getModel((long) relations.get("world_id"));
                        worldVersion.setWorld(world);
                        return worldVersion;
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLWorldVersionRepository::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                    return null;
                }
                return null;
            }
        }, WorldVersion.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<WorldVersion> getVersionsForWorld(World world) throws SQLException {
        ArrayList<WorldVersion> worldVersions = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeansInfo(connection, "select id, version, description, updated_at from world_versions where world_id='"+world.getId()+"'", WorldVersion.class);
            }
        }, ArrayList.class);

        for (WorldVersion worldVersion : worldVersions) {
            worldVersion.setWorld(world);
        }

        return worldVersions;
    }
}
