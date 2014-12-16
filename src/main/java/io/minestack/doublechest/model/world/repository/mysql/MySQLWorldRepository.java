package io.minestack.doublechest.model.world.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.world.World;
import io.minestack.doublechest.model.world.WorldVersion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLWorldRepository extends MySQLModelRepository<World> {


    public MySQLWorldRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<World> getModels() throws SQLException {

        ArrayList<World> worlds = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeansInfo(connection, "select id, name, description, directory, updated_at from worlds", World.class);
            }
        }, ArrayList.class);

        for (World world : worlds) {
            ArrayList<WorldVersion> worldVersions = getMySQLDatabase().getWorldVersionRepository().getVersionsForWorld(world);

            for (WorldVersion worldVersion : worldVersions) {
                worldVersion.setWorld(world);
                world.getVersions().add(worldVersion);
            }
        }

        return worlds;
    }

    @Override
    public World getModel(int modelId) throws SQLException {
        World world = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeanInfo(connection, "select id, name, description, directory, updated_at from worlds where id='"+modelId+"'", World.class);
            }
        }, World.class);

        if (world != null) {
            ArrayList<WorldVersion> worldVersions = getMySQLDatabase().getWorldVersionRepository().getVersionsForWorld(world);

            for (WorldVersion worldVersion : worldVersions) {
                worldVersion.setWorld(world);
                world.getVersions().add(worldVersion);
            }
        }

        return world;
    }


}
