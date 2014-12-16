package io.minestack.doublechest.model.plugin.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.plugin.Plugin;
import io.minestack.doublechest.model.plugin.PluginConfig;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
public class MySQLPluginConfigRepository extends MySQLModelRepository<PluginConfig> {

    public MySQLPluginConfigRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<PluginConfig> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<PluginConfig> pluginConfigs = getMySQLDatabase().getBeansInfo(connection, "select id, name, description from plugin_configs", PluginConfig.class);

                for (PluginConfig pluginConfig : pluginConfigs) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select plugin_id from plugin_configs where id='"+pluginConfig.getId()+"'");
                    try {
                        pluginConfig.setPlugin(getMySQLDatabase().getPluginRepository().getModel((int) relations.get("plugin_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLPluginConfigRepository::getModels::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return pluginConfigs;
            }
        }, ArrayList.class);
    }

    @Override
    public PluginConfig getModel(int modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                PluginConfig pluginConfig = getMySQLDatabase().getBeanInfo(connection, "select id, name, description from plugin_configs where id='"+modelId+"'", PluginConfig.class);

                if (pluginConfig != null) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select plugin_id from plugin_configs where id='"+modelId+"'");
                    try {
                        pluginConfig.setPlugin(getMySQLDatabase().getPluginRepository().getModel((int) relations.get("plugin_id")));
                        return pluginConfig;
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLPluginConfigRepository::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                    return null;
                }
                return null;
            }
        }, PluginConfig.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PluginConfig> getPluginConfigForPlugin(Plugin plugin) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<PluginConfig> pluginConfigs = getMySQLDatabase().getBeansInfo(connection, "select id, name, description from plugin_configs where plugin_id='"+plugin.getId()+"'", PluginConfig.class);

                for (PluginConfig pluginConfig : pluginConfigs) {
                    pluginConfig.setPlugin(plugin);
                }

                return pluginConfigs;
            }
        }, ArrayList.class);
    }
}
