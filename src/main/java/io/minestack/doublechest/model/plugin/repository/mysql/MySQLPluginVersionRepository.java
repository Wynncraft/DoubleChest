package io.minestack.doublechest.model.plugin.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.plugin.Plugin;
import io.minestack.doublechest.model.plugin.PluginVersion;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
public class MySQLPluginVersionRepository extends MySQLModelRepository<PluginVersion> {

    public MySQLPluginVersionRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<PluginVersion> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<PluginVersion> pluginVersions = getMySQLDatabase().getBeansInfo(connection, "select id, version, description from plugin_versions", PluginVersion.class);

                for (PluginVersion pluginVersion : pluginVersions) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select plugin_id from plugin_versions where id='"+pluginVersion.getId()+"'");
                    try {
                        pluginVersion.setPlugin(getMySQLDatabase().getPluginRepository().getModel((int) relations.get("plugin_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLPluginVersionRepository::getModels::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return pluginVersions;
            }
        }, ArrayList.class);
    }

    @Override
    public PluginVersion getModel(int modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                PluginVersion pluginVersion = getMySQLDatabase().getBeanInfo(connection, "select id, version, description from plugin_versions where id='"+modelId+"'", PluginVersion.class);

                if (pluginVersion != null) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select plugin_id from plugin_versions where id='"+modelId+"'");
                    try {
                        pluginVersion.setPlugin(getMySQLDatabase().getPluginRepository().getModel((int) relations.get("plugin_id")));
                        return pluginVersion;
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLPluginVersionRepository::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                    return null;
                }
                return null;
            }
        }, PluginVersion.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PluginVersion> getPluginVersionForPlugin(Plugin plugin) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<PluginVersion> pluginVersions = getMySQLDatabase().getBeansInfo(connection, "select id, version, description from plugin_versions where plugin_id='"+plugin.getId()+"'", PluginVersion.class);

                for (PluginVersion pluginVersion : pluginVersions) {
                    pluginVersion.setPlugin(plugin);
                }

                return pluginVersions;
            }
        }, ArrayList.class);
    }
}
