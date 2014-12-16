package io.minestack.doublechest.model.plugin.repository.mysql;

import io.minestack.doublechest.databases.mysql.MySQLCommand;
import io.minestack.doublechest.databases.mysql.MySQLDatabase;
import io.minestack.doublechest.databases.mysql.MySQLModelRepository;
import io.minestack.doublechest.model.plugin.PluginHolderPlugin;
import io.minestack.doublechest.model.pluginhandler.PluginHolder;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@Log4j2
public class MySQLPluginHolderPluginRepository extends MySQLModelRepository<PluginHolderPlugin> {

    public MySQLPluginHolderPluginRepository(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<PluginHolderPlugin> getModels() throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<PluginHolderPlugin> pluginHolderPlugins = getMySQLDatabase().getBeansInfo(connection, "select id, updated_at from pluginholder_plugins", PluginHolderPlugin.class);

                for (PluginHolderPlugin pluginHolderPlugin : pluginHolderPlugins) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select pluginholder_id, pluginholder_type, " +
                            "plugin_id, pluginversion_id, pluginconfig_id from pluginholder_plugins where id='"+pluginHolderPlugin.getId()+"'");

                    String pluginHolderType = (String) relations.get("pluginholder_type");
                    try {
                        PluginHolder pluginHolder = null;
                        if (pluginHolderType.equals("ServerType")) {
                            pluginHolder = getMySQLDatabase().getServerTypeRepository().getModel((int) relations.get("pluginholder_id"));
                        } else if (pluginHolderType.equals("BungeeType")) {
                            pluginHolder = getMySQLDatabase().getBungeeTypeRepository().getModel((int) relations.get("pluginholder_id"));
                        }
                        pluginHolderPlugin.setPluginHolder(pluginHolder);
                        pluginHolderPlugin.setPlugin(getMySQLDatabase().getPluginRepository().getModel((int) relations.get("plugin_id")));
                        pluginHolderPlugin.setVersion(getMySQLDatabase().getPluginVersionRepository().getModel((int) relations.get("pluginversion_id")));
                        pluginHolderPlugin.setConfig(getMySQLDatabase().getPluginConfigRepository().getModel((int) relations.get("pluginconfig_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLPluginHolderPluginRepository::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return pluginHolderPlugins;
            }
        }, ArrayList.class);
    }

    @Override
    public PluginHolderPlugin getModel(int modelId) throws SQLException {
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                PluginHolderPlugin pluginHolderPlugin = getMySQLDatabase().getBeanInfo(connection, "select id, updated_at from pluginholder_plugins where id='"+modelId+"'", PluginHolderPlugin.class);

                if (pluginHolderPlugin != null) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select pluginholder_id, pluginholder_type, " +
                            "plugin_id, pluginversion_id, pluginconfig_id from pluginholder_plugins where id='"+modelId+"'");

                    String pluginHolderType = (String) relations.get("pluginholder_type");
                    try {
                        PluginHolder pluginHolder = null;
                        if (pluginHolderType.equals("ServerType")) {
                            pluginHolder = getMySQLDatabase().getServerTypeRepository().getModel((int) relations.get("pluginholder_id"));
                        } else if (pluginHolderType.equals("BungeeType")) {
                            pluginHolder = getMySQLDatabase().getBungeeTypeRepository().getModel((int) relations.get("pluginholder_id"));
                        }
                        pluginHolderPlugin.setPluginHolder(pluginHolder);
                        pluginHolderPlugin.setPlugin(getMySQLDatabase().getPluginRepository().getModel((int) relations.get("plugin_id")));
                        pluginHolderPlugin.setVersion(getMySQLDatabase().getPluginVersionRepository().getModel((int) relations.get("pluginversion_id")));
                        pluginHolderPlugin.setConfig(getMySQLDatabase().getPluginConfigRepository().getModel((int) relations.get("pluginconfig_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLPluginHolderPluginRepository::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return pluginHolderPlugin;
            }
        }, PluginHolderPlugin.class);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PluginHolderPlugin> getPluginHolderPluginForPluginHolder(PluginHolder pluginHolder) throws SQLException {
        String pluginHolderType = pluginHolder.getClass().getSimpleName();
        return getMySQLDatabase().executeCommand(new MySQLCommand() {
            @Override
            public Object command(Connection connection) {
                ArrayList<PluginHolderPlugin> pluginHolderPlugins = getMySQLDatabase().getBeansInfo(connection, "select id, updated_at from pluginholder_plugins where pluginholder_id='"+pluginHolder.getId()+"' and pluginholder_type='"+pluginHolderType+"'", PluginHolderPlugin.class);

                for (PluginHolderPlugin pluginHolderPlugin : pluginHolderPlugins) {
                    Map<String, Object> relations = getMySQLDatabase().getMapInfo(connection, "select plugin_id, pluginversion_id, pluginconfig_id from pluginholder_plugins where id='"+pluginHolderPlugin.getId()+"'");

                    try {
                        pluginHolderPlugin.setPluginHolder(pluginHolder);
                        pluginHolderPlugin.setPlugin(getMySQLDatabase().getPluginRepository().getModel((int) relations.get("plugin_id")));
                        pluginHolderPlugin.setVersion(getMySQLDatabase().getPluginVersionRepository().getModel((int) relations.get("pluginversion_id")));
                        pluginHolderPlugin.setConfig(getMySQLDatabase().getPluginConfigRepository().getModel((int) relations.get("pluginconfig_id")));
                    } catch (SQLException e) {
                        log.error("Threw a Exception in MySQLPluginHolderPluginRepository::getModel::MySQLCommand::command, full stack trace follows: ", e);
                    }
                }

                return pluginHolderPlugins;
            }
        }, ArrayList.class);
    }
}
