package io.minestack.doublechest.model.plugin.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLPluginRepository extends MySQLModelRepository<Plugin> {

    public MySQLPluginRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Plugin> getModels() throws SQLException {
        ArrayList<Plugin> plugins = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeansInfo(connection, "select id, name, description, type, directory, updated_at from plugins", Plugin.class);
            }
        }, ArrayList.class);

        for (Plugin plugin : plugins) {
            plugin.getVersions().addAll(getMySQLDatabase().getPluginVersionRepository().getPluginVersionForPlugin(plugin));
            plugin.getConfigs().addAll(getMySQLDatabase().getPluginConfigRepository().getPluginConfigForPlugin(plugin));
        }

        return plugins;
    }

    @Override
    public Plugin getModel(int modelId) throws SQLException {
        Plugin plugin = getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                return getMySQLDatabase().getBeanInfo(connection, "select id, name, description, type, directory, updated_at from plugins where id='"+modelId+"'", Plugin.class);
            }
        }, Plugin.class);

        if (plugin != null) {
            plugin.getVersions().addAll(getMySQLDatabase().getPluginVersionRepository().getPluginVersionForPlugin(plugin));
            plugin.getConfigs().addAll(getMySQLDatabase().getPluginConfigRepository().getPluginConfigForPlugin(plugin));
        }

        return plugin;
    }
}
