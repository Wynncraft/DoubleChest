package io.minestack.doublechest.databases.mysql;

import io.minestack.doublechest.databases.Database;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MySQLDatabase implements Database {

    private final String userName;
    private final String password;
    private final String database;
    private final String address;
    private final int port;
    private BasicDataSource dataSource;

    public MySQLDatabase(String userName, String password, String database, String address, int port) {
        this.userName = userName;
        this.password = password;
        this.database = database;
        this.address = address;
        this.port = port;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection(Connection connection) {
        DbUtils.closeQuietly(connection);
    }

    @Override
    public void setupDatabase() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + address + ":" + port + "/" + database);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setMaxActive(100);
        dataSource.setMaxIdle(10);
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        dataSource.setNumTestsPerEvictionRun(3);
        dataSource.setTimeBetweenEvictionRunsMillis(1800000);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
    }

    public boolean isTable(String tableName) {
        boolean exists = false;
        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::isTable, full stack trace follows: ", ex);
            return false;
        }
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

    public void createTable(String sql) {
        if (sql.startsWith("CREATE") == false && sql.startsWith("create") == false) {
            log.warn("Can only be used to create tables!");
            return;
        }
        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::createTable, full stack trace follows: ", ex);
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

    public ArrayList<Object> getBeansInfo(String sql, ResultSetHandler resultSetHandler) {
        ArrayList<Object> beansInfo = new ArrayList<>();
        try {
            QueryRunner run = new QueryRunner(dataSource);
            List beans = (List) run.query(sql, resultSetHandler);
            for (Object bean : beans) {
                beansInfo.add(bean);
            }
        } catch (Exception ex) {
            log.error("Threw a SQLException in MySQLDatabase::getBeansInfo, full stack trace follows: ", ex);
        }
        return beansInfo;
    }

    public void updateQuery(String s, Object... params) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            if (s.toLowerCase().startsWith("select") == false) {
                run.update(s);
            } else {
                log.warn("UpdateQuery can not be used to select!");
            }
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::updateQuery, full stack trace follows: ", ex);
        }

    }

    public void updateQuery(String s) {
        try {
            QueryRunner run = new QueryRunner(dataSource);
            if (s.toLowerCase().startsWith("select") == false) {
                run.update(s);
            } else {
                log.warn("UpdateQuery can not be used to select!");
            }
        } catch (SQLException ex) {
            log.error("Threw a SQLException in MySQLDatabase::updateQuery, full stack trace follows: ", ex);
        }
    }
}
