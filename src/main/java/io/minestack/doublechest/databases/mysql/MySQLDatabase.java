package io.minestack.doublechest.databases.mysql;

import io.minestack.doublechest.databases.Database;
import io.minestack.doublechest.model.network.repository.mysql.MySQLNetworkRepository;
import io.minestack.doublechest.model.node.repository.mysql.MySQLNetworkNodeRepository;
import io.minestack.doublechest.model.node.repository.mysql.MySQLNodePublicAddressRepository;
import io.minestack.doublechest.model.node.repository.mysql.MySQLNodeRepository;
import io.minestack.doublechest.model.plugin.repository.mysql.MySQLPluginConfigRepository;
import io.minestack.doublechest.model.plugin.repository.mysql.MySQLPluginHolderPluginRepository;
import io.minestack.doublechest.model.plugin.repository.mysql.MySQLPluginRepository;
import io.minestack.doublechest.model.plugin.repository.mysql.MySQLPluginVersionRepository;
import io.minestack.doublechest.model.pluginhandler.bungeetype.repository.mysql.MySQLBungeeTypeRepository;
import io.minestack.doublechest.model.pluginhandler.servertype.repository.mysql.MySQLNetworkServerTypeRepostiroy;
import io.minestack.doublechest.model.pluginhandler.servertype.repository.mysql.MySQLServerTypeRepository;
import io.minestack.doublechest.model.world.repository.mysql.MySQLServerTypeWorldRepostiroy;
import io.minestack.doublechest.model.world.repository.mysql.MySQLWorldRepository;
import io.minestack.doublechest.model.world.repository.mysql.MySQLWorldVersionRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class MySQLDatabase implements Database {

    private final String userName;
    private final String password;
    private final String database;
    private final String address;
    private final int port;
    private BasicDataSource dataSource;

    @Getter
    private final MySQLWorldRepository worldRepository;

    @Getter
    private final MySQLWorldVersionRepository worldVersionRepository;

    @Getter
    private final MySQLServerTypeWorldRepostiroy worldInfoRepostiroy;

    @Getter
    private final MySQLServerTypeRepository serverTypeRepository;

    @Getter
    private final MySQLNetworkServerTypeRepostiroy networkServerTypeRepostiroy;

    @Getter
    private final MySQLBungeeTypeRepository bungeeTypeRepository;

    @Getter
    private final MySQLPluginRepository pluginRepository;

    @Getter
    private final MySQLPluginVersionRepository pluginVersionRepository;

    @Getter
    private final MySQLPluginConfigRepository pluginConfigRepository;

    @Getter
    private final MySQLPluginHolderPluginRepository pluginHolderPluginRepository;

    @Getter
    private final MySQLNodeRepository nodeRepository;

    @Getter
    private final MySQLNodePublicAddressRepository nodePublicAddressRepository;

    @Getter
    private final MySQLNetworkNodeRepository networkNodeRepository;

    @Getter
    private final MySQLNetworkRepository networkRepository;

    public MySQLDatabase(String userName, String password, String database, String address, int port) {
        this.userName = userName;
        this.password = password;
        this.database = database;
        this.address = address;
        this.port = port;

        this.worldRepository = new MySQLWorldRepository(this);
        this.worldVersionRepository = new MySQLWorldVersionRepository(this);
        this.worldInfoRepostiroy = new MySQLServerTypeWorldRepostiroy(this);

        this.serverTypeRepository = new MySQLServerTypeRepository(this);
        this.networkServerTypeRepostiroy = new MySQLNetworkServerTypeRepostiroy(this);

        this.bungeeTypeRepository = new MySQLBungeeTypeRepository(this);

        this.pluginRepository = new MySQLPluginRepository(this);
        this.pluginVersionRepository = new MySQLPluginVersionRepository(this);
        this.pluginConfigRepository = new MySQLPluginConfigRepository(this);
        this.pluginHolderPluginRepository = new MySQLPluginHolderPluginRepository(this);

        this.nodeRepository = new MySQLNodeRepository(this);
        this.nodePublicAddressRepository = new MySQLNodePublicAddressRepository(this);
        this.networkNodeRepository = new MySQLNetworkNodeRepository(this);

        this.networkRepository = new MySQLNetworkRepository(this);
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void closeConnection(Connection connection) {
        DbUtils.closeQuietly(connection);
    }

    @Override
    public void setupDatabase() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + address + ":" + port + "/" + database);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setMaxTotal(100);
        dataSource.setMaxIdle(10);
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        dataSource.setNumTestsPerEvictionRun(3);
        dataSource.setTimeBetweenEvictionRunsMillis(1800000);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
    }

    public Object executeCommand(MySQLCommand command) throws SQLException {
        return executeCommand(command, Object.class);
    }

    public <T> T executeCommand(MySQLCommand command, Class<T> resultClass) throws SQLException {
        Connection conn = getConnection();
        Object result = command.command(conn);
        closeConnection(conn);
        return resultClass.cast(result);
    }

    protected boolean isTable(Connection connection, String tableName) {
        boolean exists = false;
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getTables(null, null, tableName, null);
            exists = rs.next();
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::isTable, full stack trace follows: ", ex);
            return false;
        } finally {
            closeConnection(connection);
        }
        return exists;
    }

    public void createTable(Connection connection, String sql) {
        if (sql.toLowerCase().startsWith("create") == false) {
            log.warn("Can only be used to create tables!");
            return;
        }
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::createTable, full stack trace follows: ", ex);
        } finally {
            closeConnection(connection);
        }
    }

    public <T> T getBeanInfo(Connection conn, String sql, Class<T> resultClass) {
        T beanInfo = null;
        try {
            QueryRunner run = new QueryRunner();
            beanInfo = run.query(conn, sql, new BeanHandler<>(resultClass));
        } catch (Exception ex) {
            log.error("Threw a SQLException in MySQLDatabase::getBeansInfo, full stack trace follows: ", ex);
        }
        return beanInfo;
    }

    public <T> ArrayList<T> getBeansInfo(Connection conn, String sql, Class<T> resultClass) {
        ArrayList<T> beansInfo = new ArrayList<>();
        try {
            QueryRunner run = new QueryRunner();
            beansInfo.addAll(run.query(conn, sql, new BeanListHandler<>(resultClass)));
        } catch (Exception ex) {
            log.error("Threw a SQLException in MySQLDatabase::getBeansInfo, full stack trace follows: ", ex);
        }
        return beansInfo;
    }

    public ArrayList<Map<String, Object>> getMapsInfo(Connection connection, String sql) {
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        try {
            QueryRunner run = new QueryRunner();
            maps.addAll(run.query(connection, sql, new MapListHandler()));
        } catch (Exception ex) {
            log.error("Threw a SQLException in MySQLDatabase::getMapInfo, full stack trace follows: ", ex);
        }
        return maps;
    }

    public Map<String, Object> getMapInfo(Connection connection, String sql) {
        Map<String, Object> map = new HashMap<>();
        try {
            QueryRunner run = new QueryRunner();
            map = run.query(connection, sql, new MapHandler());
        } catch (Exception ex) {
            log.error("Threw a SQLException in MySQLDatabase::getMapInfo, full stack trace follows: ", ex);
        }
        return map;
    }

    public void updateQuery(Connection conn, String s, Object... params) {
        try {
            QueryRunner run = new QueryRunner();
            if (s.toLowerCase().startsWith("select") == false) {
                run.update(conn, s);
            } else {
                log.warn("UpdateQuery can not be used to select!");
            }
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::updateQuery, full stack trace follows: ", ex);
        }

    }

    public void updateQuery(Connection conn, String s) {
        try {
            QueryRunner run = new QueryRunner();
            if (s.toLowerCase().startsWith("select") == false) {
                run.update(conn, s);
            } else {
                log.warn("UpdateQuery can not be used to select!");
            }
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::updateQuery, full stack trace follows: ", ex);
        }
    }
}
