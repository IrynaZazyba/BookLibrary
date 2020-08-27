package com.itechart.javalab.library.dao.conn;

import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static volatile ConnectionPool instance;
    private MariaDbPoolDataSource pool;

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public void initPoolData(String dbUrl, String dbUser, String dbPassword, int dbPoolSize) throws SQLException {
        pool = new MariaDbPoolDataSource(dbUrl);
        pool.setMaxPoolSize(dbPoolSize);
        pool.setUser(dbUser);
        pool.setPassword(dbPassword);
    }


    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void closePool() {
        pool.close();
    }

}